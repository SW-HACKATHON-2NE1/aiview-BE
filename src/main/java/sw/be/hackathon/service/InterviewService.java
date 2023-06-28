package sw.be.hackathon.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sw.be.hackathon.domain.Interview;
import sw.be.hackathon.domain.Member;
import sw.be.hackathon.domain.Question;
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

    public void saveNewInterview(Member member, Question question){

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
        for(TranscriptionItemDTO item : items){
            String content = "";
            for(TranscriptionItemAlternativesDTO alternativesDTO : item.getAlternatives()){
                Double tempConfidence = Double.parseDouble(alternativesDTO.getConfidence());
                if(tempConfidence <= 0.80 && !alternativesDTO.getContent().equals(".")){
                    wrong = true;
                }
                confidence += tempConfidence;

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
                sentence = sentence.replace(" . ", ".");
            }
        }
        confidence /= items.size();

        interview.setTranscription(sentence);
        interview.setPronunciationScore(confidence);
    }

    private void saveEntireTranscription(TranscriptionResultDTO results, Interview interview) {
        String transcription = "";
        for(TranscriptionTextDTO transcriptionTextDto : results.getTranscripts()){
            transcription = transcription.concat(transcriptionTextDto.getTranscript());
        }

        interview.setTranscription(transcription);
    }
}
