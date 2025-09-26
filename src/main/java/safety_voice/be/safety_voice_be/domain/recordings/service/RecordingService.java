package safety_voice.be.safety_voice_be.domain.recordings.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import safety_voice.be.safety_voice_be.domain.recordings.dto.RecordingRequestDto;
import safety_voice.be.safety_voice_be.domain.recordings.dto.RecordingResponseDto;
import safety_voice.be.safety_voice_be.domain.recordings.entity.Recording;
import safety_voice.be.safety_voice_be.domain.recordings.entity.RecordingFolder;
import safety_voice.be.safety_voice_be.domain.recordings.repository.RecordingRepository;
import safety_voice.be.safety_voice_be.domain.user.entity.User;
import safety_voice.be.safety_voice_be.domain.user.repository.UserRepository;
import safety_voice.be.safety_voice_be.global.exception.code.ErrorCode;
import safety_voice.be.safety_voice_be.global.exception.response.CustomException;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.PresignedPutObjectRequest;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class RecordingService {

    private final RecordingRepository recordingRepository;
    private final UserRepository userRepository;
    private final S3Presigner s3Presigner;
    private final S3Client s3Client;

    @Value("${cloud.aws.s3.bucket}")
    private String bucketName;

    private static final DateTimeFormatter DTF = DateTimeFormatter.ofPattern("yyyy/MM/dd");


    public RecordingResponseDto createRecording(Long userId, RecordingRequestDto recordingRequestDto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        String datePath = LocalDate.now().format(DTF);

        // 1. MIME 타입에 따라 확장자 결정
        String extension;
        switch (recordingRequestDto.getMimeType()) {
            case "audio/m4a" -> extension = "m4a";
            case "audio/mp4" -> extension = "m4a";
            case "audio/wav" -> extension = "wav";
            default -> throw new CustomException(ErrorCode.UNSUPPORTED_MEDIA_TYPE);
        }
        // S3 Key 네이밍 (userId/년월/uuid.wav)
        String s3Key = String.format("%d/%s/%s.wav",
                userId, datePath, UUID.randomUUID(), extension);

        // PUT용 Presigened URL 생성 (Content-Type만 지정)
        PutObjectRequest putRequest = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(s3Key)
                .contentType(recordingRequestDto.getMimeType())
                .build();

        PresignedPutObjectRequest presigned = s3Presigner.presignPutObject(
                r -> r.signatureDuration(Duration.ofMinutes(10)).putObjectRequest(putRequest)
        );

        Recording recording = Recording.builder()
                .recordingName(recordingRequestDto.getRecordingName())
                .recordingStartTime(recordingRequestDto.getRecordingStartTime())
                .recordingDuration(recordingRequestDto.getRecordingDuration())
                .fileSize(recordingRequestDto.getFileSize() != null ? recordingRequestDto.getFileSize() : 0L)
                .mimeType(recordingRequestDto.getMimeType())
                .s3Key(s3Key)
                .storage(Recording.StorageType.S3)
                .status(Recording.RecordingStatus.UPLOADING)
                .user(user)
                .build();

        recordingRepository.save(recording);

        if(recording.getFolder() != null) {
            updateFolderStatus(recording.getFolder());
        }

        return RecordingResponseDto.from(recording, presigned.url().toString());
    }

    // Folder 통계 관련 함수
    private void updateFolderStatus(RecordingFolder folder) {
        folder.setTotalFiles(folder.getRecordings().size());
        folder.setTotalSize(
                folder.getRecordings().stream()
                        .mapToLong(Recording::getFileSize)
                        .sum()
        );
        folder.setLastAddedDate(new Date());
    }

    @Transactional(readOnly = true)
    public List<RecordingResponseDto> getUserRecordings(Long userId) {
        return recordingRepository.findByUserId(userId).stream()
                .map(RecordingResponseDto::from)
                .toList();
    }

    /**
     * 업로드 완료 표시 (파일 크기 반영 & 상태 READY 변경)
     * @param userId 업로드한 사용자 ID
     * @param recordingId 업로드 대상 녹음 ID
     * @param fileSize 실제 업로드된 파일 크기 (bytes)
     */
    public void markUploadComplete(Long userId, Long recordingId, long fileSize) {
        Recording recording = recordingRepository.findById(recordingId)
                .orElseThrow(() -> new CustomException(ErrorCode.RESOURCE_NOT_FOUND));

        if (!recording.getUser().getId().equals(userId)) {
            throw new CustomException(ErrorCode.FORBIDDEN);
        }

        recording.setFileSize(fileSize);
        recording.setStatus(Recording.RecordingStatus.READY);
    }

    /**
     * Presigned GET URL 발급 (S3에서 녹음 파일 다운로드 가능하도록)
     *
     * @param userId 요청한 사용자 ID
     * @param recordingId 다운로드할 녹음 ID
     * @return presigned GET URL (10분간 유효)
     */
    @Transactional(readOnly = true)
    public String getPresignedGetUrl(Long userId, Long recordingId) {
        Recording rec = recordingRepository.findById(recordingId)
                .orElseThrow(() -> new CustomException(ErrorCode.RESOURCE_NOT_FOUND));
        if (!rec.getUser().getId().equals(userId)) {
            throw new CustomException(ErrorCode.FORBIDDEN); // 본인 소유 아님 → 권한 거부
        }

        // S3 객체 요청 생성
        var getReq = software.amazon.awssdk.services.s3.model.GetObjectRequest.builder()
                .bucket(bucketName)
                .key(rec.getS3Key())
                .build();

        // Presigned URL 발급 (10분간 유효)
        var presigned = s3Presigner.presignGetObject(r ->
                r.signatureDuration(Duration.ofMinutes(10)).getObjectRequest(getReq)
        );
        return presigned.url().toString();
    }

    /**
     * 녹음 파일 삭제 (DB에서 삭제 + S3 객체도 삭제)
     * @param userId 요청한 사용자 ID
     * @param recordingId 삭제할 녹음 ID
     */
    public void deleteRecording(Long userId, Long recordingId) {
        Recording recording = recordingRepository.findById(recordingId)
                .orElseThrow(() -> new CustomException(ErrorCode.RESOURCE_NOT_FOUND));
        if (!recording.getUser().getId().equals(userId)) {
            throw new CustomException(ErrorCode.FORBIDDEN); // 본인 소유 아님 → 권한 거부
        }

        s3Client.deleteObject(DeleteObjectRequest.builder()
                .bucket(bucketName)
                .key(recording.getS3Key())
                .build());

        recordingRepository.delete(recording);
    }
}