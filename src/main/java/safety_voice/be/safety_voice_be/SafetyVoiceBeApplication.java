package safety_voice.be.safety_voice_be;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class SafetyVoiceBeApplication {

    public static void main(String[] args) {
        SpringApplication.run(SafetyVoiceBeApplication.class, args);
    }

}
