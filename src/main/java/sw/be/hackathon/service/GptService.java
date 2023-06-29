package sw.be.hackathon.service;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import sw.be.hackathon.dto.GptMessageDto;
import sw.be.hackathon.dto.GptRequestDto;
import sw.be.hackathon.dto.GptResponseDto;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class GptService {
    @Value("${gpt.token}")
    String GPT_TOKEN;

    ObjectMapper objectMapper = new ObjectMapper();
    private final RestTemplate restTemplate;


    public GptResponseDto requestGpt() {
        List<GptMessageDto> messages = List.of(
                GptMessageDto.builder()
                        .role("system")
                        .content("당신은 수업을 평가하는 AI입니다.")
                        .build(),
                GptMessageDto.builder()
                        .role("user")
                        .content(" 아래의 내용을 정보의 정확도와 전달력을 기준으로 0~100사이의 점수에 대해 측정하고, 이 내용에서 좋았던 점과 개선할 점을 여러개 분석해줘. 아래의 형식으로 데이터를 JSON 으로 만들어줘. 설명하지마. 마크다운으로 주지 마. 오로지 JSON 형태로만 줘. 내용을 정확하게 검증하여 잘못된 정보가 들어오거나 공부에 대한 내용이 아닐 경우 점수를 0으로 설정하고 strength은 공백으로 두고 weakness는 왜 0점이 되었는지 써줘.\n" +
                                "                        {\"score\":\"/* 0~100사이의 강의에 대한 점수 정확도를 기준으로 측정*/\",\"strength\":\"/* 200자 내외의 내용의 정확도와 전달력을 기준으로 좋았던 점*/\",\"weakness\":\"/* 200자 내외의 정확도와 전달력을 기준으로 추가해야 할 내용*/\"}\n" +
                                "                        \n" +
                                "                        강의 내용: TCP는 비연결형 프로토콜로써, 인터넷상에서 서로 정보를 주고받을 때 정보를 보낸다는 신호나 받는다는 신호 절차를 거치지 않고 보내는 쪽에서 일방적으로 데이터를 전달하는 통신 프로토콜입니다. UDP와는 다르게 연결 설정이 없으며, 혼잡 제어를 하지 않기 때문에 UDP보다 전송 속도가 빠릅니다. 그러나 데이터 전송에 대한 보장을 하지 않기 때문에 패킷 손실이 발생할 수 있습니다. UDP는 신뢰성 있는 데이터 전송을 지원하는 연결 지향형 프로토콜입니다. 일반적으로 UDP와 IP가 함께 사용되는데, IP가 데이터의 전송을 처리한다면 UDP는 패킷 추적 및 관리를 하게 됩니다. 연결 지향형인 UDP는 3-way handshaking이라는 과정을 통해 연결 후 통신을 시작하는데, 흐름 제어와 혼잡 제어를 지원하며 데이터의 순서를 보장합니다. ")
                        .build(),
                GptMessageDto.builder()
                        .role("assistant")
                        .content("{\"score\": 10,\"strength\": \"\",\"weakness\": \"TCP와 UDP에 대한 설명이 잘못되어 있습니다. TCP는 연결형 프로토콜로써 데이터의 전송을 보장하며, 흐름 제어와 혼잡 제어를 통해 데이터의 안정적인 전송을 보장합니다. 반면 UDP는 비연결형 프로토콜로서 전송 속도가 빠르지만 데이터의 전송을 보장하지 않습니다. 또한 UDP가 연결 지향형 프로토콜이라고 설명한 부분도 잘못되었습니다. 이러한 틀린 정보로 인해 전달력과 정확도가 낮아졌습니다.\"}")
                        .build(),
                GptMessageDto.builder()
                        .role("user")
                        .content("위와 같이 평가해줘. 강의 내용")
                        .build()
        );

        GptRequestDto request = GptRequestDto.builder()
                .model("gpt-3.5-turbo")
                .temperature(1.15)
                .messages(messages)
                .build();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        try {

            String url = "https://api.openai.com/v1/chat/completions";
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " + GPT_TOKEN);
            headers.set("Content-Type", "application/json");
            HttpEntity<GptRequestDto> req = new HttpEntity<>(request, headers);

            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, req, String.class);
            System.out.println(response.getBody());
            return objectMapper.readValue(response.getBody(), GptResponseDto.class);

        }catch (Exception e){
            e.printStackTrace();
            throw new RuntimeException();
        }
    }
}
