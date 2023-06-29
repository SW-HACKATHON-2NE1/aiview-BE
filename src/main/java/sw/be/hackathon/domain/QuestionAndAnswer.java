package sw.be.hackathon.domain;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QuestionAndAnswer {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "QUESTIONANDANSWER_ID")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MEMBER_ID")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "QUESTION_ID")
    private Question question;

    @Column(length = 2000)
    private String transcription;
    private Double pronunciationScore;
    private String url;


    @JoinColumn(name="INTERVIEW_ID")
    @ManyToOne(fetch = FetchType.LAZY)
    private Interview interview;

    private Integer score;
    @Column(length = 2000)
    private String feedback;
    @Column(length = 2000)
    private String bestAnswer;
    @Column(length = 2000)
    private String tailQuestion;
}
