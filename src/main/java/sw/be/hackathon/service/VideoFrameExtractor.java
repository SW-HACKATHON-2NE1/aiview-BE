package sw.be.hackathon.service;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.InputStream;

import javax.imageio.ImageIO;

import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.Java2DFrameConverter;
import org.springframework.stereotype.Service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class VideoFrameExtractor {

	private final AmazonS3Client amazonS3Client;

	public void extractS3VideoToImage(String bucketName, String keyName) {

		try(Java2DFrameConverter converter = new Java2DFrameConverter();) {
			// S3에서 영상 다운로드
			InputStream objectData = amazonS3Client.getObject(bucketName, keyName)
				.getObjectContent();

			// FFmpegFrameGrabber 설정
			FFmpegFrameGrabber grabber = new FFmpegFrameGrabber(objectData);
			grabber.start();

			int frameRate = (int)grabber.getFrameRate();
			int frameNumber = 0;

			while (true) {
				frameNumber++;
				Frame frame = grabber.grabImage();
				if (frame == null) {
					break;
				}

				// 1초 간격으로 프레임 저장
				if (frameNumber % frameRate == 0) {
					BufferedImage bufferedImage = converter.getBufferedImage(frame);
					File outputfile = new File("frame-" + (frameNumber / frameRate) + ".png");
					ImageIO.write(bufferedImage, "png", outputfile);
				}
			}
			grabber.stop();
		} catch (Exception e) {

		}

	}
}
