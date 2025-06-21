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
public class UserSettingRequestDto {

    private String triggerWord;
    private Integer triggerRepeatCount;
    private Integer triggerWithinSeconds;

    private Integer emergencyRepeatCount;
    private Integer emergencyRepeatWithinSeconds;

    private String voiceSampleUrl;
    private Boolean isVoiceTrained;

    private List<String> emergencyContacts;
}
