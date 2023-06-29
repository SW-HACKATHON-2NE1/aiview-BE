package sw.be.hackathon.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sw.be.hackathon.domain.Cycle;
import sw.be.hackathon.domain.Member;
import sw.be.hackathon.domain.QuestionAndAnswer;
import sw.be.hackathon.dto.ReportOneDto;
import sw.be.hackathon.repository.QuestionAndAnswerRepository;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class ReportService {
    private final QuestionAndAnswerRepository questionAndAnswerRepository;


    public List<ReportOneDto> getReport(Member member, Cycle cycle) {
        List<QuestionAndAnswer> qaList = questionAndAnswerRepository.findByCycle(cycle);
        List<ReportOneDto> response = new ArrayList<>();
        for(QuestionAndAnswer qa : qaList){
            ReportOneDto reportDto = ReportOneDto.builder()
                    .question(qa.getQuestion().getContent())
                    .transcription(qa.getTranscription())
                    .score(qa.getScore())
                    .feedback(qa.getFeedback())
                    .bestAnswer(qa.getBestAnswer())
                    .questionId(qa.getQuestion().getId())
                    .pronunciationScore(qa.getPronunciationScore())
                    .url(qa.getUrl())
                    .scoreDS(member.getScoreDS())
                    .scoreAL(member.getScoreAL())
                    .scoreNT(member.getScoreNT())
                    .scoreOS(member.getScoreOS())
                    .scoreDB(member.getScoreDB())
                    .scoreIS(member.getScoreIS())
                    .build();

            response.add(reportDto);
        }

        return response;

    }
}
