package com.vodafone.ebuisness.security.util;

import com.vodafone.ebuisness.exception.NoAuthenticationFoundException;
import com.vodafone.ebuisness.exception.RefreshTokenNotValidException;
import org.springframework.security.core.Authentication;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface JwtTokenProvider {

    String createAccessToken(String username, List<String> roles);

    Authentication getAuthentication(String token);

    String getUsername(String token);

    String resolveToken(HttpServletRequest req);

    boolean validateToken(String token);

    String refreshToken(String email , String refreshToken) throws RefreshTokenNotValidException;

    String createNewRefreshToken(String email);

    void logout(String email, String refreshToken) throws NoAuthenticationFoundException, RefreshTokenNotValidException;
}
