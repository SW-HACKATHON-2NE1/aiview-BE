package sw.be.hackathon.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class GptChoiceDto {
    GptMessageDto message;
    String finish_reason;
    int index;
}
