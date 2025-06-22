package safety_voice.be.safety_voice_be.domain.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import safety_voice.be.safety_voice_be.domain.user.dto.UserSettingRequestDto;
import safety_voice.be.safety_voice_be.domain.user.entity.EmergencyContact;
import safety_voice.be.safety_voice_be.domain.user.entity.User;
import safety_voice.be.safety_voice_be.domain.user.entity.UserSetting;
import safety_voice.be.safety_voice_be.domain.user.exception.UserErrorCode;
import safety_voice.be.safety_voice_be.domain.user.repository.UserRepository;
import safety_voice.be.safety_voice_be.domain.user.repository.UserSettingRepository;
import safety_voice.be.safety_voice_be.global.exception.response.CustomException;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserSettingService {

    private final UserSettingRepository userSettingRepository;
    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public UserSettingRequestDto getUserSetting(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(UserErrorCode.USER_NOT_FOUND));

        // 에러 코드 나중에 수정해야 함.
        UserSetting setting = user.getUserSetting();
        if (setting == null) {
            throw new CustomException(UserErrorCode.SETTING_NOT_FOUND);
        }

        UserSettingRequestDto dto = new UserSettingRequestDto();
        dto.setTriggerWord(setting.getTriggerWord());
        dto.setTriggerRepeatCount(setting.getTriggerRepeatCount());
        dto.setTriggerWithinSeconds(setting.getTriggerWithinSeconds());
        dto.setEmergencyRepeatCount(setting.getEmergencyRepeatCount());
        dto.setEmergencyRepeatWithinSeconds(setting.getEmergencyWithinSeconds());
        dto.setVoiceSampleUrl(setting.getVoiceSampleUrl());
        dto.setIsVoiceTrained(setting.getIsVoiceTrained());

        List<String> contactList = setting.getEmergencyContacts().stream()
                .map(EmergencyContact::getPhoneNumber)
                .collect(Collectors.toList());
        dto.setEmergencyContacts(contactList);

        return dto;
    }


    @Transactional
    public void updateUserSetting(Long userId, UserSettingRequestDto dto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(UserErrorCode.USER_NOT_FOUND));

        UserSetting setting = user.getUserSetting();
        if (setting == null) {
            // 유저에 세팅이 없을 경우 새로 생성
            setting = new UserSetting();
            setting.setUser(user);
            user.setUserSetting(setting);
        }

        setting.setTriggerWord(dto.getTriggerWord());
        setting.setTriggerRepeatCount(dto.getTriggerRepeatCount());
        setting.setTriggerWithinSeconds(dto.getTriggerWithinSeconds());
        setting.setEmergencyRepeatCount(dto.getEmergencyRepeatCount());
        setting.setEmergencyWithinSeconds(dto.getEmergencyRepeatWithinSeconds());
        setting.setVoiceSampleUrl(dto.getVoiceSampleUrl());
        setting.setIsVoiceTrained(dto.getIsVoiceTrained());

        // 기존 연락처 초기화
        setting.getEmergencyContacts().clear();

        final UserSetting finalsetting = setting;

        List<EmergencyContact> contacts = dto.getEmergencyContacts().stream()
                .map(phone -> {
                    EmergencyContact contact = new EmergencyContact();
                    contact.setPhoneNumber(phone);
                    contact.setUserSetting(finalsetting); // 관계 주의
                    return contact;
                })
                .toList();
        setting.getEmergencyContacts().addAll(contacts);
    }
}