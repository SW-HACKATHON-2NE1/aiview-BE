package sw.be.hackathon.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponses;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import sw.be.hackathon.domain.Interview;
import sw.be.hackathon.domain.Member;
import sw.be.hackathon.domain.Question;
import sw.be.hackathon.dto.transcription.TranscriptionResponseDTO;
import sw.be.hackathon.repository.InterviewRepository;
import sw.be.hackathon.service.InterviewService;
import sw.be.hackathon.service.MemberService;
import sw.be.hackathon.service.QuestionService;
import sw.be.hackathon.service.TranscriptionService;

import static io.swagger.v3.oas.annotations.enums.ParameterIn.HEADER;

@Api(tags = "STT")
@RestController
@RequiredArgsConstructor
public class InterviewController {
    private final InterviewService interviewService;
    private final MemberService memberService;
    private final QuestionService questionService;
    private final TranscriptionService transcriptionService;

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
        interviewService.saveTranscription(transcriptionResponse, interview);

        return ApiResponse.of(TranscribeResponseType.TRANSCRIBE_OK, transcriptionResponse);
    }
}
