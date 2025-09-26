package safety_voice.be.safety_voice_be.domain.recordings.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import safety_voice.be.safety_voice_be.domain.recordings.dto.RecordingDto;
import safety_voice.be.safety_voice_be.domain.recordings.dto.RecordingRequestDto;
import safety_voice.be.safety_voice_be.domain.recordings.dto.RecordingResponseDto;
import safety_voice.be.safety_voice_be.domain.recordings.service.RecordingService;
import safety_voice.be.safety_voice_be.global.exception.response.ApiResponse;

import java.util.List;

@RestController
@RequestMapping("/api/recordings")
@RequiredArgsConstructor
@Tag(name = "Recordings", description = "녹음(파일) 업로드/조회/삭제 API")
@SecurityRequirement(name = "BearerAuth")
public class RecordingController {

    private final RecordingService recordingService;

    @PostMapping
    @Operation(
            summary = "녹음 업로드 사전등록 (Presigned PUT URL 발급)",
            description = "S3 업로드용 Presigned PUT URL을 발급하고, DB에 녹음 메타를 UPLOADING 상태로 저장합니다."
    )
    public ApiResponse<RecordingResponseDto> createRecording(
            @RequestParam("userId") Long userId,
            @RequestBody RecordingRequestDto request) {

        RecordingResponseDto responseDto = recordingService.createRecording(userId, request);
        return ApiResponse.success(responseDto);
    }

    @GetMapping
    @Operation(
            summary = "유저 녹음 목록 조회",
            description = "userId로 해당 사용자의 녹음 목록을 조회합니다."
    )
    public ApiResponse<List<RecordingResponseDto>> getUserRecordings(
            @RequestParam("userId") Long userId) {

        List<RecordingResponseDto> list = recordingService.getUserRecordings(userId);
        return ApiResponse.success(list);
    }

    @PatchMapping("/{recordingId}/complete")
    @Operation(
            summary = "업로드 완료 처리",
            description = "클라이언트가 S3 업로드 완료 후 파일 크기를 알려주면, 상태를 READY로 변경합니다."
    )
    public ApiResponse<String> markUploadComplete(
            @RequestParam("userId") Long userId,
            @PathVariable("recordingId") Long recordingId,
            @RequestParam("fileSize") long fileSize) {

        recordingService.markUploadComplete(userId, recordingId, fileSize);
        return ApiResponse.success("업로드 완료 처리되었습니다.");
    }


    @GetMapping("/{recordingId}/url")
    @Operation(
            summary = "다운로드용 Presigned GET URL 발급",
            description = "해당 녹음 파일을 다운로드할 수 있는 임시 URL을 발급합니다(기본 10분)."
    )
    public ApiResponse<String> getPresignedGetUrl(
            @RequestParam("userId") Long userId,
            @PathVariable("recordingId") Long recordingId) {

        String url = recordingService.getPresignedGetUrl(userId, recordingId);
        return ApiResponse.success(url);
    }

    @DeleteMapping("/{recordingId}")
    @Operation(
            summary = "녹음 삭제",
            description = "DB에서 녹음 메타를 삭제합니다. (옵션) S3 객체도 함께 삭제 설정 가능"
    )
    public ApiResponse<String> deleteRecording(
            @RequestParam("userId") Long userId,
            @PathVariable("recordingId") Long recordingId) {

        recordingService.deleteRecording(userId, recordingId);
        return ApiResponse.success("삭제되었습니다.");
    }

}
