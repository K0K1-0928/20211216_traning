package jp.evolveit.kouki_murakami.rest_api.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

public class JwtAuthenticationFileter extends UsernamePasswordAuthenticationFilter {
    private final JwtProvider provider;

    @Autowired
    public JwtAuthenticationFileter(JwtProvider provider) {
        this.provider = provider;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        String token = resolveToken(HttpServletRequest.class.cast(request));
        if (token == null || !provider.validateToken(token)) {
            chain.doFilter(request, response);
            return;
        }
        Authentication auth = provider.getAuthentication(token);
        SecurityContextHolder.getContext().setAuthentication(auth);
        chain.doFilter(request, response);
    }

    private String resolveToken(HttpServletRequest request) {
        String authorization = request.getHeader("Authorization");
        return (authorization != null && authorization.startsWith("Bearer "))
                ? authorization.substring(7)
                : null;
    }
}
