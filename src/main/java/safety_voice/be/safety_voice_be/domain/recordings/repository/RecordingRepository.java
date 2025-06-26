package safety_voice.be.safety_voice_be.domain.recordings.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import safety_voice.be.safety_voice_be.domain.recordings.entity.Recording;
import safety_voice.be.safety_voice_be.domain.recordings.entity.RecordingFolder;
import safety_voice.be.safety_voice_be.domain.user.entity.User;

import java.util.List;

public interface RecordingRepository extends JpaRepository<Recording, Long> {

    // 사용자가 생성한 오늘 날짜로 시작하는 recordingName의 수를 세어 (n + 1)
    int countByUserAndRecordingNameStartingWith(User user, String recordingName);

    List<RecordingFolder> findAllByUser(User user);
}
