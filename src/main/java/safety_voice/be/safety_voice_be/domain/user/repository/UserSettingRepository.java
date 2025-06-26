package safety_voice.be.safety_voice_be.domain.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import safety_voice.be.safety_voice_be.domain.user.entity.User;
import safety_voice.be.safety_voice_be.domain.user.entity.UserSetting;

import java.util.Optional;

public interface UserSettingRepository extends JpaRepository<UserSetting, Long> {
    Optional<UserSetting> findByUserId(Long user_id);
    UserSetting findById(long id);

    boolean existsByUser(User savedUser);
}
