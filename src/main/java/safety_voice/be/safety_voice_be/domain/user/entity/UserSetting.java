package safety_voice.be.safety_voice_be.domain.user.entity;

import jakarta.persistence.*;
import lombok.*;
import safety_voice.be.safety_voice_be.domain.emergency_contact.entity.EmergencyContact;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "user_setting")
@Getter
@Setter
@AllArgsConstructor
@Builder
public class UserSetting {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "trigger_word", length = 30)
    private String triggerWord;

    @Column(name = "emergency_trigger_word")
    private String emergencyTriggerWord;

    @Column(name = "is_voice_trained")
    private Boolean isVoiceTrained;

    @OneToMany(mappedBy = "userSetting", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<EmergencyContact> emergencyContacts = new ArrayList<>();

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    protected UserSetting() {}

    public UserSetting(User user) {
        this.user = user;
        this.isVoiceTrained = false;
    }
}
