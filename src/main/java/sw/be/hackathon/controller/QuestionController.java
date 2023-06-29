package sw.be.hackathon.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import sw.be.hackathon.domain.Cycle;
import sw.be.hackathon.domain.Member;
import sw.be.hackathon.domain.Question;
import sw.be.hackathon.domain.SubjectCode;
import sw.be.hackathon.dto.QuestionResponseDto;
import sw.be.hackathon.service.CycleService;
import sw.be.hackathon.service.MemberService;
import sw.be.hackathon.service.QuestionService;

@Api(tags = "Question")
@RestController
@RequiredArgsConstructor
public class QuestionController {
    private final QuestionService questionService;
    private final MemberService memberService;
    private final CycleService cycleService;

    @ApiOperation(value = "과목에 따라 질문 하나 랜덤으로 가져오기 (싸이클의 첫번째 문제)", notes = "과목코드: DS-자료구조, AL-알고리즘, NT-네트워크, OS-운영체제, DB-데이터베이스, IS-정보보호")
    @GetMapping("/question/first/{subjectCode}")
    public ResponseEntity getFirstQuestion(
            @RequestHeader("Authorization") String token,
            @PathVariable String subjectCode
    ){
        Member member = memberService.findByUUID(token);
        SubjectCode code = SubjectCode.valueOf(subjectCode.toUpperCase());
        QuestionResponseDto questionDto = questionService.getQuestionRandom(code);
        Cycle cycle = cycleService.getNewCycle();
        memberService.setCurrentCycle(member, cycle);

        return new ResponseEntity(questionDto, HttpStatus.OK);
    }
    
    @ApiOperation(value = "꼬리질문 ID로 질문 꼬리질문 정보 요청", notes = "")
    @GetMapping("/question/{questionId}")
    public ResponseEntity getQuestion(
            @RequestHeader("Authorization") String token,
            @PathVariable Long questionId
    ){
        Member member = memberService.findByUUID(token);
        Question question = questionService.findById(questionId);

        QuestionResponseDto questionDto = questionService.getQuestionDto(question);

        return new ResponseEntity(questionDto, HttpStatus.OK);
    }
}
