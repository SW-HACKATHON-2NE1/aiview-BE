package sw.be.hackathon.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReportOneDto {
    String question;
    String transcription;
    Integer score;
    String feedback;
    String bestAnswer;
    Long questionId;
    Double pronunciationScore;
    String url;

}
