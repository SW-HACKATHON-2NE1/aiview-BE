package sw.be.hackathon.service;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import javax.imageio.ImageIO;

import org.springframework.stereotype.Service;

import com.amazonaws.services.rekognition.AmazonRekognition;
import com.amazonaws.services.rekognition.AmazonRekognitionClientBuilder;
import com.amazonaws.services.rekognition.model.AmazonRekognitionException;
import com.amazonaws.services.rekognition.model.Attribute;
import com.amazonaws.services.rekognition.model.DetectFacesRequest;
import com.amazonaws.services.rekognition.model.DetectFacesResult;
import com.amazonaws.services.rekognition.model.Emotion;
import com.amazonaws.services.rekognition.model.EyeDirection;
import com.amazonaws.services.rekognition.model.FaceDetail;
import com.amazonaws.services.rekognition.model.Image;
import com.amazonaws.services.rekognition.model.S3Object;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FaceRekognitionService {

	private final VideoFrameExtractor videoFrameExtractor;
	private final AmazonS3Client amazonS3Client;

	public void investigateFace(String token, String s3Bucket, String s3Key) throws IOException {

		videoFrameExtractor.extractS3VideoToImage(s3Bucket, s3Key);


		//
		AmazonRekognition rekognitionClient = AmazonRekognitionClientBuilder.defaultClient();

		DetectFacesRequest request = new DetectFacesRequest()
			.withImage(new Image()
				.withS3Object(new S3Object()
					.withName(s3Key)
					.withBucket(s3Bucket)))
			.withAttributes(Attribute.EMOTIONS, Attribute.EYE_DIRECTION);

		try {
			DetectFacesResult result = rekognitionClient.detectFaces(request);
			List<FaceDetail> faceDetails = result.getFaceDetails();

			for (FaceDetail face: faceDetails) {

				Float confidence = face.getConfidence();
				List<Emotion> emotions = face.getEmotions();
				EyeDirection eyeDirection = face.getEyeDirection();

				System.out.println("confidence = " + confidence);
				System.out.println("emotions = " + emotions);
				System.out.println("eyeDirection = " + eyeDirection);

				ObjectMapper objectMapper = new ObjectMapper();
				System.out.println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(face));
			}

		} catch (AmazonRekognitionException e) {
			e.printStackTrace();
		}

	}


	private void uploadImageToS3(String s3Bucket, String s3Key, InputStream imageInputStream) {
		try {
			// 메타데이터 설정
			ObjectMetadata metadata = new ObjectMetadata();
			metadata.setContentType("image/jpeg"); // 이미지 형식에 따라 변경 가능

			// 이미지 업로드 요청 생성
			PutObjectRequest request = new PutObjectRequest(s3Bucket, s3Key, imageInputStream, metadata);

			// 이미지 업로드
			amazonS3Client.putObject(request);
			System.out.println("Image uploaded to S3 successfully.");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static InputStream convertBufferedImageToInputStream(BufferedImage image) {
		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ImageIO.write(image, "jpg", baos);
			return new ByteArrayInputStream(baos.toByteArray());
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
}
