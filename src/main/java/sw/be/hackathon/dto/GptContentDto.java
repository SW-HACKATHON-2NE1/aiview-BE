package sw.be.hackathon.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GptContentDto {
    Integer score;
    String feedback;
    String bestAnswer;
    String tailQuestion;

}
