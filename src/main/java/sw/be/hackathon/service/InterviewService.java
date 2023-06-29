package sw.be.hackathon.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sw.be.hackathon.domain.Interview;
import sw.be.hackathon.domain.Member;
import sw.be.hackathon.domain.Question;
import sw.be.hackathon.dto.InterviewResponseDto;
import sw.be.hackathon.dto.transcription.*;
import sw.be.hackathon.repository.InterviewRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class InterviewService {
    private final InterviewRepository interviewRepository;

    public Interview findByMemberAndQuestion(Member member, Question question){
        return interviewRepository.findByMemberAndQuestion(member, question)
                .orElse(null);
    }

    public void remove(Interview interview) {
        interviewRepository.delete(interview);
    }

    public Interview saveNewInterview(Member member, Question question){
        Interview interview = Interview.builder()
                .member(member)
                .question(question)
                .build();
        interviewRepository.save(interview);

        return interview;
    }

    public void saveTranscription(TranscriptionResponseDTO transcriptionResponse, Interview interview) {
        TranscriptionResultDTO results = transcriptionResponse.getResults();
//        saveEntireTranscription(results, interview);
        saveConfidenceOfPronunciation(results.getItems(), interview);
    }

    private void saveConfidenceOfPronunciation(List<TranscriptionItemDTO> items, Interview interview) {
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

        interview.setTranscription(sentence);
        interview.setPronunciationScore(confidence);
        String url = "https://ainterview-video.s3.ap-northeast-2.amazonaws.com/" + interview.getMember().getUuid() + "/" + interview.getQuestion().getId();
        interview.setUrl(url);
    }

    private void saveEntireTranscription(TranscriptionResultDTO results, Interview interview) {
        String transcription = "";
        for(TranscriptionTextDTO transcriptionTextDto : results.getTranscripts()){
            transcription = transcription.concat(transcriptionTextDto.getTranscript());
        }

        interview.setTranscription(transcription);
    }

    public InterviewResponseDto getResultOfInterview(Interview interview) {
        return InterviewResponseDto.builder()
                .pronunciationScore(interview.getPronunciationScore())
                .questionId(interview.getQuestion().getId())
                .transcription(interview.getTranscription())
                .url(interview.getUrl())
                .build();
    }
}
