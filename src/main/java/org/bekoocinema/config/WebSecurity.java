package org.bekoocinema.config;

import lombok.RequiredArgsConstructor;
import org.bekoocinema.constant.EndPointConstant;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class WebSecurity {

    final AuthenticationProvider authenticationProvider;
    final JwtAuthenticationFilter jwtAuthenticationFilter;

    final String[] listUnAuthenticate={
            "/sign-in"
            , "/otp-sign-in"
            , "/register"
            , "/v3/api-docs/**"
            , "/swagger-ui/**"
            , "/swagger-ui.html"
            , "/forgot-password"
            , "/verify-forgot-password/**"
            , "/reset-password"
            , "/payment-result"
            , "/socket/seat/**"
            , EndPointConstant.PUBLIC + "/**"
    };

    final String[] adminEndpoints = {
        "/cinema",
        "/cinema/**",
        "/genre",
        "/genre/**",
        "/movie",
        "/movie/**",
        "/room",
        "/room/**",
        "/showtime",
        "/reset-seat/**",
    };

    final String[] userEndpoints = {
        "/booking",
        "/token",
        "/verify-sign-in/**",
        "/verify-forgot-password/**",
        "/reset-password",
        "/logout"
    };

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors(httpSecurityCorsConfigurer -> corsFilter())
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(
                        configure ->
                                configure
                                        .requestMatchers(HttpMethod.POST, "/user")
                                            .permitAll()
                                        .requestMatchers(listUnAuthenticate)
                                            .permitAll()
                                        .requestMatchers(adminEndpoints)
                                            .hasRole("ADMIN")
                                        .requestMatchers(userEndpoints)
                                            .hasAnyRole("USER", "ADMIN")
                                        .anyRequest()
                                            .authenticated()
                )
                .logout(AbstractHttpConfigurer::disable) // Vô hiệu hóa default logout của Spring Security
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthenticationFilter , UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    CorsFilter corsFilter(){
        CorsConfiguration corsConfiguration = new CorsConfiguration() ;
        corsConfiguration.addAllowedMethod("*");
        corsConfiguration.addAllowedOrigin("*");
        corsConfiguration.addAllowedHeader("*");

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource() ;
        source.registerCorsConfiguration("/**" , corsConfiguration);
        return new CorsFilter(source) ;
    }
}
