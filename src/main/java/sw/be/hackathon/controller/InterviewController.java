package sw.be.hackathon.controller;

import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;
import sw.be.hackathon.repository.InterviewRepository;
import sw.be.hackathon.service.InterviewService;
import sw.be.hackathon.service.MemberService;

@Api(tags = "Interview")
@RestController
@RequiredArgsConstructor
public class InterviewController {
    private final InterviewService interviewService;
    private final MemberService memberService;
}
