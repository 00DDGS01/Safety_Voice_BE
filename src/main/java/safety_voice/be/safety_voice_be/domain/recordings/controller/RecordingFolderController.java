package safety_voice.be.safety_voice_be.domain.recordings.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import safety_voice.be.safety_voice_be.domain.recordings.dto.CreateFolderRequestDto;
import safety_voice.be.safety_voice_be.domain.recordings.dto.FolderResponseDto;
import safety_voice.be.safety_voice_be.domain.recordings.service.RecordingFolderService;
import safety_voice.be.safety_voice_be.global.Security.CustomUserDetails;
import safety_voice.be.safety_voice_be.global.exception.response.ApiResponse;

@RestController
@RequestMapping("/api/folders")
@RequiredArgsConstructor
@Tag(name = "폴더 API", description = "녹음 파일을 저장할 폴더를 생성, 조회, 수정, 삭제하는 API입니다.")
public class RecordingFolderController {
    private final RecordingFolderService recordingFolderService;

    @PostMapping
    @Operation(
            summary = "폴더 생성",
            description = "사용자가 새 사건 폴더를 생성합니다."
    )
    public ApiResponse<FolderResponseDto> createFolder(@RequestBody @Valid CreateFolderRequestDto createFolderRequestDto,
                                                       @AuthenticationPrincipal CustomUserDetails userDetails) {
        FolderResponseDto response = recordingFolderService.createFolder(createFolderRequestDto, userDetails.getUserId());
        return ApiResponse.success(response);
    }
}
