package safety_voice.be.safety_voice_be.domain.user.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginRequestDTO {
    private String loginId;
    private String password;
}
