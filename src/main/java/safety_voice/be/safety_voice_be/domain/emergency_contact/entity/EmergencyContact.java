package safety_voice.be.safety_voice_be.domain.emergency_contact.entity;

import jakarta.persistence.*;
import lombok.*;
import safety_voice.be.safety_voice_be.domain.user.entity.User;
import safety_voice.be.safety_voice_be.domain.user.entity.UserSetting;

@Entity
@Table(name = "emergency_contacts")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmergencyContact {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "phone_number", nullable = false)
    private String phoneNumber;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_setting_id", nullable = false)
    private UserSetting userSetting;
}