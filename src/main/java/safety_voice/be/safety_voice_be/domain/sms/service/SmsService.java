package safety_voice.be.safety_voice_be.domain.sms.service;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import safety_voice.be.safety_voice_be.domain.emergency_contact.entity.EmergencyContact;
import safety_voice.be.safety_voice_be.domain.sms.dto.EmergencySmsRequestDto;
import safety_voice.be.safety_voice_be.domain.sms.entity.SmsLog;
import safety_voice.be.safety_voice_be.domain.sms.repository.SmsLogRepository;
import safety_voice.be.safety_voice_be.domain.user.entity.User;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class SmsService {

    private final SmsLogRepository smsLogRepository;

    @Value("${twilio.account-sid}")
    private String accountSid;

    @Value("${twilio.auth-token}")
    private String authToken;

    @Value("${twilio.from-number}")  // Twilio에서 구매한 발신번호
    private String fromNumber;

    public SmsService(SmsLogRepository smsLogRepository) {
        this.smsLogRepository = smsLogRepository;
    }

    @PostConstruct
    public void init() {
        Twilio.init(accountSid, authToken);
    }

    // 보호자/지인 전체 발송
    public void sendEmergencyAlerts(User user, EmergencySmsRequestDto requestDto) {
        String text = String.format(
                "🚨 [긴급 알림 - 안전한 목소리] 🚨\n%s님이 위험 신호를 보냈습니다.\n위치: https://maps.google.com/?q=%f,%f\n즉시 확인 부탁드립니다.",
                user.getNickname(),
                requestDto.getLatitude(),
                requestDto.getLongitude()
        );

        List<EmergencyContact> contacts = user.getUserSetting().getEmergencyContacts();

        for (EmergencyContact contact : contacts) {
            sendOneSms(user, contact.getPhoneNumber(), text);
        }
    }

    // 단일 발송
    public void sendOneSms(User user, String to, String text) {
        SmsLog log = null;
        try {
            Message response =
                    Message.creator(
                            new PhoneNumber(to),   // 수신자
                            new PhoneNumber(fromNumber), // 발신자 (Twilio 번호)
                            text
                    ).create();

            log = SmsLog.builder()
                    .user(user)
                    .phoneNumber(to)
                    .message(text)
                    .status(SmsLog.SmsStatus.SENT)
                    .providerMessageId(response.getSid()) // Twilio 고유 SID
                    .sentAt(LocalDateTime.now())
                    .build();

        } catch (Exception e) {
            log = SmsLog.builder()
                    .user(user)
                    .phoneNumber(to)
                    .message(text)
                    .status(SmsLog.SmsStatus.FAILED)
                    .providerMessageId(null)
                    .sentAt(LocalDateTime.now())
                    .build();

            throw e; // 로그 저장 후 예외 다시 던지기
        } finally {
            smsLogRepository.save(log);
        }
    }
}