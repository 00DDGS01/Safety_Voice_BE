package safety_voice.be.safety_voice_be.domain.recordings.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CreateFolderRequestDto {

    @NotBlank
    private String folderName;

    private String description;

    private String colorTag;
}
