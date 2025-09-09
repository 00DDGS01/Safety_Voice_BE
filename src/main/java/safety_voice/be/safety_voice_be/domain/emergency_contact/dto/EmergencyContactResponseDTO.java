package safety_voice.be.safety_voice_be.domain.emergency_contact.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import safety_voice.be.safety_voice_be.domain.emergency_contact.entity.EmergencyContact;

@Getter
@Builder
@AllArgsConstructor
public class EmergencyContactResponseDTO {
    private Long id;
    private String name;
    private String phoneNumber;

    public static EmergencyContactResponseDTO form(EmergencyContact emergencyContact) {
        return EmergencyContactResponseDTO.builder()
                .id(emergencyContact.getId())
                .name(emergencyContact.getName())
                .phoneNumber(emergencyContact.getPhoneNumber())
                .build();
    }

    public static EmergencyContactResponseDTO from(EmergencyContact entity) {
        return EmergencyContactResponseDTO.builder()
                .id(entity.getId())
                .name(entity.getName())
                .phoneNumber(entity.getPhoneNumber())
                .build();
    }
}
