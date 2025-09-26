package safety_voice.be.safety_voice_be.domain.recordings.dto;

import lombok.Data;

@Data
public class RecordingFolderRequestDto {
    private String folderName;
    private String description;
}
