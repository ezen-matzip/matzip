package com.ezen.matzip.config;

import com.ezen.matzip.domain.user.service.LoginSuccessHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.core.GrantedAuthorityDefaults;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final LoginSuccessHandler loginSuccessHandler;
//
//    // 생성자 주입으로 LoginSuccessHandler 받기
    public SecurityConfig(LoginSuccessHandler loginSuccessHandler) {
        this.loginSuccessHandler = loginSuccessHandler;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/", "/main/main", "/login", "/signup", "/signup/**", "/css/**", "/js/**", "/img/**", "/html/**",
                                "/fragments/**",
                                "/findpwd", "/checkUserInfo", "/resetPassword").permitAll()
                        .requestMatchers("/user/**").hasRole("USER")
                        .requestMatchers("/business/**").hasRole("BUSINESS")
                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        // 일단 테스트를 위해 모든 요청 허용 (추후 수정)
                        .anyRequest().permitAll()
                )
                .formLogin(login -> login
                        .loginPage("/login")
                        .usernameParameter("userId")
                        .passwordParameter("password")
                        .successHandler(loginSuccessHandler)
                        .failureUrl("/login?error=true")
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/")
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID")
                        .permitAll()
                )
                .sessionManagement(session -> session
                        .sessionFixation().newSession()
                        .maximumSessions(1)
                        .maxSessionsPreventsLogin(false)
                )
                .csrf(withDefaults())
                .csrf(csrf -> csrf
                        .ignoringRequestMatchers("/api/**", "/dapi/**", "/html/**"));

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        http
//                .authorizeHttpRequests(auth -> auth
//                        .requestMatchers("/", "/main/main", "/login", "/signup", "/signup/**", "/css/**", "/js/**", "/img/**").permitAll()
//                        .requestMatchers("/user/**").hasRole("USER")
//                        .requestMatchers("/business/**").hasRole("BUSINESS")
//                        .requestMatchers("/admin/**").hasRole("ADMIN")
////                        .anyRequest().authenticated()
//                                .anyRequest().permitAll()
//                )
//                .formLogin(login -> login
//                        .loginPage("/login")
//                        // 로그인 폼에서 "userId", "password"라는 필드명을 사용한다면:
//                        .usernameParameter("userId")
//                        .passwordParameter("password")
//                        .successHandler(new LoginSuccessHandler())
//                        // 로그인 실패 시 에러 파라미터를 붙여 리다이렉트 (옵션)
//                        .failureUrl("/login?error=true")
//                        .permitAll()
//                )
//                .logout(logout -> logout
//                        .logoutUrl("/logout")
//                        .logoutSuccessUrl("/")
//                        .invalidateHttpSession(true)
//                        .deleteCookies("JSESSIONID")
//                        .permitAll()
//                )
//                .sessionManagement(session -> session
//                        .sessionFixation().newSession()
//                        .maximumSessions(1)
//                        .maxSessionsPreventsLogin(false)
//                )
//                .csrf(withDefaults());
//
//        return http.build();
//    }
//
//    @Bean
//    public PasswordEncoder passwordEncoder() {
//        return new BCryptPasswordEncoder();
//    }
}


