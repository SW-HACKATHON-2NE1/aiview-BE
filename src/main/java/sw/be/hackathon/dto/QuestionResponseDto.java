package sw.be.hackathon.dto;

import lombok.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Setter
public class QuestionResponseDto {
    private Long questionId;
    private String content;
    private String subjectCode;
}
