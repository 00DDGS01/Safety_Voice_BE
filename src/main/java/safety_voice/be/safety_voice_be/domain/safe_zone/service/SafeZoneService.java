package safety_voice.be.safety_voice_be.domain.safe_zone.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import safety_voice.be.safety_voice_be.domain.safe_time.entity.SafeTime;
import safety_voice.be.safety_voice_be.domain.safe_time.repository.SafeTimeRepository;
import safety_voice.be.safety_voice_be.domain.safe_zone.dto.SafeZoneRequestDto;
import safety_voice.be.safety_voice_be.domain.safe_zone.dto.SafeZoneResponseDto;
import safety_voice.be.safety_voice_be.domain.safe_zone.entity.SafeZone;
import safety_voice.be.safety_voice_be.domain.safe_zone.repository.SafeZoneRepository;
import safety_voice.be.safety_voice_be.domain.user.entity.User;
import safety_voice.be.safety_voice_be.domain.user.repository.UserRepository;
import safety_voice.be.safety_voice_be.global.exception.response.CustomException;

import java.util.List;
import java.util.stream.Collectors;

import static safety_voice.be.safety_voice_be.global.exception.code.ErrorCode.USER_NOT_FOUND;

@Service
@RequiredArgsConstructor
@Transactional
public class SafeZoneService {

    private final SafeZoneRepository safeZoneRepository;
    private final SafeTimeRepository safeTimeRepository;
    private final UserRepository userRepository;

    public List<SafeZoneResponseDto> getSafeZones(Long userId) {
        return safeZoneRepository.findByUserId(userId).stream()
                .map(this::toResponseDto)
                .collect(Collectors.toList());
    }

    public void saveOrUpdateSafeZones(Long userId, List<SafeZoneRequestDto> requestDtos) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(USER_NOT_FOUND));

        // 기존 SafeZone 전체 삭제 후 새로 저장
        List<SafeZone> existingZones = safeZoneRepository.findByUserId(userId);
        safeZoneRepository.deleteAll(existingZones);

        for (SafeZoneRequestDto dto : requestDtos) {
            SafeZone zone = SafeZone.builder()
                    .safeZoneName(dto.getSafeZoneName())
                    .radius(dto.getRadius())
                    .latitude(dto.getLatitude())
                    .longitude(dto.getLongitude())
                    .user(user)
                    .build();

            SafeZone savedZone = safeZoneRepository.save(zone);

            // SafeTime 저장
            if (dto.getSafeTimes() != null) {
                for (SafeZoneRequestDto.SafeTimeDto timeDto : dto.getSafeTimes()) {
                    SafeTime safeTime = SafeTime.builder()
                            .safeZone(savedZone)
                            .user(user)
                            .startTime(timeDto.getStartTime())
                            .endTime(timeDto.getEndTime())
                            .daysActive(timeDto.getDaysActive())
                            .build();
                    safeTimeRepository.save(safeTime);
                }
            }
        }
    }

    private SafeZoneResponseDto toResponseDto(SafeZone zone) {
        return SafeZoneResponseDto.builder()
                .id(zone.getId())
                .safeZoneName(zone.getSafeZoneName())
                .latitude(zone.getLatitude())
                .longitude(zone.getLongitude())
                .radius(zone.getRadius())
                .safeTimes(zone.getSafeTimes().stream()
                        .map(t -> SafeZoneResponseDto.SafeTimeDto.builder()
                                .id(t.getId())
                                .startTime(t.getStartTime())
                                .endTime(t.getEndTime())
                                .daysActive(t.getDaysActive())
                                .build())
                        .collect(Collectors.toList()))
                .build();
    }
}