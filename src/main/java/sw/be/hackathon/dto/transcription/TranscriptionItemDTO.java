package sw.be.hackathon.dto.transcription;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TranscriptionItemDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    private String start_time;
    private String end_time;
    private List<TranscriptionItemAlternativesDTO> alternatives;
    private String type;

    @Override
    public boolean equals(Object o){
        if(this == o){
            return true;
        }
        if(o == null || getClass() != o.getClass()){
            return false;
        }

        TranscriptionItemDTO transcriptionItemDTO = (TranscriptionItemDTO) o;
        if(transcriptionItemDTO.getType() == null){
            return false;
        }
        return Objects.equals(getType(), transcriptionItemDTO.getType());
    }

    @Override
    public int hashCode(){
        return Objects.hashCode(getType());
    }

    @Override
    public String toString(){
        return "TranscriptionItemDTO{" +
                "type=" + getType() +
                ", start_time'" + getStart_time() + "'" +
                ", end_time='" + getEnd_time() + "'" +
                ", alternatives='" + getAlternatives() + "'" +
                "}";
    }
}
