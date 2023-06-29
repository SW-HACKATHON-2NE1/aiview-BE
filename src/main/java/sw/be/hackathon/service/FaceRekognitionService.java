package sw.be.hackathon.service;

import java.util.List;

import org.opencv.video.Video;
import org.springframework.stereotype.Service;

import com.amazonaws.services.rekognition.AmazonRekognition;
import com.amazonaws.services.rekognition.AmazonRekognitionClientBuilder;
import com.amazonaws.services.rekognition.model.AgeRange;
import com.amazonaws.services.rekognition.model.AmazonRekognitionException;
import com.amazonaws.services.rekognition.model.Attribute;
import com.amazonaws.services.rekognition.model.DetectFacesRequest;
import com.amazonaws.services.rekognition.model.DetectFacesResult;
import com.amazonaws.services.rekognition.model.Emotion;
import com.amazonaws.services.rekognition.model.EyeDirection;
import com.amazonaws.services.rekognition.model.FaceDetail;
import com.amazonaws.services.rekognition.model.Image;
import com.amazonaws.services.rekognition.model.S3Object;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FaceRekognitionService {

	private final VideoFrameExtractor videoFrameExtractor;

	public void investigateFace(String s3Bucket, String s3Key) {

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

				// ObjectMapper objectMapper = new ObjectMapper();
				// System.out.println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(face));
			}

		} catch (AmazonRekognitionException e) {
			e.printStackTrace();
		}

	}
}
