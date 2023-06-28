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


    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    private List<Interview> interviews;

    @PrePersist
    public void setUuid(){
        this.uuid = UUID.randomUUID().toString();
    }

}
