package sw.be.hackathon.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sw.be.hackathon.domain.Cycle;
import sw.be.hackathon.repository.CycleRepository;

@Service
@Transactional
@RequiredArgsConstructor
public class CycleService {
    private final CycleRepository cycleRepository;

    public Cycle findById(Long id){
        return cycleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException());
    }

    public Cycle getNewCycle(){
        Cycle cycle = Cycle.builder().build();
        cycleRepository.save(cycle);
        return cycle;
    }
}
