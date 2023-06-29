package sw.be.hackathon.domain;

import javax.persistence.*;

import lombok.Data;

import java.util.List;

@Data
@Entity
public class Interview {

	@Id
	@GeneratedValue
	private Long id;

	@OneToMany(mappedBy = "interview", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<QuestionAndAnswer> questionAndAnswers;


	private Double sad;
	private Double confused;
	private Double disgusted;
	private Double angry;
	private Double surprised;
	private Double fear;
	private Double calm;
	private Double happy;
}
