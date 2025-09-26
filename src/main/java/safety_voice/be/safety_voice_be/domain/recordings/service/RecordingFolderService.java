package safety_voice.be.safety_voice_be.domain.recordings.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import safety_voice.be.safety_voice_be.domain.recordings.dto.RecordingFolderResponseDto;
import safety_voice.be.safety_voice_be.domain.recordings.entity.RecordingFolder;
import safety_voice.be.safety_voice_be.domain.recordings.repository.RecordingFolderRepository;
import safety_voice.be.safety_voice_be.domain.user.entity.User;
import safety_voice.be.safety_voice_be.domain.user.repository.UserRepository;
import safety_voice.be.safety_voice_be.global.exception.code.ErrorCode;
import safety_voice.be.safety_voice_be.global.exception.response.CustomException;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Service
@RequiredArgsConstructor
@Transactional
public class RecordingFolderService {

    private final RecordingFolderRepository recordingFolderRepository;
    private final UserRepository userRepository;

    // 자동 생성 이름용 카운터
    private final AtomicInteger autoCounter = new AtomicInteger(1);

    public RecordingFolderResponseDto createFolder(Long userId, String folderName, String description) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        // folderName 없으면 자동 생성
        if (folderName == null || folderName.isBlank()) {
            folderName = "사건 파일 " + autoCounter.getAndIncrement();
        }

        RecordingFolder recordingFolder = RecordingFolder.builder()
                .folderName(folderName)
                .description(description)
                .user(user)
                .totalFiles(0)
                .totalSize(0L)
                .build();

        recordingFolderRepository.save(recordingFolder);

        return RecordingFolderResponseDto.from(recordingFolder);

    }

    @Transactional(readOnly = true)
    public List<RecordingFolderResponseDto> getUserFolders(Long userId) {
        return recordingFolderRepository.findByUserId(userId).stream()
                .map(RecordingFolderResponseDto::from)
                .toList();
    }

    public void deleteFolder(Long userId, Long folderId) {
        RecordingFolder folder = recordingFolderRepository.findById(folderId)
                .orElseThrow(() -> new CustomException(ErrorCode.RESOURCE_NOT_FOUND));

        if (!folder.getUser().getId().equals(userId)) {
            throw new CustomException(ErrorCode.FORBIDDEN);
        }

        recordingFolderRepository.delete(folder);
    }


}
