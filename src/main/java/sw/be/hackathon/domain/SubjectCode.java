package sw.be.hackathon.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum SubjectCode {
    DS("DS"),
    AL("AL"),
    NT("NT"),
    OS("OS"),
    DB("DB"),
    IS("IS"),
    ;

    private final String name;
}
