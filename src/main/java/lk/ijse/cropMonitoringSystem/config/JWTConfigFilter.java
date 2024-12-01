package lk.ijse.cropMonitoringSystem.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lk.ijse.cropMonitoringSystem.service.AuthService.JWTService;
import lk.ijse.cropMonitoringSystem.service.AuthService.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
@Configuration
@RequiredArgsConstructor

public class JWTConfigFilter extends OncePerRequestFilter { //ALL request filter executed once per request
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
        userEmail =jwtService.extractUserName(extractedJwtToken);

        // user email
        if(org.apache.commons.lang3.StringUtils.isNotEmpty(userEmail) &&
                SecurityContextHolder.getContext().getAuthentication() == null) {
            var userDetails =
                    userService.userDetailsService().loadUserByUsername(userEmail);
            if(jwtService.validateToken(extractedJwtToken, userDetails)) {
                //add user to the security context
                SecurityContext emptyContext =
                        SecurityContextHolder.createEmptyContext();
                var authToken =
                        new UsernamePasswordAuthenticationToken(
                                userDetails,null,userDetails.getAuthorities());
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                emptyContext.setAuthentication(authToken);
                SecurityContextHolder.setContext(emptyContext);
            }
        }
        filterChain.doFilter(request, response);
    }
}
