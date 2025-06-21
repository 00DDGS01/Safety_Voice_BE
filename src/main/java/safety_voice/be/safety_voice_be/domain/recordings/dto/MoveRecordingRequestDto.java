package safety_voice.be.safety_voice_be.domain.recordings.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MoveRecordingRequestDto {
    private Long recordingId;
    private Long targetFolderId;
}
