package safety_voice.be.safety_voice_be.domain.user.entity;

import jakarta.persistence.*;
import lombok.*;
import safety_voice.be.safety_voice_be.domain.recordings.entity.RecordingFolder;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "user_setting")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserSetting {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "trigger_word", length = 30)
    private String triggerWord;

    @Column(name = "trigger_repeat_count")
    private Integer triggerRepeatCount;

    // 시간 제한
    @Column(name = "trigger_within_seconds")
    private Integer triggerWithinSeconds;

    @Column(name = "emergency_repeat_count")
    private Integer emergencyRepeatCount;

    @Column(name = "emergency_within_seconds")
    private Integer emergencyWithinSeconds;

    @Column(name = "voice_sample_url", length = 500)
    private String voiceSampleUrl;

    @Column(name = "is_voice_trained")
    private Boolean isVoiceTrained;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<EmergencyContact> emergencyContacts = new ArrayList<>();

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

}
