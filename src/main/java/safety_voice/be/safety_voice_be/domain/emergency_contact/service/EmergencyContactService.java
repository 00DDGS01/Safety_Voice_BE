package safety_voice.be.safety_voice_be.domain.emergency_contact.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import safety_voice.be.safety_voice_be.domain.emergency_contact.dto.EmergencyContactRequestDTO;
import safety_voice.be.safety_voice_be.domain.emergency_contact.dto.EmergencyContactResponseDTO;
import safety_voice.be.safety_voice_be.domain.emergency_contact.repository.EmergencyContactRepository;
import safety_voice.be.safety_voice_be.domain.emergency_contact.entity.EmergencyContact;
import safety_voice.be.safety_voice_be.domain.user.entity.UserSetting;
import safety_voice.be.safety_voice_be.domain.user.repository.UserRepository;
import safety_voice.be.safety_voice_be.domain.user.repository.UserSettingRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EmergencyContactService {

    private final EmergencyContactRepository emergencyContactRepository;
    private final UserRepository userRepository;
    private final UserSettingRepository userSettingRepository;

    @Transactional
    public void addContact(Long userId, EmergencyContactRequestDTO dto) {

        userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        UserSetting userSetting = userSettingRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("UserSetting not found"));

        boolean exists = emergencyContactRepository
                .existsByUserSettingIdAndPhoneNumber(userSetting.getId(), dto.getPhoneNumber());
        if (exists) {
            throw new IllegalArgumentException("이미 등록된 연락처입니다.");
        }

        EmergencyContact emergencyContact = EmergencyContact.builder()
                .name(dto.getName())
                .phoneNumber(dto.getPhoneNumber())
                .userSetting(userSetting)
                .build();

        emergencyContactRepository.save(emergencyContact);
    }

    @Transactional(readOnly = true)
    public List<EmergencyContactResponseDTO> getContacts(Long userId) {

        UserSetting userSetting = userSettingRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("User setting not found"));


        return emergencyContactRepository.findAllByUserSettingId(userSetting.getId())
                .stream()
                .map(EmergencyContactResponseDTO::from)
                .toList();
    }
}
