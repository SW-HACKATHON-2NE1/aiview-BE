package sw.be.hackathon.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sw.be.hackathon.domain.Cycle;
import sw.be.hackathon.domain.QuestionAndAnswer;
import sw.be.hackathon.domain.Member;
import sw.be.hackathon.domain.Question;

import java.util.List;
import java.util.Optional;

public interface QuestionAndAnswerRepository extends JpaRepository<QuestionAndAnswer, Long> {
    Optional<QuestionAndAnswer> findByMemberAndQuestion(Member member, Question question);
    List<QuestionAndAnswer> findByCycle(Cycle cycle);
}
