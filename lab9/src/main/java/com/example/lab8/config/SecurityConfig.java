package com.example.lab8.config;

import com.example.lab8.service.CustomerService;
import com.example.lab8.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.SessionManagementConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationEntryPoint;
import org.springframework.security.web.session.HttpSessionEventPublisher;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final CustomerService customerService;
    private final JwtUtil jwtUtil;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return configureHttpSecurity(http).build();
    }

    private HttpSecurity configureHttpSecurity(HttpSecurity http) throws Exception {
        return http
                .csrf(this::disableCsrf)
                .authorizeHttpRequests(this::configureAuthorization)
                .httpBasic(this::configureHttpBasic)
                .sessionManagement(this::configureSessionManagement)
                .addFilterBefore(new JwtAuthenticationFilter(jwtUtil, customerService),
                        UsernamePasswordAuthenticationFilter.class);
    }

    private void disableCsrf(org.springframework.security.config.annotation.web.configurers.CsrfConfigurer<HttpSecurity> csrf) {
        csrf.disable();
    }

    private void configureAuthorization(org.springframework.security.config.annotation.web.configurers.AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry auth) {
        auth
                .requestMatchers("/api/auth/**").permitAll()
                .requestMatchers("/api/orders/**").hasAnyRole("USER", "ADMIN")
                .requestMatchers("/api/admin/**").hasRole("ADMIN")
                .anyRequest().authenticated();
    }

    private void configureHttpBasic(org.springframework.security.config.annotation.web.configurers.HttpBasicConfigurer<HttpSecurity> httpBasic) {
        httpBasic.authenticationEntryPoint(basicAuthenticationEntryPoint());
    }

    private void configureSessionManagement(SessionManagementConfigurer<HttpSecurity> session) {
        session
                .sessionFixation().migrateSession()
                .maximumSessions(1)
                .maxSessionsPreventsLogin(false)
                .expiredUrl("/api/auth/register");
    }

    @Bean
    public BasicAuthenticationEntryPoint basicAuthenticationEntryPoint() {
        BasicAuthenticationEntryPoint entryPoint = new BasicAuthenticationEntryPoint();
        entryPoint.setRealmName("Realm");
        return entryPoint;
    }

    @Bean
    public HttpSessionEventPublisher httpSessionEventPublisher() {
        return new HttpSessionEventPublisher();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
}