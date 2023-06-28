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
public class TranscriptionResultDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private List<TranscriptionTextDTO> transcripts;

    private List<TranscriptionItemDTO> items;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        TranscriptionResultDTO transcriptionResultDTO = (TranscriptionResultDTO) o;
        if (transcriptionResultDTO.getTranscripts() == null) {
            return false;
        }
        return Objects.equals(getTranscripts(), transcriptionResultDTO.getTranscripts());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getTranscripts());
    }

    @Override
    public String toString() {
        return "TranscriptionResultDTO{" +
                "transcripts=" + getTranscripts() +
                ", items='" + getItems() + "'" +
                "}";
    }
}
