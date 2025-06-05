package safety_voice.be.safety_voice_be.domain.recordings.entity;

import jakarta.persistence.*;
import lombok.*;
import safety_voice.be.safety_voice_be.domain.user.entity.User;

import java.time.LocalDateTime;

@Entity
@Table(name = "recordings")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Recording {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "recording_name", nullable = false)
    private String recordingName;

    @Column(name = "recording_start_time", nullable = false)
    private LocalDateTime recordingStartTime;

    @Column(name = "recording_duration", nullable = false)
    private Long recordingDuration; // 초 단위 저장 추천

    @Column(name = "recording_summary", columnDefinition = "TEXT")
    private String recordingSummary;

    @Column(name = "file_size", nullable = false)
    private Long fileSize; // 바이트 단위

    @Column(name = "file_path", nullable = false, length = 512)
    private String filePath;

    // 사용자와의 N:1 관계
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "folder_id")
    private RecordingFolder folder;

}
