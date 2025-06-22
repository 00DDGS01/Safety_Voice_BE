package safety_voice.be.safety_voice_be.domain.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import safety_voice.be.safety_voice_be.domain.user.entity.EmergencyContact;
import safety_voice.be.safety_voice_be.domain.user.entity.User;

import java.util.List;

public interface EmergencyContactRepository extends JpaRepository<EmergencyContact, Long> {

    // 사용자 ID 기반 연락처 모두 조회
    // List<EmergencyContact> findAllByUser(User user);
}
