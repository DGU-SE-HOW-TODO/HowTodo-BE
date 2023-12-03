package com.barbet.howtodobe.global.eunse;

import com.barbet.howtodobe.domain.member.dao.MemberRepository;
import com.barbet.howtodobe.domain.member.domain.Member;
import com.barbet.howtodobe.global.exception.CustomErrorCode;
import com.barbet.howtodobe.global.exception.CustomException;
import io.jsonwebtoken.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.util.Base64;
import java.util.Date;
import java.util.List;

import static com.barbet.howtodobe.global.exception.CustomErrorCode.*;

@Slf4j
@RequiredArgsConstructor
@Component
public class JwtTokenProvider {

    @Value("HOWTODOBESECRETKEY2023112020011116202210082002091620231120200111162022100820020916202311202001111620221008200209162023112020011116202210082002091620231120200111162022100820020916")
    private String secretKey;

    //토큰 유효시간 7일로 설정
    private Long tokenVaildTime = 1440 * 60 * 7 * 1000L;

    private final MemberRepository memberRepository;
    private final CustomMemberDetailService customMemberDetailService;

    @PostConstruct
    protected void init() {
        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
    }

    public String createToken(String userPk) {
        Claims claims = Jwts.claims().setSubject(userPk);
        Date now = new Date();
        log.info(userPk);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + tokenVaildTime))
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    public Authentication getAuthentication(String token) {
        String userPk = getUserPk(token);
        UserDetails userDetails = customMemberDetailService.loadUserByUsername(userPk);
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    public String getUserPk(String token) {
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().getSubject();
    }

    public String resolveToken(HttpServletRequest request) {
        return request.getHeader("X-AUTH-TOKEN");
    }

    public boolean validateToken(String jwtToken) {
        try {
            Jws<Claims> claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(jwtToken);
            return !claims.getBody().getExpiration().before(new Date());
        } catch (ExpiredJwtException expiredJwtException) {
            throw new CustomException(TOKEN_EXPIRED);
        } catch (MalformedJwtException malformedJwtException) {
            throw new CustomException(INVALID_TOKEN_FORMAT);
        } catch (Exception e) {
            throw new CustomException(TOKEN_VALIDATION_ERROR);
        }
    }

    public Long getUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof Member) {
            Member member = (Member) authentication.getPrincipal();
            return member.getMemberId();
        }
        return null;
    }

    public Long getUserIdByServlet(HttpServletRequest request) {
        String token = resolveToken(request);
        if (token != null && validateToken(token)) {
            String userPk = getUserPk(token);
            Member memeber = memberRepository.findByEmail(userPk)
                    .orElseThrow(() -> new CustomException(CustomErrorCode.USER_NOT_FOUND));
            return memeber.getMemberId();
        }
        return null;
    }

}