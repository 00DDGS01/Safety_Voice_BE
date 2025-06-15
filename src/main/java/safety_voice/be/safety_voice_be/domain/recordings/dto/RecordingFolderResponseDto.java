package safety_voice.be.safety_voice_be.domain.recordings.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.Date;

@Getter
@Builder
public class RecordingFolderResponseDto {
    private Long id;
    private String folderName;
    private String colorTag;
    private String description;
    private Date lastAddeddDate;
    private Long totalSize;
    private int totalFiles;

}
