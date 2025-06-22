package safety_voice.be.safety_voice_be;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
@ComponentScan(
        basePackages = "safety_voice.be.safety_voice_be",
        excludeFilters = @ComponentScan.Filter(
                type = FilterType.REGEX,
                pattern = "safety_voice\\.be\\.safety_voice_be\\.domain\\.recordings\\..*"
        )
)

public class SafetyVoiceBeApplication {

    public static void main(String[] args) {
        SpringApplication.run(SafetyVoiceBeApplication.class, args);
    }

}
