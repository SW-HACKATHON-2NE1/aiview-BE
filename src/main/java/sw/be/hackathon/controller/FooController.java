package sw.be.hackathon.controller;

import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "foo")
@RestController
public class FooController {
    @GetMapping("/")
    public String foo(){
        return "Software 중심대학 공동 해커톤";
    }
}
