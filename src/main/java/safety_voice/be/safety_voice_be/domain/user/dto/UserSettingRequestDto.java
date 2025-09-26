package safety_voice.be.safety_voice_be.domain.user.dto;

import lombok.*;
import safety_voice.be.safety_voice_be.domain.emergency_contact.dto.EmergencyContactRequestDTO;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserSettingRequestDto {

    private String triggerWord;

    private String emergencyTriggerWord;

    private Boolean isVoiceTrained;

    private List<EmergencyContactRequestDTO> emergencyContacts;

}
