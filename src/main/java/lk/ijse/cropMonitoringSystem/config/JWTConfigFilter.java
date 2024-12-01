package lk.ijse.cropMonitoringSystem.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lk.ijse.cropMonitoringSystem.service.AuthService.JWTService;
import lk.ijse.cropMonitoringSystem.service.AuthService.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
@Configuration
@RequiredArgsConstructor

public class JWTConfigFilter extends OncePerRequestFilter { //Ensures filter executed once per request
    private final JWTService jwtService;
    private final UserService userService;
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String initToken = request.getHeader("Authorization");
        String userEmail;
        String extractedJwtToken;

        if (StringUtils.isEmpty(initToken) || !initToken.startsWith("Bearer ")){
            filterChain.doFilter(request,response);
            return;
        }
        extractedJwtToken = initToken.substring(7);
        userEmail =jwtService.
    }
}
