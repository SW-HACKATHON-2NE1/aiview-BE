package sw.be.hackathon.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sw.be.hackathon.domain.Question;

public interface QuestionRepository extends JpaRepository<Question, Long> {
}
