package sw.be.hackathon.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReportDto {
    List<ReportOneDto> reports;

    Integer angry;
    Integer calm;
    Integer confused;
    Integer disgusted;
    Integer fear;
    Integer happy;
    Integer sad;
    Integer surprised;
}
