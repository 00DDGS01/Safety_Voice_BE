package safety_voice.be.safety_voice_be.domain.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import safety_voice.be.safety_voice_be.domain.emergency_contact.dto.EmergencyContactRequestDTO;
import safety_voice.be.safety_voice_be.domain.emergency_contact.dto.EmergencyContactResponseDTO;
import safety_voice.be.safety_voice_be.domain.user.dto.UserSettingRequestDto;
import safety_voice.be.safety_voice_be.domain.emergency_contact.entity.EmergencyContact;
import safety_voice.be.safety_voice_be.domain.user.entity.User;
import safety_voice.be.safety_voice_be.domain.user.entity.UserSetting;
import safety_voice.be.safety_voice_be.domain.user.exception.UserErrorCode;
import safety_voice.be.safety_voice_be.domain.user.repository.UserRepository;
import safety_voice.be.safety_voice_be.domain.user.repository.UserSettingRepository;
import safety_voice.be.safety_voice_be.global.exception.response.CustomException;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class UserSettingService {

    private final UserSettingRepository userSettingRepository;
    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public UserSettingRequestDto getUserSetting(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(UserErrorCode.USER_NOT_FOUND));

        UserSetting setting = user.getUserSetting();
        if (setting == null) {
            setting = UserSetting.builder()
                    .triggerWord("")
                    .emergencyTriggerWord("")
                    .isVoiceTrained(false)
                    .user(user)
                    .build();
            user.setUserSetting(setting);
            userSettingRepository.save(setting);
        }

        return UserSettingRequestDto.builder()
                .triggerWord(setting.getTriggerWord())
                .emergencyTriggerWord(setting.getEmergencyTriggerWord())
                .isVoiceTrained(setting.getIsVoiceTrained())
                .emergencyContacts(
                        setting.getEmergencyContacts().stream()
                                .map(ec -> new EmergencyContactRequestDTO(ec.getName(), ec.getPhoneNumber()))
                                .collect(Collectors.toList())
                )
                .build();
    }


    @Transactional
    public void updateUserSetting(Long userId, UserSettingRequestDto dto) {
        log.info("✅ [updateUserSetting] userId={}, trigger={}, emergency={}, contacts={}",
                userId, dto.getTriggerWord(), dto.getEmergencyTriggerWord(), dto.getEmergencyContacts().size());        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(UserErrorCode.USER_NOT_FOUND));

        UserSetting setting = user.getUserSetting();
        if (setting == null) {
            setting = UserSetting.builder()
                    .triggerWord(dto.getTriggerWord())
                    .emergencyTriggerWord(dto.getEmergencyTriggerWord())
                    .isVoiceTrained(Boolean.TRUE.equals(dto.getIsVoiceTrained()))
                    .build();

            // 양방향 연관관계 세팅 (필요한 쪽 모두 연결)
            setting.setUser(user);      // UserSetting → User
            user.setUserSetting(setting); // User → UserSetting
        } else {
            // 기존 설정 업데이트
            setting.setTriggerWord(dto.getTriggerWord());
            setting.setEmergencyTriggerWord(dto.getEmergencyTriggerWord());
            setting.setIsVoiceTrained(Boolean.TRUE.equals(dto.getIsVoiceTrained()));
        }

        // 기존 연락처 초기화
        setting.getEmergencyContacts().clear();

        if (dto.getEmergencyContacts() != null) {
            final UserSetting finalSetting = setting;
            List<EmergencyContact> contacts = dto.getEmergencyContacts().stream()
                    .map(c -> EmergencyContact.builder()
                            .name(c.getName())
                            .phoneNumber(c.getPhoneNumber())
                            .userSetting(finalSetting) // 역방향 세팅
                            .build())
                    .toList();
            setting.getEmergencyContacts().addAll(contacts);
        }
        userSettingRepository.save(setting);
    }

    public void markVoiceAsTrained(Long userId) {
        UserSetting setting = userSettingRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("User setting not found"));
        setting.setIsVoiceTrained(true);

        if(!Boolean.TRUE.equals(setting.getIsVoiceTrained())) {
            setting.setIsVoiceTrained(true);
            userSettingRepository.save(setting);
        }
    }
}