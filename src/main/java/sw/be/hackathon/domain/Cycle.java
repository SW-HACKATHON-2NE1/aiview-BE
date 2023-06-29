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
	private Integer sad=0;
	@Builder.Default
	private Integer confused=0;
	@Builder.Default
	private Integer disgusted=0;
	@Builder.Default
	private Integer angry=0;
	@Builder.Default
	private Integer surprised=0;
	@Builder.Default
	private Integer fear=0;
	@Builder.Default
	private Integer calm=0;
	@Builder.Default
	private Integer happy=0;
}
