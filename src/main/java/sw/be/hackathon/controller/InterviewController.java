package sw.be.hackathon.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sw.be.hackathon.domain.Cycle;
import sw.be.hackathon.domain.QuestionAndAnswer;
import sw.be.hackathon.domain.Member;
import sw.be.hackathon.domain.Question;
import sw.be.hackathon.dto.*;
import sw.be.hackathon.dto.transcription.TranscriptionResponseDTO;
import sw.be.hackathon.service.*;

import java.util.List;

@Api(tags = "Interview Evaluation")
@RestController
@RequiredArgsConstructor
public class InterviewController {
    private final InterviewService interviewService;
    private final MemberService memberService;
    private final QuestionService questionService;
    private final TranscriptionService transcriptionService;
    private final AmazonS3Service amazonS3Service;
    private final GptService gptService;
    private final CycleService cycleService;
    private final ReportService reportService;
    private final FaceService faceService;

    @ApiOperation(value = "영상에서 텍스트 추출", notes = "AWS Transcribe API를 이용하여 영상에서 텍스트 추출 작업 실행. OK만 주고 body 없음")
    @PostMapping("/transcription/{questionId}")
    public ResponseEntity createTranscription(
            @RequestHeader(name = "Authorization") String token,
            @PathVariable Long questionId
    ){
        Member member = memberService.findByUUID(token);
        Question question = questionService.findById(questionId);
        QuestionAndAnswer questionAndAnswer = interviewService.findByMemberAndQuestion(member, question);

        if(questionAndAnswer != null){
            interviewService.remove(questionAndAnswer);
        }


        TranscriptionResponseDTO transcriptionResponse = transcriptionService.extractSpeechTextFromVideo(member, questionId);
        interviewService.saveTranscription(transcriptionResponse, interviewService.saveNewQuestionAndAnswer(member, question));

        return new ResponseEntity(HttpStatus.OK);
    }

    @ApiOperation(value = "답변 영상 업로드용 URL 발급 (업로드!!)", notes = "presigned-url을 통해 영상을 업로드 하고 성공(OK)를 받았으면 업로드 성공 \n {\n" +
            "  \"uploadUrl\": \"https://ainterview-video.s3.ap-northeast-2.amazonaws.com/f3ad5850-de47-49a5-906b-47e662ce8f42/1?X-Amz-Algorithm=AWS4-HMAC-SHA256&X-Amz-Date=20230629T023435Z&X-Amz-SignedHeaders=host&X-Amz-Expires=1799&X-Amz-Credential=AKIAQRKQNEOUET4FUFHP%2F20230629%2Fap-northeast-2%2Fs3%2Faws4_request&X-Amz-Signature=a8de243c5d7fdffde50cd41be31dab17a8e32e80bf9217d686a6df99a3b05836\"\n" +
            "}")
    @GetMapping("/presigned-url/upload/{questionId}")
    public ResponseEntity getPresignedUrlForUpload(
            @RequestHeader(name = "Authorization") String token,
            @PathVariable Long questionId
    ){
        Member member = memberService.findByUUID(token);
        String preSignedUrl = amazonS3Service.getPresignedUrlForUpload(member, questionId);

        return new ResponseEntity(new S3UploadUrlDto(preSignedUrl), HttpStatus.OK);
    }

    @ApiOperation(value = "답변 영상의 URL 발급 (브라우저에 띄우는!!)", notes = "video 태그의 src 값으로 들어갈 영상 url 발급" +
            "{\n" +
            "  \"uploadUrl\": \"https://ainterview-video.s3.ap-northeast-2.amazonaws.com/f3ad5850-de47-49a5-906b-47e662ce8f42/1?X-Amz-Algorithm=AWS4-HMAC-SHA256&X-Amz-Date=20230629T023435Z&X-Amz-SignedHeaders=host&X-Amz-Expires=1799&X-Amz-Credential=AKIAQRKQNEOUET4FUFHP%2F20230629%2Fap-northeast-2%2Fs3%2Faws4_request&X-Amz-Signature=a8de243c5d7fdffde50cd41be31dab17a8e32e80bf9217d686a6df99a3b05836\"\n" +
            "}")

    @GetMapping("/presigned-url/{questionId}")
    public ResponseEntity getPresignedUrl(
            @RequestHeader(name = "Authorization") String token,
            @PathVariable Long questionId
    ){
        Member member = memberService.findByUUID(token);
        String preSignedUrl = amazonS3Service.getPresignedUrl(member, questionId);

        return new ResponseEntity(new S3UploadUrlDto(preSignedUrl), HttpStatus.OK);
    }

