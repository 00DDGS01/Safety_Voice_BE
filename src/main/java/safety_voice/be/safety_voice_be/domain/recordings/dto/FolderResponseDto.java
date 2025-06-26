package safety_voice.be.safety_voice_be.domain.recordings.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class FolderResponseDto {
    private Long id;
    private String folderName;
    private String description;
    private String colorTag;
    private int totalFiles;
    private Long totalSize;
}
