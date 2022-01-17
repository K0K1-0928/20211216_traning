package jp.evolveit.kouki_murakami.rest_api.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jp.evolveit.kouki_murakami.rest_api.service.LoginUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.time.ZonedDateTime;
import java.util.Date;

@Component
public class JwtProvider {
    /** トークンの有効期限(秒) */
    private static final long JWT_TOKEN_VALIDITY = 1_800L;

    private final LoginUserDetailsService loginUserDetailsService;
    private final JwtParser jwtParser;
    private final JwtBuilder jwtBuilder;

    @Autowired
    public JwtProvider(LoginUserDetailsService loginUserDetailsService, @Value("${jwt.secret}") String secret) {
        this.loginUserDetailsService = loginUserDetailsService;

        /* 署名キーの作成 */
        byte[] keyBytes = secret.getBytes(StandardCharsets.UTF_8);
        Key key = Keys.hmacShaKeyFor(keyBytes);

        /* 毎回生成しなくて済むように、フィールドに持たせる */
        jwtParser = Jwts.parserBuilder().setSigningKey(key).build();
        jwtBuilder = Jwts.builder().signWith(key, SignatureAlgorithm.HS256);
    }

    public String createToken(UserDetails userDetails) {
        ZonedDateTime now = ZonedDateTime.now();

        return jwtBuilder.setSubject(userDetails.getUsername()).setIssuedAt(Date.from(now.toInstant()))
                .setExpiration(Date.from(now.plusSeconds(JWT_TOKEN_VALIDITY).toInstant())).compact();
    }

    public boolean validateToken(String token) {
        try {
            Claims claims = jwtParser.parseClaimsJws(token).getBody();
            return !isTokenExpired(claims);
        } catch (Exception e) {
            return false;
        }
    }

    private boolean isTokenExpired(Claims claims) {
        return claims.getExpiration().before(new Date());
    }

    public Authentication getAuthentication(String token) {
        Claims claims = jwtParser.parseClaimsJws(token).getBody();
        UserDetails principal = loginUserDetailsService.loadUserByUsername(claims.getSubject());
        return new UsernamePasswordAuthenticationToken(principal, "", principal.getAuthorities());
    }
}