    @ApiOperation(value = "답변 결과 데이터 응답", notes = "{\n" +
            "  \"pronunciationScore\": 0.888,\n" +
            "  \"transcription\": \"페이징은 메모리를 동일한 크기의 페이지로 분할하여 물리 메모리의 프레임에 매핑하는 메모리 관리 기법입니다. 페이지 테이블을 사용하여 가상 주소를 물리 주소로 변환하며, 이를 통해 메모리 단편화를 줄이고, 가상 메모리 사용을 가능하게 합니다. 이 기법은 메모리를 효율적으로 사용하고 보호하는 데 도움이 되지만, 관리 오버헤드가 발생할 수 있습니다.\",\n" +
            "  \"url\": https://~~~~,\n" +
            "  \"questionId\": 1\n" +
            "}")
    @GetMapping("/interview/{questionId}")
    public ResponseEntity getResultOfInterview(
            @RequestHeader(name = "Authorization") String token,
            @PathVariable Long questionId
    ){
        Member member = memberService.findByUUID(token);
        Question question = questionService.findById(questionId);
        QuestionAndAnswer questionAndAnswer = interviewService.findByMemberAndQuestion(member, question);
        InterviewResponseDto responseDto = interviewService.getResultOfInterview(questionAndAnswer);

        return new ResponseEntity(responseDto, HttpStatus.OK);
    }

    @ApiOperation(value = "GPT로부터, 점수/피드백/모범답안 얻어내어 DB에 저장", notes = "{\n" +
            "  \"questionId\": 1,\n" +
            "  \"score\": 80,\n" +
            "  \"feedback\": \"이 답변은 페이징에 대한 정의와 주요 개념을 다루고 있지만, 더 자세한 설명이 필요합니다. 페이징이 어떻게 동작하는지, 페이지 테이블을 어떻게 사용하여 가상 주소를 물리 주소로 변환하는지 등에 대해 더 자세하게 다루어야 합니다. 답변을 보완하여 페이징에 대한 전반적인 이해를 보여줄 수 있도록 하시기 바랍니다.\",\n" +
            "  \"bestAnswer\": \"페이징은 운영체제의 메모리 관리 방식 중 하나로, 메모리를 고정 크기의 페이지로 분할하여 사용합니다. 이렇게 분할된 페이지는 물리 메모리의 프레임에 매핑되어 실제 데이터를 저장합니다. 주소 변환은 페이지 테이블을 통해 이루어지며, 가상 주소를 물리 주소로 변환하여 실제 데이터에 접근합니다. 페이징은 메모리 단편화를 줄이고 가상 메모리를 사용하여 메모리 효율성을 높여줍니다. 단, 페이지 테이블을 관리하기 위한 오버헤드가 발생할 수 있습니다.\",\n" +
            "  \"tailQuestion\": \"페이지 테이블의 작동 방식에 대해 자세히 설명해주세요.\",\n" +
            "  \"tailQuestionId\": 3\n" +
            "}\n" +
            "\n" +
            "tailQuestionId를 가지고 다음 질문 요청하고 녹화하고 해주시면 됩니다.\n" +
            "이 API에서 응답한 body 데이터는 안 써도 돼요. 그냥 확인용")
    @PostMapping("/gpt/{questionId}")
    public ResponseEntity requestGPT(
            @RequestHeader(name = "Authorization") String token,
            @PathVariable Long questionId
    ){
        Member member = memberService.findByUUID(token);
        Question question = questionService.findById(questionId);
        QuestionAndAnswer questionAndAnswer = interviewService.findByMemberAndQuestion(member, question);
        GptEvaluationResponseDto evaluateDto = gptService.evaluate(questionAndAnswer);
        Cycle cycle = cycleService.findById(member.getCurrentCycle());
        faceService.evaluateFace(member, question, cycle);

        return new ResponseEntity(evaluateDto, HttpStatus.OK);
    }

    @ApiOperation(value = "결과 리포트 (한 싸이클이 끝나고)", notes = "한 싸이클이 끝나고 결과 리포트")
    @GetMapping("/report")
    public ResponseEntity getReport(
            @RequestHeader(name = "Authorization") String token
    ){
        Member member = memberService.findByUUID(token);
        Cycle cycle = cycleService.findById(member.getCurrentCycle());

        ReportDto report = reportService.getReport(member, cycle);

        return new ResponseEntity(report, HttpStatus.OK);
    }
}
