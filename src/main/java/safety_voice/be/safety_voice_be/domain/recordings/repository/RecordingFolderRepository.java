package safety_voice.be.safety_voice_be.domain.recordings.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import safety_voice.be.safety_voice_be.domain.recordings.entity.RecordingFolder;

import java.util.List;

@Repository
public interface RecordingFolderRepository extends JpaRepository<RecordingFolder, Long> {
    List<RecordingFolder> findAllByRecordingId(Long recordingId);
}
