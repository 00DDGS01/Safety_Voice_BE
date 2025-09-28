package safety_voice.be.safety_voice_be.domain.sms.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import net.nurigo.sdk.message.response.SingleMessageSentResponse;
import org.springframework.web.bind.annotation.*;
import safety_voice.be.safety_voice_be.domain.sms.dto.EmergencySmsRequestDto;
import safety_voice.be.safety_voice_be.domain.sms.service.SmsService;
import safety_voice.be.safety_voice_be.domain.user.entity.User;
import safety_voice.be.safety_voice_be.domain.user.repository.UserRepository;
import safety_voice.be.safety_voice_be.global.exception.code.ErrorCode;
import safety_voice.be.safety_voice_be.global.exception.response.ApiResponse;
import safety_voice.be.safety_voice_be.global.exception.response.CustomException;

@RestController
@RequestMapping("/api/sms")
@RequiredArgsConstructor
@Tag(name = "SMS", description = "긴급 상황 SMS 발송 API")
public class SmsController {
    private final SmsService smsService;
    private final UserRepository userRepository;

    @PostMapping("/emergency")
    @Operation(summary = "긴급 상황 SMS 발송", description = "EmergencyContact에 등록된 번호로 긴급 메시지를 발송합니다.")
    public ApiResponse<String> sendEmergencySms(
            @RequestParam("userId") Long userId,
            @RequestBody EmergencySmsRequestDto requestDto
    ) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        String message = String.format("%s님이 위험에 처했어요!", user.getNickname());

        smsService.sendEmergencyAlerts(user, requestDto);

        return ApiResponse.success("긴급 상황 알림 메시지를 발송했습니다.");
    }

    @PostMapping("/send-one")
    @Operation(summary = "단일 SMS 발송 (테스트)", description = "지정된 번호로 단일 테스트 메시지를 보냅니다.")
    public SingleMessageSentResponse sendOne(
            @RequestParam("userId") Long userId,
            @RequestParam("to") String to
    ) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        String text = "안전한 목소리 테스트 메시지!";

        return smsService.sendOneSms(user, to, text);
    }}
