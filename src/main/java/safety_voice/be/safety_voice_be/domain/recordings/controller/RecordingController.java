package safety_voice.be.safety_voice_be.domain.recordings.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import safety_voice.be.safety_voice_be.domain.recordings.dto.RecordingDto;
import safety_voice.be.safety_voice_be.domain.recordings.service.RecordingService;
import safety_voice.be.safety_voice_be.global.exception.response.ApiResponse;

@RestController
@RequestMapping("/api/recordings")
@RequiredArgsConstructor
public class RecordingController {

    private final RecordingService recordingService;


}
