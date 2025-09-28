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
import safety_voice.be.safety_voice_be.global.exception.code.ErrorCode;
import safety_voice.be.safety_voice_be.global.exception.response.BaseErrorCode;
import safety_voice.be.safety_voice_be.global.exception.response.CustomException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EmergencyContactService {

    private final EmergencyContactRepository emergencyContactRepository;
    private final UserRepository userRepository;
    private final UserSettingRepository userSettingRepository;

    @Transactional
    public void addContact(Long userId, EmergencyContactRequestDTO dto) {

        UserSetting userSetting = userSettingRepository.findByUserId(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_SETTING_NOT_FOUND));

        boolean exists = emergencyContactRepository
                .existsByUserSettingIdAndPhoneNumber(userSetting.getId(), dto.getPhoneNumber());
        if (exists) {
            throw new CustomException(ErrorCode.DUPLICATE_EMERGENCY_CONTACT);
        }

        EmergencyContact emergencyContact = EmergencyContact.builder()
                .name(dto.getName())
                .phoneNumber(normalizePhoneNumber(dto.getPhoneNumber()))
                .userSetting(userSetting)
                .build();

        emergencyContactRepository.save(emergencyContact);
    }

    @Transactional(readOnly = true)
    public List<EmergencyContactResponseDTO> getContacts(Long userId) {

        UserSetting userSetting = userSettingRepository.findByUserId(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_SETTING_NOT_FOUND));


        return emergencyContactRepository.findAllByUserSettingId(userSetting.getId())
                .stream()
                .map(EmergencyContactResponseDTO::from)
                .toList();
    }

    private String normalizePhoneNumber(String rawPhoneNumber) {
        String cleaned = rawPhoneNumber.replaceAll("[^0-9]", "");
        if (cleaned.startsWith("0")) {
            return "+82" + cleaned.substring(1);
        }
        return "+" + cleaned;
    }
}
