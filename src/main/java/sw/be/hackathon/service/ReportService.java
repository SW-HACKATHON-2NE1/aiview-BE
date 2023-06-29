package sw.be.hackathon.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sw.be.hackathon.domain.Cycle;
import sw.be.hackathon.domain.Member;
import sw.be.hackathon.domain.QuestionAndAnswer;
import sw.be.hackathon.dto.ReportDto;
import sw.be.hackathon.dto.ReportOneDto;
import sw.be.hackathon.repository.QuestionAndAnswerRepository;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class ReportService {
    private final QuestionAndAnswerRepository questionAndAnswerRepository;


    public ReportDto getReport(Member member, Cycle cycle) {
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
                    .build();

            response.add(reportDto);
        }

        Integer scoreDS = 0;
        Integer scoreAL = 0;
        Integer scoreNT = 0;
        Integer scoreOS = 0;
        Integer scoreDB = 0;
        Integer scoreIS = 0;

        if(member.getCountDS() != 0) scoreDS = member.getScoreDS() / member.getCountDS();
        if(member.getCountAL() != 0) scoreAL = member.getScoreAL() / member.getCountAL();
        if(member.getCountNT() != 0) scoreNT = member.getScoreNT() / member.getCountNT();
        if(member.getCountOS() != 0) scoreOS = member.getScoreOS() / member.getCountOS();
        if(member.getCountDB() != 0) scoreDB = member.getScoreDB() / member.getCountDB();
        if(member.getCountIS() != 0) scoreIS = member.getScoreIS() / member.getCountIS();


        ReportDto reportDto = ReportDto.builder()
                .reports(response)
                .angry(cycle.getAngry())
                .sad(cycle.getSad())
                .happy(cycle.getHappy())
                .calm(cycle.getCalm())
                .confused(cycle.getConfused())
                .disgusted(cycle.getDisgusted())
                .fear(cycle.getFear())
                .surprised(cycle.getSurprised())
                .scoreDS(scoreDS)
                .scoreAL(scoreAL)
                .scoreNT(scoreNT)
                .scoreOS(scoreOS)
                .scoreDB(scoreDB)
                .scoreIS(scoreIS)
                .build();

        return reportDto;

    }
}
