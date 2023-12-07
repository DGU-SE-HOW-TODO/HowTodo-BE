package com.barbet.howtodobe.global.config;

import com.barbet.howtodobe.global.util.JwtAuthenticationFilter;
import com.barbet.howtodobe.global.util.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.formLogin().disable()
                .httpBasic().disable()
                .csrf().disable()
                .cors().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                .antMatchers("/member/signup").permitAll()
                .antMatchers("/member/login").permitAll()
                .antMatchers("/member/**").permitAll()
                .antMatchers(HttpMethod.GET, "/home/**").permitAll()
                .antMatchers(HttpMethod.POST, "/category").permitAll()
                .antMatchers(HttpMethod.POST, "/todo/assign").permitAll()
                .antMatchers(HttpMethod.POST, "/todo/check/**").permitAll()
                .antMatchers(HttpMethod.POST, "/todo/fix/**").permitAll()
                .antMatchers(HttpMethod.PATCH, "/todo/failtag/**").permitAll()
                .antMatchers(HttpMethod.POST,"/failtag/todoCategory").permitAll()
                .antMatchers(HttpMethod.GET,"/failtag/**").permitAll()
                .antMatchers(HttpMethod.GET,"/statistic/**").permitAll()
                .antMatchers(HttpMethod.GET, "/feedback/**").permitAll()

                .anyRequest().authenticated()
                .and()
                .addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider), UsernamePasswordAuthenticationFilter.class);
    }

    @Bean
    public static PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

}
