package safety_voice.be.safety_voice_be.domain.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserSettingResponseDto {

    private String triggerWord;
    private String emergencyTriggerWord;

    private Boolean isVoiceTrained;

    private List<String> emergencyContacts;
}
