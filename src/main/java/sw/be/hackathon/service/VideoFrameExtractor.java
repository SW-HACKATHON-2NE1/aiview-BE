package sw.be.hackathon.service;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import org.apache.catalina.Loader;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.videoio.VideoCapture;
import org.springframework.stereotype.Service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.S3ObjectInputStream;

import lombok.RequiredArgsConstructor;
import software.amazon.awssdk.core.sync.ResponseTransformer;
import software.amazon.awssdk.regions.Region;

@Service
@RequiredArgsConstructor
public class VideoFrameExtractor {

	private final AmazonS3Client amazonS3Client;

	static{
		System.load("D:/opencv/build/java/x64/opencv_java451.dll");
	}

	public List<BufferedImage> extractS3VideoToImage(String s3Bucket, String s3Key) {


		// 로컬에 저장할 디렉토리와 파일 경로 설정
		String localDir = "ainterview-video";
		File localFile = new File(localDir, Paths.get(s3Key).getFileName().toString());
		String localFilePath = localDir + "/" + Paths.get(s3Key).getFileName().toString(); // 파일 이름을 가져와 로컬 경로를 완성합니다.

		// S3에서 비디오 다운로드
		try (S3ObjectInputStream s3is = amazonS3Client.getObject(s3Bucket, s3Key).getObjectContent();
			 OutputStream os = new FileOutputStream(localFile)) {

			byte[] buffer = new byte[1024];
			int bytesRead;
			while ((bytesRead = s3is.read(buffer)) > 0) {
				os.write(buffer, 0, bytesRead);
			}
			System.out.println("Video downloaded successfully.");
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("Failed to download video.");
		}

		List<BufferedImage> images = new ArrayList<>();

		VideoCapture videoCapture = new VideoCapture(localFilePath);
		Mat frame = new Mat();
		int frameNumber = 0;

		if (videoCapture.isOpened()) {
			while (true) {
				if (videoCapture.read(frame)) {
					frameNumber++;

					if (frameNumber % 30 == 0) { // Assuming 30 FPS video
						MatOfByte matOfByte = new MatOfByte();
						Imgcodecs.imencode(".png", frame, matOfByte);
						byte[] byteArray = matOfByte.toArray();
						InputStream in = new ByteArrayInputStream(byteArray);

						try {
							BufferedImage bufImage = ImageIO.read(in);
							images.add(bufImage);
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				} else {
					break; // Video end
				}
			}
			videoCapture.release();
		}

		return images;
	}

	public void runPythonScript(String videoPath) throws IOException {


		try {
			String command = "python py-script/video_extract_image.py";
			ProcessBuilder pb = new ProcessBuilder(command);
			pb.environment().put("FILE_PATH", videoPath);
			pb.redirectErrorStream(true);
			Process p = pb.start();
			BufferedReader in = new BufferedReader(new InputStreamReader(p.getInputStream()));
			String s = "-1";
			while (true) {
				String temp = in.readLine();
				if (temp == null) break;
				s = temp;
				System.out.println("s = " + s);
			}
			//			deleteFile();
		} catch (Exception e) {
			e.printStackTrace();
			//			deleteFile();
		}
	}

}
