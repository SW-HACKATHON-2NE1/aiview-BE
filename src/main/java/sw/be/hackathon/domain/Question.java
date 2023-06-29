package sw.be.hackathon.domain;

import lombok.*;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Builder
public class Question {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "QUESTION_ID")
    private Long id;

    @Enumerated(EnumType.STRING)
    private SubjectCode subjectCode;
    private String content;

    // 새로 추가하는 꼬리질문들도 여기에 저장을 하되, status를 N으로 표기
    // 원래 우리가 직접 넣은 원시 질문들만 Y로 수기로 저장
    @Builder.Default
    @Enumerated(EnumType.STRING)
    private QuestionBaseStatus status = QuestionBaseStatus.N;

    @OneToMany(mappedBy = "question", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Interview> interviews;
}
