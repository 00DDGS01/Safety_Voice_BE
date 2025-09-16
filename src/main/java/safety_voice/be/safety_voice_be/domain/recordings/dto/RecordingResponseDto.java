package safety_voice.be.safety_voice_be.domain.recordings.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import safety_voice.be.safety_voice_be.domain.recordings.entity.Recording;

@Data
@AllArgsConstructor
public class RecordingResponseDto {
    private Long id;
    private String recordingName;
    private String s3Key;
    private String presignedUrl;
    private Recording.RecordingStatus status;

    public static RecordingResponseDto from(Recording recording) {
        return new RecordingResponseDto(
                recording.getId(),
                recording.getRecordingName(),
                recording.getS3Key(),
                null,
                recording.getStatus()
        );
    }

    public static RecordingResponseDto from(Recording recording, String presignedUrl) {
        return new RecordingResponseDto(
                recording.getId(),
                recording.getRecordingName(),
                recording.getS3Key(),
                presignedUrl,
                recording.getStatus()
        );
    }

}
