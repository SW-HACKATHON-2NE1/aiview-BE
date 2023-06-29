package sw.be.hackathon.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sw.be.hackathon.domain.Member;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByUuid(String uuid);
}
