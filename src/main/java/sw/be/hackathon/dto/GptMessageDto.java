package sw.be.hackathon.dto;

import lombok.*;


@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class GptMessageDto{
    String role;
    String content;
}
