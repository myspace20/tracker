package com.task.tracker.config;

import com.task.tracker.controllers.OAuth2AuthenticationSuccessHandler;
import com.task.tracker.exceptions.ApplicationAccessDeniedHandler;
import com.task.tracker.exceptions.ApplicationAuthenticationEntryPoint;
import com.task.tracker.filters.JWTFilter;
import com.task.tracker.services.CustomOAuth2UserServiceImpl;
import com.task.tracker.services.UserDetailsServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;


import java.util.List;

import static org.springframework.security.config.Customizer.withDefaults;

@EnableWebSecurity
@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    private final UserDetailsServiceImpl userDetailsService;
    private final JWTFilter jwtFilter;
    private final CustomOAuth2UserServiceImpl customOAuth2UserService;
    private final OAuth2AuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler;
    private final ApplicationAccessDeniedHandler applicationAccessDeniedHandler;
    private final ApplicationAuthenticationEntryPoint applicationAuthenticationEntryPoint;

    public SecurityConfig(UserDetailsServiceImpl userDetailsService, JWTFilter jwtFilter, CustomOAuth2UserServiceImpl customOAuth2UserService, OAuth2AuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler, AccessDeniedHandler accessDeniedHandler, ApplicationAccessDeniedHandler applicationAccessDeniedHandler, ApplicationAuthenticationEntryPoint applicationAuthenticationEntryPoint) {
        this.userDetailsService = userDetailsService;
        this.jwtFilter = jwtFilter;
        this.customOAuth2UserService = customOAuth2UserService;
        this.oAuth2AuthenticationSuccessHandler = oAuth2AuthenticationSuccessHandler;
        this.applicationAccessDeniedHandler = applicationAccessDeniedHandler;
        this.applicationAuthenticationEntryPoint = applicationAuthenticationEntryPoint;
    }


    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }



    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOriginPatterns(List.of("https://example.com"));
        config.setAllowedMethods(List.of("GET", "POST", "OPTIONS", "PUT", "DELETE"));
        config.setAllowedHeaders(List.of("Authorization", "Content-Type", "X-Requested-With"));
        config.setExposedHeaders(List.of("Authorization"));
        config.setAllowCredentials(true);
        config.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }


    @Profile("dev")
    @Bean
    public SecurityFilterChain devSecurityChain(HttpSecurity http) throws Exception {
        return http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/", "/login", "/login/**", "/error", "/oauth2/**"
                        ).permitAll()
                        .requestMatchers(
                                "/actuator/**"
                        ).permitAll()
                        .requestMatchers("/swagger-ui/**", "/v3/api-docs/**", "/h2-console/**").hasRole("ADMIN")
                        .requestMatchers("/api/v1/auth/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/v1/projects/**").hasAnyRole("MANAGER", "ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/v1/projects/**").hasAnyRole("MANAGER", "ADMIN")
                        .requestMatchers("/api/v1/admin/**").hasRole("ADMIN")
                        .requestMatchers("/api/v1/users/me").authenticated()
                        .anyRequest().authenticated()
                )
                .oauth2Login(oauth -> oauth
                        .userInfoEndpoint(userInfo -> userInfo
                                .userService(customOAuth2UserService)
                        )
                        .defaultSuccessUrl("/auth/oauth2/success")
                        .successHandler(oAuth2AuthenticationSuccessHandler)
                )
                .csrf(AbstractHttpConfigurer::disable)
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling(customizer -> customizer
                        .accessDeniedHandler(applicationAccessDeniedHandler)
                        .authenticationEntryPoint(applicationAuthenticationEntryPoint)
                )
                .build();
    }



    @Profile("prod")
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .cors(Customizer.withDefaults())
                .securityMatcher("/api/**")
                              .authorizeHttpRequests(request -> request
                        .requestMatchers("/api/v1/auth/**").permitAll()
                                      .requestMatchers("/swagger-ui/**", "/v3/api-docs/**").denyAll()
                                      .requestMatchers(HttpMethod.POST, "/api/v1/projects/**").hasAnyRole("MANAGER", "ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/v1/projects/**").hasAnyRole("MANAGER", "ADMIN")
                        .requestMatchers("/api/v1/admin/**").hasRole("ADMIN")
                        .requestMatchers("/api/v1/users/me").authenticated()
                        .anyRequest().authenticated()
                )
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .csrf(AbstractHttpConfigurer::disable)
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling(
                        customizer->
                                customizer.accessDeniedHandler(applicationAccessDeniedHandler)
                                        .authenticationEntryPoint(applicationAuthenticationEntryPoint)
                )
                .redirectToHttps(withDefaults())
                .build();
    }

    @Profile("prod")
    @Bean
    public SecurityFilterChain oauth2LoginFilter(HttpSecurity http) throws Exception {
        http
                .cors(Customizer.withDefaults())
                .securityMatcher((request) -> !request.getRequestURI().startsWith("/api"))
                .authorizeHttpRequests(request -> request
                        .anyRequest().authenticated()
                )
                .oauth2Login(oauth -> oauth
                        .userInfoEndpoint(userInfo -> userInfo
                                .userService(customOAuth2UserService)
                        )
                        .defaultSuccessUrl("/auth/oauth2/success")
                        .successHandler(oAuth2AuthenticationSuccessHandler)
                ).exceptionHandling(customizer -> customizer.accessDeniedPage("/no-access"))
			.redirectToHttps(withDefaults());

        return http.build();
    }




    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }


}



