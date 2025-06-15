package safety_voice.be.safety_voice_be.domain.recordings.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import safety_voice.be.safety_voice_be.domain.recordings.entity.RecordingFolder;
import safety_voice.be.safety_voice_be.domain.recordings.repository.RecordingFolderRepository;
import safety_voice.be.safety_voice_be.domain.user.entity.User;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RecordingFolderService {

    private final RecordingFolderRepository recordingFolderRepository;

    // 전체 폴더 조회
    public List<RecordingFolder> getFoldersByUserId(Long userId) {
        return recordingFolderRepository.findAllByRecordingId(userId);
    }

    // 폴더 하나 조회
    public Optional<RecordingFolder> getFolderById(Long recordingFolderId) {
        return recordingFolderRepository.findById(recordingFolderId);
    }

    // 폴더 생성
    public RecordingFolder createFolder(String folderName, String colorTag, String description, User user) {
        RecordingFolder folder = RecordingFolder.builder()
                .folderName(folderName)
                .colorTag(colorTag)
                .description(description)
                .user(user)
                .lastAddeddDate(new Date())
                .totalSize(0L)
                .totalFiles(0)
                .build();

        return recordingFolderRepository.save(folder);
    }

    @Transactional
    public RecordingFolder updateFolder(Long folderId, String folderName, String colorTag, String description) {
        RecordingFolder folder = recordingFolderRepository.findById(folderId)
                .orElseThrow(() -> new IllegalArgumentException("폴더를 찾을 수 없습니다"));

        folder.setFolderName(folderName);
        folder.setColorTag(colorTag);
        folder.setDescription(description);
        return folder;
    }

    // 폴더 삭제
    public void deleteFolder(Long folderId) {
        recordingFolderRepository.deleteById(folderId);
    }



}
