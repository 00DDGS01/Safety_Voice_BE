package safety_voice.be.safety_voice_be.domain.user.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import safety_voice.be.safety_voice_be.domain.user.dto.LoginRequestDTO;
import safety_voice.be.safety_voice_be.domain.user.dto.LoginResponseDTO;
import safety_voice.be.safety_voice_be.domain.user.dto.SignupRequestDTO;
import safety_voice.be.safety_voice_be.domain.user.service.AuthService;
import safety_voice.be.safety_voice_be.global.exception.response.ApiResponse;
import safety_voice.be.safety_voice_be.global.exception.response.CustomException;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @CrossOrigin(origins = "*")
    @PostMapping("/signup")
    public ApiResponse<String> signup(@RequestBody SignupRequestDTO signupRequestDTO) {
        authService.signup(signupRequestDTO);
        return ApiResponse.success("회원가입이 완료되었습니다.");
    }

    @CrossOrigin(origins = "*")
    @PostMapping("/login")
    public ApiResponse<LoginResponseDTO> login(@RequestBody LoginRequestDTO loginRequestDTO) {
        try {
            LoginResponseDTO loginResponseDTO = authService.login(loginRequestDTO);
            return ApiResponse.success(loginResponseDTO);
        } catch (CustomException e) {
            return ApiResponse.error(e.getErrorCode());
        }
    }

    @GetMapping("/mypage")
    @SecurityRequirement(name = "BearerAuth")
    public ResponseEntity<String> mypage() {
        return ResponseEntity.ok("JWT 인증된 사용자");
    }
}
