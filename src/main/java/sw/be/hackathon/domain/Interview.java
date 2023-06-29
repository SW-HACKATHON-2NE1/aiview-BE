package sw.be.hackathon.domain;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Interview {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "INTERVIEW_ID")
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

    private Double sad;
    private Double confused;
    private Double disgusted;
    private Double angry;
    private Double surprised;
    private Double fear;
    private Double calm;
    private Double happy;

    private Integer score;
    @Column(length = 2000)
    private String feedback;
    @Column(length = 2000)
    private String bestAnswer;
    @Column(length = 2000)
    private String tailQuestion;
}
