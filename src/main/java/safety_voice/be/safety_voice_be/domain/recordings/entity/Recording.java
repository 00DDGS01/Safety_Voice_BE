package safety_voice.be.safety_voice_be.domain.recordings.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.*;
import safety_voice.be.safety_voice_be.domain.user.entity.User;
import safety_voice.be.safety_voice_be.global.base.BaseEntity;

import java.time.LocalDateTime;

@Entity
@Table(
        name = "recordings",
        indexes = {
                @Index(name = "idx_recordings_user_start", columnList = "user_id, recording_start_time"),
                @Index(name = "idx_recordings_s3key", columnList = "s3_key")
        },
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_recordings_s3key", columnNames = {"s3_key"})
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Recording extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "recording_name", nullable = false, length = 255)
    private String recordingName;

    @NotNull
    @Column(name = "recording_start_time", nullable = false)
    private LocalDateTime recordingStartTime;

    @NotNull
    @PositiveOrZero
    @Column(name = "recording_duration", nullable = false)
    private Long recordingDuration;

    // 기능이 추가해야 됨
    @Lob
    @Column(name = "recording_summary", columnDefinition = "TEXT")
    private String recordingSummary;

    @NotNull
    @PositiveOrZero
    @Column(name = "file_size", nullable = false)
    private Long fileSize; // bytes

    @NotNull
    @Column(name = "s3_key", nullable = false, length = 512)
    private String s3Key;

    @Enumerated(EnumType.STRING)
    @Column(name = "storage", nullable = false, length = 20)
    private StorageType storage;

    @Column(name = "mime_type", length = 100)
    private String mimeType;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private RecordingStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "folder_id")
    private RecordingFolder folder;


    public enum StorageType { S3 }
    public enum RecordingStatus { UPLOADING, READY, FAILED }
}
