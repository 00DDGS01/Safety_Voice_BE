package safety_voice.be.safety_voice_be.domain.recordings.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import safety_voice.be.safety_voice_be.domain.recordings.dto.RecordingFolderRequestDto;
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
            @RequestParam("userId") Long userId,
            @RequestBody RecordingFolderRequestDto recordingFolderRequestDto
    ) {
        return ApiResponse.success(recordingFolderService.createFolder(userId, recordingFolderRequestDto.getFolderName(), recordingFolderRequestDto.getDescription()));
    }

    @GetMapping
    @Operation(summary = "유저 폴더 목록 조회", description = "특정 유저의 폴더 목록을 조회합니다.")
    ApiResponse<List<RecordingFolderResponseDto>> getUsersFolders(@RequestParam("userId") Long userId) {
        return ApiResponse.success(recordingFolderService.getUserFolders(userId));
    }

    @PutMapping("/{folderId}")
    @Operation(summary = "폴더 수정", description = "폴더 이름과 설명을 수정합니다.")
    public ApiResponse<RecordingFolderResponseDto> updateFolder(
            @RequestParam("userId") Long userId,
            @PathVariable Long folderId,
            @RequestBody RecordingFolderRequestDto recordingFolderRequestDto
    ) {
        return ApiResponse.success(recordingFolderService.updateFolder(userId, folderId, recordingFolderRequestDto.getFolderName(), recordingFolderRequestDto.getDescription()));
    }


    @DeleteMapping("/{folderId}")
    @Operation(summary = "폴더 삭제", description = "특정 폴더를 삭제합니다.")
    ApiResponse<String> deleteFolder(
            @RequestParam("userId") Long userId,
            @PathVariable Long folderId
    )
    {
        recordingFolderService.deleteFolder(userId, folderId);
        return ApiResponse.success("폴더가 삭제되었습니다.");
    }


    @PatchMapping("/move/{recordingId}")
    @Operation(summary = "녹음 파일 폴더 이동", description = "특정 녹음 파일을 다른 폴더로 이동합니다.")
    public ApiResponse<String> moveRecordingToFolder(
            @RequestParam("userId") Long userId,
            @PathVariable("recordingId") Long recordingId,
            @RequestParam("targetFolderId") Long targetFolderId
    ) {
        recordingFolderService.moveRecordingToFolder(userId, recordingId, targetFolderId);
        return ApiResponse.success("녹음 파일이 폴더로 이동되었습니다.");
    }

}
