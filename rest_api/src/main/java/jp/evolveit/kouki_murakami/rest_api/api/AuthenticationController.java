package jp.evolveit.kouki_murakami.rest_api.api;

import jp.evolveit.kouki_murakami.rest_api.security.JwtProvider;
import jp.evolveit.kouki_murakami.rest_api.service.LoginUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthenticationController {
    private final AuthenticationManager authenticationManager;
    private final JwtProvider provider;
    private final LoginUserDetailsService service;

    @Autowired
    public AuthenticationController(AuthenticationManager authenticationManager, JwtProvider provider,
            LoginUserDetailsService service) {
        this.authenticationManager = authenticationManager;
        this.provider = provider;
        this.service = service;
    }

    private void authenticate(String username, String password) throws AuthenticationException {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
    }

    @PostMapping("/api/authenticate")
    public AuthResponse createAuthenticationToken(@RequestBody AuthRequest request) throws AuthenticationException {
        // 認証
        authenticate(request.getUsername(), request.getPassword());

        // トークン生成
        UserDetails userDetails = service.loadUserByUsername(request.getUsername());
        String token = provider.createToken(userDetails);
        return new AuthResponse(token);
    }
}
