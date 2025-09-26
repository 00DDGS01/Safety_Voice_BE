package safety_voice.be.safety_voice_be.domain.recordings.dto;

import lombok.Builder;
import lombok.Data;
import safety_voice.be.safety_voice_be.domain.recordings.entity.RecordingFolder;

import java.util.Date;

@Data
@Builder
public class RecordingFolderResponseDto {
    Long id;
    private String folderName;
    private String description;
    private Date lastAddedDate;
    private Long totalSize;
    private int totalFiles;

    public static RecordingFolderResponseDto from(RecordingFolder recordingFolder) {
        return RecordingFolderResponseDto.builder()
                .id(recordingFolder.getId())
                .folderName(recordingFolder.getFolderName())
                .description(recordingFolder.getDescription())
                .lastAddedDate(recordingFolder.getLastAddedDate())
                .totalSize(recordingFolder.getTotalSize())
                .totalFiles(recordingFolder.getTotalFiles())
                .build();
    }

}
