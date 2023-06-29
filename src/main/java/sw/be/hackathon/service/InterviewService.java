package sw.be.hackathon.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sw.be.hackathon.domain.Cycle;
import sw.be.hackathon.domain.QuestionAndAnswer;
import sw.be.hackathon.domain.Member;
import sw.be.hackathon.domain.Question;
import sw.be.hackathon.dto.InterviewResponseDto;
import sw.be.hackathon.dto.transcription.*;
import sw.be.hackathon.repository.CycleRepository;
import sw.be.hackathon.repository.QuestionAndAnswerRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class InterviewService {
    private final QuestionAndAnswerRepository questionAndAnswerRepository;
    private final CycleRepository cycleRepository;

    public QuestionAndAnswer findByMemberAndQuestion(Member member, Question question){
        return questionAndAnswerRepository.findByMemberAndQuestion(member, question)
                .orElse(null);
    }

    public void remove(QuestionAndAnswer questionAndAnswer) {
        questionAndAnswerRepository.delete(questionAndAnswer);
    }

    public QuestionAndAnswer saveNewQuestionAndAnswer(Member member, Question question){
        Cycle cycle = cycleRepository.findById(member.getCurrentCycle())
                .orElseThrow(()-> new RuntimeException());

        QuestionAndAnswer questionAndAnswer = QuestionAndAnswer.builder()
                .member(member)
                .question(question)
                .cycle(cycle)
                .build();
        questionAndAnswerRepository.save(questionAndAnswer);

        return questionAndAnswer;
    }

    public void saveTranscription(TranscriptionResponseDTO transcriptionResponse, QuestionAndAnswer questionAndAnswer) {
        TranscriptionResultDTO results = transcriptionResponse.getResults();
//        saveEntireTranscription(results, interview);
        saveConfidenceOfPronunciation(results.getItems(), questionAndAnswer);
    }

    private void saveConfidenceOfPronunciation(List<TranscriptionItemDTO> items, QuestionAndAnswer questionAndAnswer) {
        Boolean wrong = false;
        String sentence = "";
        Double confidence = 0.0;
        Integer dotCount = 0;
        for(TranscriptionItemDTO item : items){
            String content = "";
            for(TranscriptionItemAlternativesDTO alternativesDTO : item.getAlternatives()){
                Double tempConfidence = Double.parseDouble(alternativesDTO.getConfidence());
                if(tempConfidence <= 0.75 && !alternativesDTO.getContent().equals(".")){
                    wrong = true;
                }

                if(tempConfidence > 0.0) {
                    confidence += tempConfidence;
                }

                if(!wrong){
                    content += alternativesDTO.getContent();
                }
                else{
                    content += "%" + alternativesDTO.getContent() + "%";
                    wrong = false;
                }
            }
            sentence += content + " ";

            if(content.equals(".")){
                sentence = sentence.replace(" . ", ". ");
                dotCount++;
            }
        }
        confidence /= (items.size() - dotCount);
        confidence = Math.round(confidence * 10000) / 100.0;

        questionAndAnswer.setTranscription(sentence);
        questionAndAnswer.setPronunciationScore(confidence);
        String url = "https://ainterview-video.s3.ap-northeast-2.amazonaws.com/" + questionAndAnswer.getMember().getUuid() + "/" + questionAndAnswer.getQuestion().getId();
        questionAndAnswer.setUrl(url);
    }

    private void saveEntireTranscription(TranscriptionResultDTO results, QuestionAndAnswer questionAndAnswer) {
        String transcription = "";
        for(TranscriptionTextDTO transcriptionTextDto : results.getTranscripts()){
            transcription = transcription.concat(transcriptionTextDto.getTranscript());
        }

        questionAndAnswer.setTranscription(transcription);
    }

    public InterviewResponseDto getResultOfInterview(QuestionAndAnswer questionAndAnswer) {
        return InterviewResponseDto.builder()
                .pronunciationScore(questionAndAnswer.getPronunciationScore())
                .questionId(questionAndAnswer.getQuestion().getId())
                .transcription(questionAndAnswer.getTranscription())
                .url(questionAndAnswer.getUrl())
                .build();
    }
}
