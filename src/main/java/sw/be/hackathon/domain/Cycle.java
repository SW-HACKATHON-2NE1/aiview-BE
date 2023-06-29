package sw.be.hackathon.domain;

import javax.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Cycle {

	@Id
	@GeneratedValue
	@Column(name = "CYCLE_ID")
	private Long id;

	@OneToMany(mappedBy = "cycle", cascade = CascadeType.ALL, orphanRemoval = true)
	@Builder.Default
	private List<QuestionAndAnswer> questionAndAnswers = new ArrayList<>();


	private Double sad;
	private Double confused;
	private Double disgusted;
	private Double angry;
	private Double surprised;
	private Double fear;
	private Double calm;
	private Double happy;
}
