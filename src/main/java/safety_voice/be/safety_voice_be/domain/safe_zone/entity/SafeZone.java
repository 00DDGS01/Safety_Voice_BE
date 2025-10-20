package safety_voice.be.safety_voice_be.domain.safe_zone.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import safety_voice.be.safety_voice_be.domain.safe_time.entity.SafeTime;
import safety_voice.be.safety_voice_be.domain.user.entity.User;
import safety_voice.be.safety_voice_be.global.base.BaseEntity;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "safe_zones")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SafeZone extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "safe_zone_name", nullable = false)
    private String safeZoneName;

    @Column(nullable = false)
    private Integer radius; // in meters (e.g., 100, 200)

    @Column(nullable = false)
    private String location; // 주소명

    @Column(nullable = true)
    private Double latitude;

    @Column(nullable = true)
    private Double longitude;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @OneToMany(mappedBy = "safeZone", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SafeTime> safeTimes = new ArrayList<>();
}
