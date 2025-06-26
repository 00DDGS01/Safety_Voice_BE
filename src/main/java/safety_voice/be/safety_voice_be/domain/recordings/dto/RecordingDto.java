package safety_voice.be.safety_voice_be.domain.recordings.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RecordingDto {
    private String recordingName;
    private int duration;
    private long fileSize;
    private String filePath;
}
