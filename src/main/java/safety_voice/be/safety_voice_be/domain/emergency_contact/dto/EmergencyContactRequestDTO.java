package safety_voice.be.safety_voice_be.domain.emergency_contact.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmergencyContactRequestDTO {
    private String name;
    private String phoneNumber;
}
