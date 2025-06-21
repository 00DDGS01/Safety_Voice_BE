package safety_voice.be.safety_voice_be.domain.user.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import safety_voice.be.safety_voice_be.domain.user.dto.UserSettingRequestDto;
import safety_voice.be.safety_voice_be.domain.user.service.UserSettingService;
import safety_voice.be.safety_voice_be.global.Security.CustomUserDetails;
import safety_voice.be.safety_voice_be.global.exception.response.ApiResponse;

@RestController
@RequestMapping("/api/user/settings")
@RequiredArgsConstructor
public class UserSettingController {

    private final UserSettingService userSettingService;

    @GetMapping
    public ApiResponse<UserSettingRequestDto> getUserSetting(
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        Long userId = userDetails.getUser().getId();

        UserSettingRequestDto settings = userSettingService.getUserSetting(userId);
        return ApiResponse.success(settings);
    }

    @PutMapping
    public ApiResponse<String> updateUserSettings(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestBody UserSettingRequestDto dto) {
        Long userId = userDetails.getUser().getId();

        userSettingService.updateUserSetting(userId, dto);
        return ApiResponse.success("설정이 성공적으로 저장되었습니다.");
    }

}
