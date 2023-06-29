package sw.be.hackathon.domain;

import lombok.*;

import javax.persistence.*;
import java.util.List;
import java.util.UUID;


@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Member {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MEMBER_ID")
    private Long id;

    private String uuid;

    private Integer scoreDS;
    private Integer countDS;

    private Integer scoreAL;
    private Integer countAL;

    private Integer scoreNT;
    private Integer countNT;

    private Integer scoreOS;
    private Integer countOS;

    private Integer scoreDB;
    private Integer countDB;

    private Integer scoreIS;
    private Integer countIS;


    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    private List<Interview> interviews;

    @PrePersist
    public void init(){
        this.uuid = UUID.randomUUID().toString();
        this.scoreDS = 0;
        this.scoreAL = 0;
        this.scoreNT = 0;
        this.scoreOS = 0;
        this.scoreDB = 0;
        this.scoreIS = 0;

        this.countDS = 0;
        this.countAL = 0;
        this.countNT = 0;
        this.countOS = 0;
        this.countDB = 0;
        this.countIS = 0;
    }

}
