package safety_voice.be.safety_voice_be.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import safety_voice.be.safety_voice_be.domain.dto.LoginRequestDTO;
import safety_voice.be.safety_voice_be.domain.dto.LoginResponseDTO;
import safety_voice.be.safety_voice_be.domain.dto.SignupRequestDTO;
import safety_voice.be.safety_voice_be.service.AuthService;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @CrossOrigin(origins = "*")
    @PostMapping("/signup")
    public ResponseEntity<Map<String, String>> signup(@RequestBody SignupRequestDTO request) {
        authService.signup(request);
        return ResponseEntity
                .ok()
                .header("Content-Type", "application/json; charset=UTF-8")
                .body(Map.of("message", "회원가입이 완료되었습니다."));
    }

    @CrossOrigin(origins = "*")
    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody LoginRequestDTO request) {
        try {
            // 로그인 성공 시 토큰 포함된 응답 받기
            LoginResponseDTO loginResponse = authService.login(request);
            return ResponseEntity
                    .ok()
                    .header("Content-Type", "application/json; charset=UTF-8")
                    .body(Map.of(
                            "message", "로그인 성공",
                            "token", loginResponse.getToken()
                    ));
        } catch (IllegalArgumentException e) {
            return ResponseEntity
                    .status(401)
                    .body(Map.of("message", e.getMessage()));
        }
    }
}
