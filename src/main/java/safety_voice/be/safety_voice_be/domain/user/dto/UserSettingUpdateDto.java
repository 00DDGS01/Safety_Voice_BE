package safety_voice.be.safety_voice_be.domain.user.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class UserSettingUpdateDto {
    private String triggerWord;
    private Integer triggerRepeatCount;
    private Integer triggerWithSeconds;
    private Integer emergencyRepeatCount;
    private Integer emergencyWithSeconds;

    private List<String> emergencyContacts;
}
