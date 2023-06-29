package sw.be.hackathon.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import lombok.Data;

@Data
@Entity
public class Interview {

	@Id
	@GeneratedValue
	private Long id;


	private Double sad;
	private Double confused;
	private Double disgusted;
	private Double angry;
	private Double surprised;
	private Double fear;
	private Double calm;
	private Double happy;
}
