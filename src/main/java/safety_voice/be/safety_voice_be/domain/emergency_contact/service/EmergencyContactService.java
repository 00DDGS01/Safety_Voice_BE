package safety_voice.be.safety_voice_be.domain.emergency_contact.service;

import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import safety_voice.be.safety_voice_be.domain.emergency_contact.dto.EmergencyContactRequestDTO;
import safety_voice.be.safety_voice_be.domain.emergency_contact.dto.EmergencyContactResponseDTO;
import safety_voice.be.safety_voice_be.domain.emergency_contact.repository.EmergencyContactRepository;
import safety_voice.be.safety_voice_be.domain.emergency_contact.entity.EmergencyContact;
import safety_voice.be.safety_voice_be.domain.user.entity.User;
import safety_voice.be.safety_voice_be.domain.user.entity.UserSetting;
import safety_voice.be.safety_voice_be.domain.user.repository.UserRepository;

import java.util.List;

@Service
@Setter
@RequiredArgsConstructor
public class EmergencyContactService {

    private final EmergencyContactRepository emergencyContactRepository;
    private final UserRepository userRepository;

    @Transactional
    public void addContact(Long userId, EmergencyContactRequestDTO dto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        EmergencyContact emergencyContact = EmergencyContact.builder()
                .name(dto.getName())
                .phoneNumber(dto.getPhoneNumber())
                .userSetting(UserSetting.builder().build())
                .build();
    }

    @Transactional
    public List<EmergencyContactResponseDTO> getContacts(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        UserSetting userSetting = user.getUserSetting();
        if (userSetting == null) {
            return List.of();
        }

        List<EmergencyContact> contacts =
                emergencyContactRepository.findAllByUserSettingId(userSetting.getId());

        return contacts.stream()
                .map(EmergencyContactResponseDTO::from)
                .toList();
    }
}
