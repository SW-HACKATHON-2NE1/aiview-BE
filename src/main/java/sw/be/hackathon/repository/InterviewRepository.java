package sw.be.hackathon.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sw.be.hackathon.domain.Interview;
import sw.be.hackathon.domain.Member;
import sw.be.hackathon.domain.Question;

import java.util.Optional;

public interface InterviewRepository extends JpaRepository<Interview, Long> {
    Optional<Interview> findByMemberAndQuestion(Member member, Question question);
}
