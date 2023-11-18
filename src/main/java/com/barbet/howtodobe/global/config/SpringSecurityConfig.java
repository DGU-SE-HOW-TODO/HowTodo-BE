package com.barbet.howtodobe.global.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.CorsConfigurer;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import javax.servlet.DispatcherType;
import java.util.Arrays;

@Configuration
@EnableWebSecurity
public class SpringSecurityConfig  {

    @Bean
    public BCryptPasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    private void corsConfiguration(CorsConfigurer cors){
        cors.configurationSource(request -> {
            CorsConfiguration configuration = new CorsConfiguration();
            configuration.setAllowedOrigins(Arrays.asList("*"));
            configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "HEAD"));
            configuration.setAllowedHeaders(Arrays.asList("Authorization", "Cache-Control", "Content-Type"));
            configuration.setAllowCredentials(true);
            UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
            source.registerCorsConfiguration("/**", configuration);
            return configuration;
        });
    }

    private void csrfConfiguration(CsrfConfigurer csrf) {
        csrf.disable();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
        http
                .cors(this::corsConfiguration)
                .csrf(this::csrfConfiguration)
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeHttpRequests(authz ->
                        authz
                                .dispatcherTypeMatchers(DispatcherType.FORWARD).permitAll()
                                .antMatchers("/member/**",
                                        "*", "/**").permitAll()
                                .anyRequest().authenticated());
        return http.build();
    }
}
