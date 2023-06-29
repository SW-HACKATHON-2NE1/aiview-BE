package sw.be.hackathon.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sw.be.hackathon.domain.Question;
import sw.be.hackathon.domain.SubjectCode;
import sw.be.hackathon.dto.QuestionRandomResponseDto;
import sw.be.hackathon.repository.QuestionRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class QuestionService {
    private final QuestionRepository questionRepository;

    public Question findById(Long id){
        return questionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException());
    }

    public QuestionRandomResponseDto getQuestionRandom(SubjectCode code) {
        Question question = questionRepository.findByCode(code.getName());

        return QuestionRandomResponseDto.builder()
                .questionId(question.getId())
                .content(question.getContent())
                .subjectCode(question.getSubjectCode().getName())
                .build();
    }
}
