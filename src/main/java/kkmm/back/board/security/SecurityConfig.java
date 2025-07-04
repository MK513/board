package kkmm.back.board.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // 1. CSRF 보호 비활성화 (POST 요청 등을 허용하기 위함)
                .csrf(csrf -> csrf.disable())

                // 2. 모든 HTTP 요청에 대해 인증 없이 접근을 허용합니다.
                .authorizeHttpRequests(authorize -> authorize
                        .anyRequest().permitAll() // <-- 이 부분이 핵심입니다!
                )

        // 3. 폼 로그인 관련 설정도 사실상 필요 없어지지만, 명시적으로 비활성화하거나 제거할 수 있습니다.
        //    하지만 명시적으로 permitAll()을 설정했으므로 생략해도 무방합니다.
        .formLogin(AbstractHttpConfigurer::disable) // 폼 로그인 비활성화
        .logout(AbstractHttpConfigurer::disable)   // 로그아웃 비활성화
        ;

        // 4. H2 Console이 iframe을 사용하면 필요할 수 있는 설정 (개발 환경에서만)
        http.headers(headers -> headers.frameOptions(frameOptions -> frameOptions.sameOrigin()));

        return http.build();
    }
}
