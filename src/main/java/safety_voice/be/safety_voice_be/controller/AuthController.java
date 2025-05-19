package safety_voice.be.safety_voice_be.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import safety_voice.be.safety_voice_be.domain.dto.SignupRequest;
import safety_voice.be.safety_voice_be.service.AuthService;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @CrossOrigin(origins = "*")
    @PostMapping("/signup")
    public ResponseEntity<Map<String, String>> signup(@RequestBody SignupRequest request) {
        authService.signup(request);
        return ResponseEntity
                .ok()
                .header("Content-Type", "application/json")
                .body(Map.of("message", "회원가입이 완료되었습니다."));
    }
}
