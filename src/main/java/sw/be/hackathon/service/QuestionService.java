package sw.be.hackathon.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sw.be.hackathon.domain.Question;
import sw.be.hackathon.repository.QuestionRepository;

@Service
@RequiredArgsConstructor
@Transactional
public class QuestionService {
    private final QuestionRepository questionRepository;

    public Question findById(Long id){
        return questionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException());
    }
}
