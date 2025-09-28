package safety_voice.be.safety_voice_be.domain.sms.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import safety_voice.be.safety_voice_be.domain.sms.entity.SmsLog;

public interface SmsLogRepository extends JpaRepository<SmsLog, Long> {
}
