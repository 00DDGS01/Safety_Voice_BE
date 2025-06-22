package safety_voice.be.safety_voice_be.domain.user.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "User Settings", description = "사용자의 트리거 설정 API")
public class UserSettingController {

    private final UserSettingService userSettingService;

    @GetMapping
    @Operation(
            summary = "사용자 설정 조회",
            description = "현재 로그인된 사용자의 트리거 단어, 반복 횟수, 긴급 연락 설정을 조회"
    )
    public ApiResponse<UserSettingRequestDto> getUserSetting(
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        Long userId = userDetails.getUserId();

        UserSettingRequestDto settings = userSettingService.getUserSetting(userId);
        return ApiResponse.success(settings);
    }

    @PutMapping
    @Operation(
            summary = "사용자 설정 업데이트",
            description = "현재 로그인된 사용자의 트리거 단어, 반복 횟수, 긴급 연락 설정을 저장"
    )
    public ApiResponse<String> updateUserSettings(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestBody UserSettingRequestDto dto) {
        Long userId = userDetails.getUserId();

        userSettingService.updateUserSetting(userId, dto);
        return ApiResponse.success("설정이 성공적으로 저장되었습니다.");
    }

}
