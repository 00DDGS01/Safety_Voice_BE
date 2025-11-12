package safety_voice.be.safety_voice_be.domain.safe_time.entity;

import jakarta.persistence.*;
import lombok.*;
import safety_voice.be.safety_voice_be.domain.safe_zone.entity.SafeZone;
import safety_voice.be.safety_voice_be.domain.user.entity.User;
import safety_voice.be.safety_voice_be.global.base.BaseEntity;

import java.time.LocalTime;

@Entity
@Table(name = "safe_times")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SafeTime extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "start_time", nullable = false)
    private LocalTime startTime;

    @Column(name = "end_time", nullable = false)
    private LocalTime endTime;

    @Column(name = "days_active", nullable = false)
    private String daysActive; // e.g., "MON,TUE,WED"

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "safe_zone_id", nullable = false)
    private SafeZone safeZone;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

}
