package safety_voice.be.safety_voice_be.global.Security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

@Component
public class JwtUtil {

    private final Key key;
    private final long expirationMs;

    public JwtUtil(@Value("${jwt.secret}") String secret,
                   @Value("${jwt.expiration:86400000}") long expirationMs) {
        this.key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        this.expirationMs = expirationMs;
    }

    // 토큰 생성
    public String generateToken(String loginId) {
        return Jwts.builder()
                .setSubject(loginId)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expirationMs))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    // 토큰에서 사용자 ID 추출
    public String getLoginIdFromToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public Claims getAllClaimsFromToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public String getUserIdFromToken(String token) {
        return getAllClaimsFromToken(token).getSubject();
    }

    // 토큰 유효성 검사
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (ExpiredJwtException e) {
            System.out.println("⚠️ JWT 만료됨: " + e.getMessage());
        } catch (io.jsonwebtoken.SignatureException e) {
            System.out.println("🚨 JWT 서명 불일치: " + e.getMessage());
        } catch (JwtException e) {
            System.out.println("❌ JWT 파싱 오류: " + e.getMessage());
        }
        return false;
    }
}
