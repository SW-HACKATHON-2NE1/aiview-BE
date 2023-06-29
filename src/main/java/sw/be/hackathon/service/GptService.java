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
import sw.be.hackathon.domain.QuestionAndAnswer;
import sw.be.hackathon.domain.Question;
import sw.be.hackathon.dto.*;
import sw.be.hackathon.repository.QuestionRepository;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class GptService {
    private final QuestionRepository questionRepository;
    @Value("${gpt.token}")
    String GPT_TOKEN;

    ObjectMapper objectMapper = new ObjectMapper();
    private final RestTemplate restTemplate;


    public GptResponseDto getResponseFromGpt(String question, String answer) {
        List<GptMessageRequestDto> messages = List.of(
                GptMessageRequestDto.builder()
                        .role("system")
                        .content("당신은 백엔드 개발자 채용을 위한 직무 면접관 역할입니다. 질문과 답에 대해 기술적으로 정확한 채점을 해야 하며, 틀린 답변과 부족한 답변에 대해서는 정확한 피드백이 있어야 합니다.")
                        .build(),
                GptMessageRequestDto.builder()
                        .role("user")
                        .content("지금 너가 채용해야 하는 직무는 백엔드 개발자이고, 해당 직무에 대한 기술 면접이기 때문에 네트워크 과목에 대해서 질문을 할꺼야. 너가 할 질문은 다음과 같아\n" +
                                "\n" +
                                "직무 면접 질문: TCP, UDP에 대해서 아는대로 설명해보세요.\n" +
                                "\n" +
                                "위의 기술 면접 질문에 대해서 아래 답변을 질문과 관련하여 맞는 질문인지 정확도와 전달력을 기준으로 0~100 사이의 점수에 대해 측정해줘. 답변의 길이는 한국말 기준 1분 이내라는 것을 명심해, 답변 내용의 적절성을 위주로 확인하고, 내용이 좀 부족한 것에 대해서는 넘어가도 좋아. 이 내용에서 좋았던 점과 개선할 점을 분석해줘. 질문에 대해서 너가 생각하는 모범답안을 알려주고, 이전 질문과 그 답변에 대해서 좀  더 심화적으로 물어볼 수 있는 질문을 1개 알려줘.  \n" +
                                "\n" +
                                "아래의 형식으로 데이터를 JSON 으로 만들어줘. 설명하지말고, 마크다운으로 주지 말고. 오로지 JSON 형태로만 줘. 내용을 정확하게 검증하여 잘못된 정보가 들어오거나 답변에 대한 내용이 아닐 경우 점수를 0으로 설정하고 strength는 공백으로 두고 weakness는 왜 0점이 되었는지 써줘.\n" +
                                "{\"score\":\"/* 0~100사이의 답변에 대한 점수. 정확도를 기준으로 측정함.*/\", \"feedback\":\"/* 200자가 넘지 않게 답변에 대한 피드백을 해줘. 만약 피드백할 내용이 없으면 대체적으로 잘 답변했다고 하면 될 것 같아. */\", “bestAnswer\":\"/* 200자가 넘지 않게 너가 생각하는 질문에 대한 모범 답안을 작성해줘. */\", “tailQuestion\":\"/* 이전 질문과  사용자의 답변을 참고하여 주제에 대해 심화적으로 더 물어볼 수 있는 질문 */\"}\n" +
                                "\n" +
                                "답변 내용: TCP는 데이터의 순서와 신뢰성을 보장하는 연결 지향형 프로토콜입니다. 연결을 설정한 후 통신하며, 흐름 제어와 혼잡 제어를 지원합니다. 반면, UDP는 연결 없이 빠르게 데이터를 전송하는 비연결형 프로토콜이며, 데이터의 순서나 신뢰성을 보장하지 않습니다.")
                        .build(),
                GptMessageRequestDto.builder()
                        .role("assistant")
                        .content("{\"score\": 10, \"feedback\": \"이 답변에는 TCP와 UDP에 대한 설명이 혼동되고 잘못 표현되어 있습니다. 실제로 TCP는 연결 지향형 프로토콜로, 데이터 전송 전에 3-way handshake를 통해 연결을 설정합니다. TCP는 데이터의 순서 보장과 신뢰성을 제공합니다. 반대로, UDP는 비연결형 프로토콜로, 연결 설정 없이 데이터를 전송합니다. UDP는 빠른 전송을 제공하지만, 신뢰성이나 순서 보장은 하지 않습니다. 답변을 수정하여 TCP와 UDP의 특징을 정확하게 반영하는 것이 중요합니다.\", \"bestAnswer\":\"TCP(Transmission Control Protocol)는 연결 지향형 프로토콜로, 통신을 시작하기 전에 두 시스템 간에 연결을 설정합니다. 이는 3-way handshake 과정을 통해 이루어집니다. TCP는 데이터의 순서 보장, 신뢰성, 혼잡 제어 및 흐름 제어를 제공합니다. 이로 인해 높은 신뢰성이 필요한 애플리케이션에서 주로 사용됩니다. 반면, UDP(User Datagram Protocol)는 비연결형 프로토콜로, 데이터를 전송하기 전에 연결을 설정하지 않습니다. UDP는 낮은 지연 시간과 빠른 전송을 제공하지만, 데이터의 순서 보장이나 신뢰성은 제공하지 않습니다. 이로 인해 실시간 스트리밍, 게임, VoIP 등 빠른 데이터 전송이 중요한 경우에 적합합니다.\", \"tailQuestion\":\"TCP의 혼잡 제어(congestion control) 메커니즘이란 무엇이며, 이를 통해 네트워크 성능에 어떤 영향을 미치는지 설명할 수 있나요?\"}")
                        .build(),
                GptMessageRequestDto.builder()
                        .role("user")
                        .content("위와 같이 답변해줘.\n" +
                                "\n" +
                                "직무 면접 질문 : " + question + "\n" +
                                "답변 내용: " + answer)
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
            String responseBody = response.getBody().replace("\\", "");
            responseBody = responseBody.replace("\\", "");
            responseBody = responseBody.replace("\"{", "{");
            responseBody = responseBody.replace("}\"", "}");
            System.out.println(responseBody);
            return objectMapper.readValue(responseBody, GptResponseDto.class);

        }catch (Exception e){
            e.printStackTrace();
            throw new RuntimeException();
        }
    }

    public GptEvaluationResponseDto evaluate(QuestionAndAnswer questionAndAnswer) {
        GptResponseDto responseDto = getResponseFromGpt(questionAndAnswer.getQuestion().getContent(), questionAndAnswer.getTranscription());
        GptContentDto content = responseDto.getChoices().get(0).getMessage().getContent();
        Integer score = content.getScore();
        String bestAnswer = content.getBestAnswer();
        String feedback = content.getFeedback();
        String tailQuestion = content.getTailQuestion();

        questionAndAnswer.setScore(score);
        questionAndAnswer.setBestAnswer(bestAnswer);
        questionAndAnswer.setFeedback(feedback);
        questionAndAnswer.setTailQuestion(tailQuestion);

        Question question = Question.builder()
                .subjectCode(questionAndAnswer.getQuestion().getSubjectCode())
                .content(tailQuestion)
                .build();
        questionRepository.save(question);

        GptEvaluationResponseDto evalDto = GptEvaluationResponseDto.builder()
                .questionId(questionAndAnswer.getQuestion().getId())
                .score(score)
                .feedback(feedback)
                .bestAnswer(bestAnswer)
                .tailQuestion(tailQuestion)
                .tailQuestionId(question.getId())
                .build();

        return evalDto;
    }
}
