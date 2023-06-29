package sw.be.hackathon.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sw.be.hackathon.domain.Cycle;
import sw.be.hackathon.domain.Member;
import sw.be.hackathon.repository.MemberRepository;

@Service
@RequiredArgsConstructor
@Transactional
public class MemberService {
    private final MemberRepository memberRepository;
    private final CycleService cycleService;

    public Member findByUUID(String uuid){
        return memberRepository.findByUuid(uuid)
                .orElseThrow(() -> new RuntimeException());
    }

    public Member signUp(){
        Member member = new Member();
        memberRepository.save(member);
        return member;
    }

    public void setCurrentCycle(Member member){
        Cycle cycle = cycleService.getNewCycle();
        member.setCurrentCycle(cycle.getId());
    }
}
