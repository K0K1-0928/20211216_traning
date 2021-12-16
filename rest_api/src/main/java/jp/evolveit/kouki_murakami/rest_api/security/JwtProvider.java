package jp.evolveit.kouki_murakami.rest_api.security;

import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jp.evolveit.kouki_murakami.rest_api.service.LoginUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.time.ZonedDateTime;
import java.util.Date;

@Component
public class JwtProvider {
    /** トークンの有効期限(秒) */
    private static final long JWT_TOKEN_VALIDITY = 1_800L;

    /** 署名用の共通鍵 */
    @Value("${jwt.secret}")
    private String secret;

    private final LoginUserDetailsService loginUserDetailsService;
    private final JwtParser jwtParser;
    private final JwtBuilder jwtBuilder;

    @Autowired
    public JwtProvider(LoginUserDetailsService loginUserDetailsService, @Value("${jwt.secret}") String secret) {
        this.loginUserDetailsService = loginUserDetailsService;
        this.secret = secret;

        /* 署名キーの作成 */
        byte[] keyBytes = Decoders.BASE64.decode(secret);
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
}
