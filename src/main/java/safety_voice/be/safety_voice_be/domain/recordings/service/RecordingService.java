package safety_voice.be.safety_voice_be.domain.recordings.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import safety_voice.be.safety_voice_be.domain.recordings.entity.Recording;
import safety_voice.be.safety_voice_be.domain.recordings.entity.RecordingFolder;
import safety_voice.be.safety_voice_be.domain.recordings.repository.RecordingFolderRepository;
import safety_voice.be.safety_voice_be.domain.recordings.repository.RecordingRepository;
import safety_voice.be.safety_voice_be.domain.user.entity.User;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;

@Service
public class RecordingService {

    private final RecordingRepository recordingRepository;
    private final RecordingFolderRepository recordingFolderRepository;

    public RecordingService(RecordingRepository recordingRepository, RecordingFolderRepository recordingFolderRepository) {
        this.recordingRepository = recordingRepository;
        this.recordingFolderRepository = recordingFolderRepository;
    }

    private String generateRecordingName(User user) {
        String today = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        int count = recordingRepository.countByUserAndRecordingNameStartingWith(user, today);

        return today + "(" + (count + 1) + ")";
    }

    @Transactional
    public void addRecordingToFolder(Long folderId, Recording recording) {
        RecordingFolder folder = recordingFolderRepository.findById(folderId)
                .orElseThrow(() -> new IllegalArgumentException("폴더를 찾을 수 없습니다."));

        // 폴더에 녹음 추가
        folder.getRecordings().add(recording);
        folder.setLastAddeddDate(new Date());

        // 용량과 파일 수 갱신
        Long currentSize = folder.getTotalSize() != null ? folder.getTotalSize() : 0L;
        folder.setTotalSize(currentSize + (recording.getFileSize() != null ? recording.getFileSize() : 0L));
        folder.setTotalFiles(folder.getTotalFiles() + 1);

        recording.setFolder(folder);
        recordingRepository.save(recording);
    }
}
