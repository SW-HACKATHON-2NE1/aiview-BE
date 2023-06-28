package sw.be.hackathon.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sw.be.hackathon.domain.Interview;
import sw.be.hackathon.domain.Member;
import sw.be.hackathon.domain.Question;
import sw.be.hackathon.dto.InterviewResponseDto;
import sw.be.hackathon.dto.S3UploadUrlDto;
import sw.be.hackathon.dto.transcription.TranscriptionResponseDTO;
import sw.be.hackathon.repository.InterviewRepository;
import sw.be.hackathon.service.*;

import static io.swagger.v3.oas.annotations.enums.ParameterIn.HEADER;

@Api(tags = "STT")
@RestController
@RequiredArgsConstructor
public class InterviewController {
    private final InterviewService interviewService;
    private final MemberService memberService;
    private final QuestionService questionService;
    private final TranscriptionService transcriptionService;
    private final AmazonS3Service amazonS3Service;

    @ApiOperation(value = "영상에서 텍스트 추출", notes = "AWS Transcribe API를 이용하여 영상에서 텍스트 추출 작업 실행")
    @GetMapping("/transcription")
    public ResponseEntity createTranscription(
            @RequestHeader(name = "Authorization") String token,
            @RequestParam Long questionId
    ){
        Member member = memberService.findByUUID(token);
        Question question = questionService.findById(questionId);
        Interview interview = interviewService.findByMemberAndQuestion(member, question);

        if(interview != null){
            interviewService.remove(interview);
        }


        TranscriptionResponseDTO transcriptionResponse = transcriptionService.extractSpeechTextFromVideo(member, questionId);
        interviewService.saveTranscription(transcriptionResponse, interviewService.saveNewInterview(member, question));

        return new ResponseEntity(HttpStatus.OK);
    }

    @ApiOperation(value = "답변 영상 업로드용 URL 발급 (업로드!!)", notes = "presigned-url을 통해 영상을 업로드 하고 성공(OK)를 받았으면 업로드 성공")
    @GetMapping("/presigned-url/upload/{questionId}")
    public ResponseEntity getPresignedUrlForUpload(
            @RequestHeader(name = "Authorization") String token,
            @PathVariable Long questionId
    ){
        Member member = memberService.findByUUID(token);
        String preSignedUrl = amazonS3Service.getPresignedUrlForUpload(member, questionId);

        return new ResponseEntity(new S3UploadUrlDto(preSignedUrl), HttpStatus.OK);
    }

    @ApiOperation(value = "답변 영상의 URL 발급 (브라우저에 띄우는!!)", notes = "video 태그의 src 값으로 들어갈 영상 url 발급")

    @GetMapping("/presigned-url/{questionId}")
    public ResponseEntity getPresignedUrl(
            @RequestHeader(name = "Authorization") String token,
            @PathVariable Long questionId
    ){
        Member member = memberService.findByUUID(token);
        String preSignedUrl = amazonS3Service.getPresignedUrl(member, questionId);

        return new ResponseEntity(new S3UploadUrlDto(preSignedUrl), HttpStatus.OK);
    }

    @ApiOperation(value = "답변 결과 데이터 응답", notes = "")
    @GetMapping("/interview/{questionId}")
    public ResponseEntity getResultOfInterview(
            @RequestHeader(name = "Authorization") String token,
            @PathVariable Long questionId
    ){
        Member member = memberService.findByUUID(token);
        Question question = questionService.findById(questionId);
        Interview interview = interviewService.findByMemberAndQuestion(member, question);
        InterviewResponseDto responseDto = interviewService.getResultOfInterview(interview);

        return new ResponseEntity(responseDto, HttpStatus.OK);
    }
}
