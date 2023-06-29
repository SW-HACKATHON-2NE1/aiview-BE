package sw.be.hackathon.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class InterviewResponseDto {
    private Double pronunciationScore;
    private String transcription;
    private String url;
    private Long questionId;
}
