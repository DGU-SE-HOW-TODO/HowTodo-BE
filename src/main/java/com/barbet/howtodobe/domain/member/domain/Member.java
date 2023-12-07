package com.barbet.howtodobe.domain.member.domain;

import com.barbet.howtodobe.global.common.exception.CustomException;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.*;
import java.util.Collection;
import java.util.List;

import static com.barbet.howtodobe.global.common.exception.CustomResponseCode.NOT_EXIST_PASSWORD;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@Table(name = "MEMBER")
public class Member implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long memberId;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String nickname;

    @Column(nullable = false)
    private String password;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_USER"));
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }


    public boolean checkPassword(String plainPw, PasswordEncoder passwordEncoder) {
        return passwordEncoder.matches(plainPw, this.password);
    }

    public void encodePassword(PasswordEncoder passwordEncoder) {
        if (password != null) {
            this.password = passwordEncoder.encode(password);
        } else {
            new CustomException(NOT_EXIST_PASSWORD);
        }
    }
}
