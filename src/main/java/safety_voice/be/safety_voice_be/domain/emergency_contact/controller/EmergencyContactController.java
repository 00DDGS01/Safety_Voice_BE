package safety_voice.be.safety_voice_be.domain.emergency_contact.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import safety_voice.be.safety_voice_be.domain.emergency_contact.dto.EmergencyContactRequestDTO;
import safety_voice.be.safety_voice_be.domain.emergency_contact.dto.EmergencyContactResponseDTO;
import safety_voice.be.safety_voice_be.domain.emergency_contact.service.EmergencyContactService;
import safety_voice.be.safety_voice_be.global.exception.response.ApiResponse;

import java.util.List;

@RestController
@RequestMapping("/api/emergency-contacts")
@RequiredArgsConstructor
public class EmergencyContactController {

    private final EmergencyContactService emergencyContactService;

    @PostMapping("/{userId}")
    public ApiResponse<String> addContact(@PathVariable Long userId, @RequestBody EmergencyContactRequestDTO emergencyContactRequestDTO) {
        emergencyContactService.addContact(userId, emergencyContactRequestDTO);
        return ApiResponse.success("비상 연락처가 추가되었습니다.");
    }

    @GetMapping("/{userId}")
    public ApiResponse<List<EmergencyContactResponseDTO>> getContacts(@PathVariable Long userId) {
        return ApiResponse.success(emergencyContactService.getContacts(userId));
    }
}
