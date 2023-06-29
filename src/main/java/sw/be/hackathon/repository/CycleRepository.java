package sw.be.hackathon.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sw.be.hackathon.domain.Cycle;

public interface CycleRepository extends JpaRepository<Cycle, Long> {
}
