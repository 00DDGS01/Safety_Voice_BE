package safety_voice.be.safety_voice_be.domain.recordings.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import safety_voice.be.safety_voice_be.domain.recordings.entity.Recording;
import safety_voice.be.safety_voice_be.domain.recordings.entity.RecordingFolder;
import safety_voice.be.safety_voice_be.domain.user.entity.User;

import java.util.List;
import java.util.Optional;

public interface RecordingRepository extends JpaRepository<Recording, Long> {

    List<Recording> findByUserId(Long userId);


    @Query("select r from Recording r where r.user.id = :userId")
    List<Recording> findAllByUserId(@Param("userId") Long userId);

    Optional<Recording> findByIdAndUserId(Long recordingId, Long userId);
}
