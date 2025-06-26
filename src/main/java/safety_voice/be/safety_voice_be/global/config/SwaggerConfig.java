package safety_voice.be.safety_voice_be.global.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("안전한 목소리")
                        .description("백엔드 API 문서")
                        .version("v1")
                        .contact(new Contact()
                                .name("DDGS")
                                .email("ksh000624@gmail.com")
                        )
                )

                // JWT 토큰 인증 방식
                .addSecurityItem(new SecurityRequirement().addList("BearerAuth"))

                .components(new Components().addSecuritySchemes("BearerAuth",
                        new SecurityScheme()
                                .type(SecurityScheme.Type.HTTP) // HTTP 방식 인증
                                .scheme("bearer")               // 'Bearer '인증 방식
                                .bearerFormat("JWT")            // JWT 형식 사용 명시
                        )
                );
    }
}
