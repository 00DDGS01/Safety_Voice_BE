package safety_voice.be.safety_voice_be.domain.safe_time.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import safety_voice.be.safety_voice_be.domain.safe_time.entity.SafeTime;

import java.util.List;

public interface SafeTimeRepository extends JpaRepository<SafeTime, Long> {
    List<SafeTime> findBySafeZoneId(Long safeZoneId);
}