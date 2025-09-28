package safety_voice.be.safety_voice_be.domain.sms.entity;

import jakarta.persistence.*;
import lombok.*;
import safety_voice.be.safety_voice_be.domain.user.entity.User;

import java.time.LocalDateTime;

@Entity
@Table(name = "sms_logs")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SmsLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 발송 주체 (회원)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // 수신자 번호
    @Column(name = "phone_number", nullable = false, length = 20)
    private String phoneNumber;

    // 발송 메시지
    @Column(name = "message", nullable = false, columnDefinition = "TEXT")
    private String message;

    // 발송 결과 (SENT, FAILED 등)
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private SmsStatus status;

    // AWS SNS 메시지 ID (트래킹용)
    @Column(name = "provider_message_id", length = 100)
    private String providerMessageId;

    // 발송 시각
    @Column(name = "sent_at", nullable = false)
    private LocalDateTime sentAt;

    public enum SmsStatus {
        SENT, FAILED
    }

}
