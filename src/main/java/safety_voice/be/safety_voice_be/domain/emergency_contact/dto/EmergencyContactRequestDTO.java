package safety_voice.be.safety_voice_be.domain.emergency_contact.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EmergencyContactRequestDTO {
    private String name;
    private String phoneNumber;
}
