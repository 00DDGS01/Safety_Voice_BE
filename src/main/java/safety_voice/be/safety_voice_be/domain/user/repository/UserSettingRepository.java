package safety_voice.be.safety_voice_be.domain.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import safety_voice.be.safety_voice_be.domain.user.entity.UserSetting;

public interface UserSettingRepository extends JpaRepository<UserSetting, Long> {
    UserSetting findById(long id);
}
