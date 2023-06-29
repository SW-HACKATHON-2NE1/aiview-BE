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


	@Builder.Default
	private Double sad=0d;
	@Builder.Default
	private Double confused=0d;
	@Builder.Default
	private Double disgusted=0d;
	@Builder.Default
	private Double angry=0d;
	@Builder.Default
	private Double surprised=0d;
	@Builder.Default
	private Double fear=0d;
	@Builder.Default
	private Double calm=0d;
	@Builder.Default
	private Double happy=0d;
}
