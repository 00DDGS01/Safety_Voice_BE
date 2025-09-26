package safety_voice.be.safety_voice_be.domain.recordings.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import safety_voice.be.safety_voice_be.domain.recordings.entity.Recording;
import safety_voice.be.safety_voice_be.domain.recordings.entity.RecordingFolder;
import safety_voice.be.safety_voice_be.domain.user.entity.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface RecordingRepository extends JpaRepository<Recording, Long> {

    List<Recording> findByUserId(Long userId);

    Optional<Recording> findByIdAndUserId(Long recordingId, Long userId);

    long countByFolderId(Long folderId);

    @Query("select coalesce(sum(r.fileSize), 0) from Recording r where r.folder.id = :folderId")
    long sumFileSizeByFolderId(@Param("folderId") Long folderId);

    @Query("select max(r.createdAt) from Recording r where r.folder.id = :folderId")
    Optional<LocalDateTime> findLastCreatedAtByFolderId(@Param("folderId") Long folderId);
}
