package sw.be.hackathon.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import sw.be.hackathon.domain.SubjectCode;
import sw.be.hackathon.dto.QuestionRandomResponseDto;
import sw.be.hackathon.service.MemberService;
import sw.be.hackathon.service.QuestionService;

@Api(tags = "Question")
@RestController
@RequiredArgsConstructor
public class QuestionController {
    private final QuestionService questionService;
    private final MemberService memberService;

    @ApiOperation(value = "과목에 따라 질문 하나 가져오기", notes = "과목코드: DS-자료구조, AL-알고리즘, NT-네트워크, OS-운영체제, DB-데이터베이스, IS-정보보호")
    @GetMapping("/question/{subjectCode}")
    public ResponseEntity getQuestion(
            @RequestHeader("Authorization") String token,
            @PathVariable String subjectCode
    ){
        SubjectCode code = SubjectCode.valueOf(subjectCode.toUpperCase());
        QuestionRandomResponseDto questionDto = questionService.getQuestionRandom(code);

        return new ResponseEntity(questionDto, HttpStatus.OK);

    }
}
