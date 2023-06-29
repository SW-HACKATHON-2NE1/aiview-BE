package sw.be.hackathon.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.json.JsonParseException;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import sw.be.hackathon.domain.Cycle;
import sw.be.hackathon.domain.Member;
import sw.be.hackathon.domain.Question;
import sw.be.hackathon.dto.FaceReqResponseDto;
import sw.be.hackathon.dto.FaceRequestDto;
import sw.be.hackathon.repository.CycleRepository;

@Service
@Transactional
@RequiredArgsConstructor
public class FaceService {
    private final CycleRepository cycleRepository;
    private final RestTemplate restTemplate;
    ObjectMapper objectMapper = new ObjectMapper();
    private String S3_BUCKET = "ainterview-video";
    private String REKOGNITION_REQUEST_URL = "http://54.180.14.177:3000";

    public void evaluateFace(Member member, Question question, Cycle cycle) {
        FaceRequestDto requestDto = FaceRequestDto.builder()
                .userUuid(member.getUuid())
                .s3Bucket(S3_BUCKET)
                .s3Key(member.getUuid() + "/" + question.getId())
                .build();

        HttpEntity<FaceRequestDto> req = new HttpEntity<>(requestDto);
        ResponseEntity<String> response = restTemplate.exchange(REKOGNITION_REQUEST_URL, HttpMethod.POST, req, String.class);
        String responseBody = response.getBody();
        try{
            FaceReqResponseDto responseDto = objectMapper.readValue(responseBody, FaceReqResponseDto.class);
            if(responseDto.getStatusCode() != 200){
                throw new RuntimeException();
            }
        }catch (Exception e){
            throw new JsonParseException();
        }

    }
}
