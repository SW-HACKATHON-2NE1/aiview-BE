package sw.be.hackathon.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GptEvaluationResponseDto {
    private Long questionId;
    private Integer score;
    private String feedback;
    private String bestAnswer;
    private String tailQuestion;
    private Long tailQuestionId;
}
