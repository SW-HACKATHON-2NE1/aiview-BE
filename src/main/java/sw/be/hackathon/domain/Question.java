package sw.be.hackathon.domain;

import lombok.*;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Setter
public class Question {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "QUESTION_ID")
    private Long id;

    @Enumerated(EnumType.STRING)
    private SubjectCode subjectCode;
    private String content;

    @OneToMany(mappedBy = "question", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Interview> interviews;
}
