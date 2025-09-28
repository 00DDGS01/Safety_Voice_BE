package safety_voice.be.safety_voice_be.domain.sms.service;

import net.nurigo.sdk.NurigoApp;
import net.nurigo.sdk.message.request.SingleMessageSendingRequest;
import net.nurigo.sdk.message.response.SingleMessageSentResponse;
import net.nurigo.sdk.message.service.DefaultMessageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import safety_voice.be.safety_voice_be.domain.emergency_contact.entity.EmergencyContact;
import safety_voice.be.safety_voice_be.domain.sms.entity.SmsLog;
import net.nurigo.sdk.message.model.Message;
import safety_voice.be.safety_voice_be.domain.sms.repository.SmsLogRepository;
import safety_voice.be.safety_voice_be.domain.user.entity.User;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class SmsService {

    private final SmsLogRepository smsLogRepository;
    private final DefaultMessageService messageService;

    public SmsService(
            SmsLogRepository smsLogRepository,
            @Value("${coolsms.api-key}") String apiKey,
            @Value("${coolsms.api-secret}") String apiSecret
    ) {
        this.smsLogRepository = smsLogRepository;
        this.messageService = NurigoApp.INSTANCE.initialize(apiKey, apiSecret, "https://api.coolsms.co.kr");
    }

    // 보호자/지인 전체 발송
    public void sendEmergencyAlerts(User user) {
        String text = user.getNickname() + "님이 위험에 처했어요!";

        List<EmergencyContact> contacts = user.getUserSetting().getEmergencyContacts();

        for (EmergencyContact contact : contacts) {
            sendOneSms(user, contact.getPhoneNumber(), text);
        }
    }

    // 단일 메시지 발송
    public SingleMessageSentResponse sendOneSms(User user, String to, String text) {

        Message message = new Message();
        message.setFrom("01036701995"); // 반드시 사전 등록된 발신번호
        message.setTo(to);
        message.setText(text);

        try {
            SingleMessageSentResponse response =
                    messageService.sendOne(new SingleMessageSendingRequest(message));

            System.out.println("CoolSMS Response: " + response);

            // 성공/실패 로그 저장
            SmsLog.SmsStatus status = "2000".equals(response.getStatusCode())
                    ? SmsLog.SmsStatus.SENT
                    : SmsLog.SmsStatus.FAILED;

            SmsLog log = SmsLog.builder()
                    .user(user)
                    .phoneNumber(to)
                    .message(text)
                    .status(status)
                    .providerMessageId(response.getMessageId())
                    .sentAt(LocalDateTime.now())
                    .build();

            // user 가 있을 때만 세팅
            if (user != null) {
                log.setUser(user);
                smsLogRepository.save(log);
            }
            smsLogRepository.save(log);

            return response;
        } catch (Exception e) {
            System.err.println("CoolSMS Error: " + e.getMessage());
            e.printStackTrace(); // 콘솔에 전체 스택 출력
            throw e; // 일단 터뜨려서 원인 확인
        }
    }
}
