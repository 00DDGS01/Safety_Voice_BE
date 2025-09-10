package safety_voice.be.safety_voice_be.domain.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import safety_voice.be.safety_voice_be.domain.user.entity.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    // logindId로 유저 찾기 (로그인 시 사용)
    Optional<User> findByLoginId(String loginId);

    // loginId 중복 체크 (회원가입 시 사용)
    boolean existsByLoginId(String loginId);

    // 이메일 중복 체크 (회원가입 시 사용)
    boolean existsByEmail(String email);
}