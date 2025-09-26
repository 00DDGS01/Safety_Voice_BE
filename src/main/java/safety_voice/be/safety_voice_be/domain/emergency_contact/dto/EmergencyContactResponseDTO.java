package safety_voice.be.safety_voice_be.domain.emergency_contact.dto;

import lombok.*;
import safety_voice.be.safety_voice_be.domain.emergency_contact.entity.EmergencyContact;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmergencyContactResponseDTO {
    private String name;
    private String phoneNumber;

    public static EmergencyContactResponseDTO form(EmergencyContact emergencyContact) {
        return EmergencyContactResponseDTO.builder()
                .name(emergencyContact.getName())
                .phoneNumber(emergencyContact.getPhoneNumber())
                .build();
    }

    public static EmergencyContactResponseDTO from(EmergencyContact entity) {
        return EmergencyContactResponseDTO.builder()
                .name(entity.getName())
                .phoneNumber(entity.getPhoneNumber())
                .build();
    }
}
