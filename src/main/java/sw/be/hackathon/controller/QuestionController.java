package sw.be.hackathon.controller;

import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;
import sw.be.hackathon.service.MemberService;
import sw.be.hackathon.service.QuestionService;

@Api(tags = "Question")
@RestController
@RequiredArgsConstructor
public class QuestionController {
    private final QuestionService questionService;
    private final MemberService memberService;
}
