package sw.be.hackathon.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum QuestionBaseStatus {
    Y("Y"),
    N("N"),
    ;

    private final String status;
}
