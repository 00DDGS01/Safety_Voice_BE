package safety_voice.be.safety_voice_be.domain.recordings.dto;

import lombok.*;

// 폴더 생성 시 사용
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RecordingFolderRequestDto {
    private String folderName;
    private String colorTag;
    private String description;

}
