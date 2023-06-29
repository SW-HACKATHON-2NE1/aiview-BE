package sw.be.hackathon.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QuestionRandomResponseDto {
    private Long questionId;
    private String content;
    private String subjectCode;
}
