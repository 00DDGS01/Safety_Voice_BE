package safety_voice.be.safety_voice_be.domain.safe_zone.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import safety_voice.be.safety_voice_be.domain.safe_zone.dto.SafeZoneRequestDto;
import safety_voice.be.safety_voice_be.domain.safe_zone.dto.SafeZoneResponseDto;
import safety_voice.be.safety_voice_be.domain.safe_zone.service.SafeZoneService;
import safety_voice.be.safety_voice_be.global.Security.CustomUserDetails;
import safety_voice.be.safety_voice_be.global.exception.response.ApiResponse;

import java.util.List;

@RestController
@RequestMapping("/api/safe-zones")
@RequiredArgsConstructor
@SecurityRequirement(name = "BearerAuth")
@Tag(name = "Safe Zone", description = "안전지대 및 타임테이블 API")
public class SafeZoneController {

    private final SafeZoneService safeZoneService;

    @GetMapping
    @Operation(summary = "사용자 안전지대 조회", description = "로그인된 사용자의 모든 안전지대와 타임테이블 정보를 조회합니다.")
    public ApiResponse<List<SafeZoneResponseDto>> getSafeZones(
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        Long userId = userDetails.getUserId();
        return ApiResponse.success(safeZoneService.getSafeZones(userId));
    }

    @PutMapping
    @Operation(summary = "사용자 안전지대 저장/수정", description = "사용자의 안전지대 정보와 타임테이블을 저장 또는 갱신합니다.")
    public ApiResponse<String> updateSafeZones(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestBody List<SafeZoneRequestDto> requestDtos) {
        Long userId = userDetails.getUserId();
        safeZoneService.saveOrUpdateSafeZones(userId, requestDtos);
        return ApiResponse.success("안전지대 설정이 성공적으로 저장되었습니다.");
    }
}