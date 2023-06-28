package sw.be.hackathon.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sw.be.hackathon.domain.Interview;

public interface InterviewRepository extends JpaRepository<Interview, Long> {
}
