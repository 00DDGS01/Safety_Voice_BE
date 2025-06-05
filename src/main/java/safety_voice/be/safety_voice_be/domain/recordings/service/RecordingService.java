package safety_voice.be.safety_voice_be.domain.recordings.service;

import org.springframework.stereotype.Service;
import safety_voice.be.safety_voice_be.domain.recordings.repository.RecordingRepository;
import safety_voice.be.safety_voice_be.domain.user.entity.User;
import safety_voice.be.safety_voice_be.domain.user.repository.UserRepository;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Service
public class RecordingService {

    private final RecordingRepository recordingRepository;
    private final UserRepository userRepository;

    public RecordingService(RecordingRepository recordingRepository, UserRepository userRepository) {
        this.recordingRepository = recordingRepository;
        this.userRepository = userRepository;
    }

    private String generateRecordingName(User user) {
        String today = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        int count = recordingRepository.countByUserAndRecordingNameStartingWith(user, today);

        return today + "(" + (count + 1) + ")";
    }
}
