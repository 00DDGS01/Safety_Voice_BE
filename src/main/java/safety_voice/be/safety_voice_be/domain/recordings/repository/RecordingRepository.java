package safety_voice.be.safety_voice_be.domain.recordings.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import safety_voice.be.safety_voice_be.domain.recordings.entity.Recording;
import safety_voice.be.safety_voice_be.domain.user.entity.User;

public interface RecordingRepository extends JpaRepository<Recording, Integer> {

    // 사용자가 생성한 오늘 날짜로 시작하는 recordingName의 수를 세어 (n + 1)
    int countByUserAndRecordingNameStartingWith(User user, String recordingName);
}
