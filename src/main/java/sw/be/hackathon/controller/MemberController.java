package sw.be.hackathon.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import sw.be.hackathon.domain.Member;
import sw.be.hackathon.dto.MemberUUIDDto;
import sw.be.hackathon.service.MemberService;

@Api(tags = "Member")
@RequiredArgsConstructor
@RestController
public class MemberController {
    private final MemberService memberService;

    @ApiOperation(value = "처음 접속 시 토큰 받기", notes = "헤더에 토큰이 있으면 OK만 리턴, 헤더가 비어있으면 회원가입 진행 후 토큰 리턴" +
            "{\n" +
            "  \"token\": \"efcf2ca0-d19d-459f-b833-8ab76533b524\"\n" +
            "}")
    @GetMapping("/")
    public ResponseEntity provideToken(
            @RequestHeader(value = "Authorization", required = false) String token
    ){
        if(token == null){
            Member member = memberService.signUp();
            return new ResponseEntity<>(new MemberUUIDDto(member.getUuid()), HttpStatus.OK);
        }

        return new ResponseEntity(HttpStatus.OK);
    }
}
