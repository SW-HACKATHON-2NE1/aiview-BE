package sw.be.hackathon.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import sw.be.hackathon.domain.Interview;
import sw.be.hackathon.domain.Member;
import sw.be.hackathon.domain.Question;
import sw.be.hackathon.dto.transcription.TranscriptionResponseDTO;
import sw.be.hackathon.service.FaceRekognitionService;

@Api(tags = "FACE")
@RestController
@RequiredArgsConstructor
public class FaceInvestigateController {

	private final FaceRekognitionService faceRekognitionService;


	@ApiOperation(value = "얼굴 검사하기", notes = "AWS rekognition API를 이용하여 영상에서 얼굴 추출 작업 실행")
	@GetMapping("/face")
	public ResponseEntity<?> investigateFace(
		@RequestHeader(name = "Authorization") String token,
		@RequestParam String s3Bucket,
		@RequestParam String s3Key

	){
		faceRekognitionService.investigateFace(s3Bucket, s3Key);
		return ResponseEntity.ok(null);
	}
	
}
