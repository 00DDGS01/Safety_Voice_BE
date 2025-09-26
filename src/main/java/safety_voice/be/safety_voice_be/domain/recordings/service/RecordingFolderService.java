package safety_voice.be.safety_voice_be.domain.recordings.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import safety_voice.be.safety_voice_be.domain.recordings.dto.RecordingFolderResponseDto;
import safety_voice.be.safety_voice_be.domain.recordings.dto.RecordingResponseDto;
import safety_voice.be.safety_voice_be.domain.recordings.entity.RecordingFolder;
import safety_voice.be.safety_voice_be.domain.recordings.repository.RecordingFolderRepository;
import safety_voice.be.safety_voice_be.domain.recordings.repository.RecordingRepository;
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
    private final RecordingRepository recordingRepository;
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

    public RecordingFolderResponseDto updateFolder(Long userId, Long folderId, String folderName, String description) {
        RecordingFolder recordingFolder = recordingFolderRepository.findById(folderId)
                .orElseThrow(() -> new CustomException(ErrorCode.RESOURCE_NOT_FOUND));

        if(!recordingFolder.getUser().getId().equals(userId)) {
            throw new CustomException(ErrorCode.FORBIDDEN);
        }

        if(folderName != null && !folderName.isBlank()) {
            recordingFolder.setFolderName(folderName);
        }

        if(description != null) {
            recordingFolder.setDescription(description);
        }

        return RecordingFolderResponseDto.from(recordingFolder);
    }

    public void deleteFolder(Long userId, Long folderId) {
        RecordingFolder folder = recordingFolderRepository.findById(folderId)
                .orElseThrow(() -> new CustomException(ErrorCode.RESOURCE_NOT_FOUND));

        if (!folder.getUser().getId().equals(userId)) {
            throw new CustomException(ErrorCode.FORBIDDEN);
        }

        recordingFolderRepository.delete(folder);
    }

    public void moveRecordingToFolder(Long userId, Long recordingId, Long targetFolderId) {
        var recording = recordingRepository.findById(recordingId)
                .orElseThrow(() -> new CustomException(ErrorCode.RESOURCE_NOT_FOUND));

        if(!recording.getUser().getId().equals(userId)) {
            throw new CustomException(ErrorCode.FORBIDDEN);
        }

        RecordingFolder targetFolder = recordingFolderRepository.findById(targetFolderId)
                .orElseThrow(() -> new CustomException(ErrorCode.RESOURCE_NOT_FOUND));

        if (!targetFolder.getUser().getId().equals(userId)) {
            throw new CustomException(ErrorCode.FORBIDDEN);
        }

        recording.setFolder(targetFolder);
    }
}
