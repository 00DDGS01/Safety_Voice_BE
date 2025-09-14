package safety_voice.be.safety_voice_be.domain.user.entity;

import jakarta.persistence.*;
import lombok.*;
import safety_voice.be.safety_voice_be.domain.emergency_contact.entity.EmergencyContact;
import safety_voice.be.safety_voice_be.domain.recordings.entity.Recording;
import safety_voice.be.safety_voice_be.domain.recordings.entity.RecordingFolder;
import safety_voice.be.safety_voice_be.domain.safe_time.entity.SafeTime;
import safety_voice.be.safety_voice_be.domain.safe_zone.entity.SafeZone;
import safety_voice.be.safety_voice_be.global.base.BaseEntity;

import java.util.ArrayList;
import java.util.List;


@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "login_id", nullable = false, unique = true, length = 50)
    private String loginId;

    @Column(name = "password_hash", nullable = false, length = 255)
    private String passwordHash;

    @Column(nullable = false, unique = true, length = 255)
    private String email;

    @Column(nullable = false, unique = true, length = 50)
    private String nickname;

    @Column(length = 100)
    private String location;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Recording> recordings = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SafeZone> safeZones = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SafeTime> safeTimes = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RecordingFolder> recordingFolders = new ArrayList<>();

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private UserSetting userSetting;

    @Builder
    public User(String loginId, String passwordHash, String email, String nickname, String location) {
        this.loginId = loginId;
        this.passwordHash = passwordHash;
        this.email = email;
        this.nickname = nickname;
        this.location = location;

        // 유저가 생성되면 유저세팅도 자동 생성
        this.userSetting = new UserSetting(this);
    }


}
