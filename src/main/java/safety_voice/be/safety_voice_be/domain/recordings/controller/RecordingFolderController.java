package safety_voice.be.safety_voice_be.domain.recordings.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import safety_voice.be.safety_voice_be.domain.recordings.dto.RecordingFolderResponseDto;
import safety_voice.be.safety_voice_be.domain.recordings.service.RecordingFolderService;
import safety_voice.be.safety_voice_be.global.exception.response.ApiResponse;

import java.util.List;

@RestController
@RequestMapping("/api/folders")
@RequiredArgsConstructor
@Tag(name = "Recording Folders", description = "녹음 폴더 관리 API")
public class RecordingFolderController {

    private final RecordingFolderService recordingFolderService;

    @PostMapping
    @Operation(summary = "폴더 생성", description = "새로운 폴더를 생성합니다. folderName이 없으면 자동 생성")
    public ApiResponse<RecordingFolderResponseDto> createFolder(
            @RequestParam Long userId,
            @RequestParam(required = false) String folderName,
            @RequestParam(required = false) String description
    ) {
        return ApiResponse.success(recordingFolderService.createFolder(userId, folderName, description));
    }

    @GetMapping
    @Operation(summary = "유저 폴더 목록 조회", description = "특정 유저의 폴더 목록을 조회합니다.")
    ApiResponse<List<RecordingFolderResponseDto>> getUsersFolders(@RequestParam Long userId) {
        return ApiResponse.success(recordingFolderService.getUserFolders(userId));
    }

    @DeleteMapping("/{folderId}")
    @Operation(summary = "폴더 삭제", description = "특정 폴더를 삭제합니다.")
    ApiResponse<String> deleteFolder(
            @RequestParam Long userId,
            @PathVariable Long folderId
    )
    {
        recordingFolderService.deleteFolder(userId, folderId);
        return ApiResponse.success("폴더가 삭제되었습니다.");
    }
}
