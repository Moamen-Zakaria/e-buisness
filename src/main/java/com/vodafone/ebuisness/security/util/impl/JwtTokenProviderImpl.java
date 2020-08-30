package com.vodafone.ebuisness.security.util.impl;


import com.vodafone.ebuisness.exception.NoAuthenticationFoundException;
import com.vodafone.ebuisness.exception.RefreshTokenNotValidException;
import com.vodafone.ebuisness.security.util.JwtTokenProvider;
import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Scope("singleton")
public class JwtTokenProviderImpl implements JwtTokenProvider {

    private Map<String, String> tokensCache = new HashMap<>();

    //    @Value("${security.jwt.token.secret-key:secret}")
    private String secretKey = "secret";
    //    @Value("${security.jwt.token.expire-length:3600000}")
    private long validityInMilliseconds = 3600000; // 1h

    @Qualifier("authServiceImpl")
    @Autowired
    private UserDetailsService userDetailsService;

    @PostConstruct
    protected void init() {
        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
    }

    @Override
    public String createAccessToken(String username, List<String> roles) {
        Claims claims = Jwts.claims().setSubject(username);
        claims.put("roles", roles);
        Date now = new Date();
        Date validity = new Date(now.getTime() + validityInMilliseconds);
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    @Override
    public Authentication getAuthentication(String token) {
        UserDetails userDetails = this.userDetailsService.loadUserByUsername(getUsername(token));
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    @Override
    public String getUsername(String token) {
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().getSubject();
    }

    @Override
    public String resolveToken(HttpServletRequest req) {
        String bearerToken = req.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7, bearerToken.length());
        }
        return null;
    }

    @Override
    public boolean validateToken(String token) {
        try {
            Jws<Claims> claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
            if (claims.getBody().getExpiration().before(new Date())) {
                return false;
            }
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    @Override
    public String refreshToken(String email, String refreshToken)
            throws RefreshTokenNotValidException {

        String cachedRefreshToken = tokensCache.get(email);

        if (cachedRefreshToken == null || !cachedRefreshToken.equals(refreshToken)) {
            throw new RefreshTokenNotValidException();
        }

        UserDetails userDetails = userDetailsService.loadUserByUsername(email);
        var listOfRoles =
                userDetails.getAuthorities()
                        .stream().map(o -> o.getAuthority()).collect(Collectors.toList());
        return createAccessToken(userDetails.getUsername(), listOfRoles);
    }

    @Override
    public String createNewRefreshToken(String email) {
        String base = "email:" + new Date().getTime();
        var refreshToken = new BCryptPasswordEncoder().encode(base);
        tokensCache.put(email, refreshToken);
        return refreshToken;
    }


    @Override
    public void logout(String email)
            throws NoAuthenticationFoundException {

        if (!tokensCache.containsKey(email)) {
            throw new NoAuthenticationFoundException();
        }
        tokensCache.remove(email);

    }

}
