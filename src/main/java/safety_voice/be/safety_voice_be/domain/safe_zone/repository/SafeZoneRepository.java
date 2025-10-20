package safety_voice.be.safety_voice_be.domain.safe_zone.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import safety_voice.be.safety_voice_be.domain.safe_zone.entity.SafeZone;

import java.util.List;

public interface SafeZoneRepository extends JpaRepository<SafeZone, Long> {
    List<SafeZone> findByUserId(Long userId);
}