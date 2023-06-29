package sw.be.hackathon.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import sw.be.hackathon.domain.Question;
import sw.be.hackathon.domain.SubjectCode;

import java.util.List;

public interface QuestionRepository extends JpaRepository<Question, Long> {

    @Query(nativeQuery = true, value = "select * from question q where q.subject_code = :code order by RAND() limit 1")
    Question findByCode(@Param("code") SubjectCode code);
}
