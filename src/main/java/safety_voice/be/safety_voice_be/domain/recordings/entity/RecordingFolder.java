package safety_voice.be.safety_voice_be.domain.recordings.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import safety_voice.be.safety_voice_be.domain.user.entity.User;
import safety_voice.be.safety_voice_be.global.base.BaseEntity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "recording_folders")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RecordingFolder extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "folder_name", nullable = false)
    private String folderName;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    // 최근 추가일
    @Column(name = "last_added_date")
    private Date lastAddedDate;

    // 총 용량
    @Column(name = "total_size")
    private Long totalSize;

    @Column(name = "total_files")
    private int totalFiles;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @OneToMany(mappedBy = "folder", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Recording> recordings = new ArrayList<>();

}
