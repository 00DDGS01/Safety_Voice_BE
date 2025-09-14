package safety_voice.be.safety_voice_be.domain.emergency_contact.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import safety_voice.be.safety_voice_be.domain.emergency_contact.entity.EmergencyContact;

import java.util.List;

public interface EmergencyContactRepository extends JpaRepository<EmergencyContact, Long> {
    List<EmergencyContact> findAllByUserSettingId(Long userSettingId);
    List<EmergencyContact> findByUserSetting_User_Id(Long userId);
    boolean existsByUserSettingIdAndPhoneNumber(Long userSettingId, String phoneNumber);
}
