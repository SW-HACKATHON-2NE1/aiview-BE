package sw.be.hackathon.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FaceRequestDto {
    private String userUuid;
    private String s3Bucket;
    private String s3Key;
}
