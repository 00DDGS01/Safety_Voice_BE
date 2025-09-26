package safety_voice.be.safety_voice_be.domain.recordings.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class RecordingRequestDto {
    private String recordingName;
    private LocalDateTime recordingStartTime;
    private Long recordingDuration;
    private Long fileSize;
    private String mimeType;
}
