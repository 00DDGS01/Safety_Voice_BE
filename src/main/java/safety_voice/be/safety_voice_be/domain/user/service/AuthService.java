package safety_voice.be.safety_voice_be.domain.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import safety_voice.be.safety_voice_be.domain.user.dto.LoginRequestDTO;
import safety_voice.be.safety_voice_be.domain.user.dto.LoginResponseDTO;
import safety_voice.be.safety_voice_be.domain.user.dto.SignupRequestDTO;
import safety_voice.be.safety_voice_be.domain.user.entity.User;
import safety_voice.be.safety_voice_be.domain.user.entity.UserSetting;
import safety_voice.be.safety_voice_be.domain.user.repository.UserRepository;
import safety_voice.be.safety_voice_be.domain.user.repository.UserSettingRepository;
import safety_voice.be.safety_voice_be.global.exception.code.ErrorCode;
import safety_voice.be.safety_voice_be.global.exception.response.CustomException;
import safety_voice.be.safety_voice_be.global.Security.JwtUtil;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final UserSettingRepository userSettingRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public void signup(SignupRequestDTO request) {

        // id 중복 검사
        if(userRepository.existsByLoginId(request.getLoginId())) {
            throw new CustomException(ErrorCode.DUPLICATE_LOGIN_ID);
        }

        // 이메일 중복 검사
        if(userRepository.existsByEmail(request.getEmail())) {
            throw new CustomException(ErrorCode.DUPLICATE_EMAIL);
        }

        // 비밀번호 암호화
        String encodedPassword = passwordEncoder.encode(request.getPassword());

        // User Entity 생성
        User user = User.builder()
                .loginId(request.getLoginId())
                .email(request.getEmail())
                .nickname(request.getNickname())
                .location(request.getLocation())
                .phoneNumber(request.getPhoneNumber())
                .passwordHash(encodedPassword)
                .build();

        User savedUser = userRepository.save(user);

        if(!userSettingRepository.existsByUser(savedUser)) {
            UserSetting setting = new UserSetting(savedUser);
            userSettingRepository.save(setting);
        }
    }

    public LoginResponseDTO login(LoginRequestDTO requestDTO) {
        User user = userRepository.findByLoginId(requestDTO.getLoginId())
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        if(!passwordEncoder.matches(requestDTO.getPassword(), user.getPasswordHash())) {
            throw new CustomException(ErrorCode.INVALID_PASSWORD);
        }

        String token = jwtUtil.generateToken(user.getLoginId());
        return new LoginResponseDTO(token, "로그인 성공");
    }

}
