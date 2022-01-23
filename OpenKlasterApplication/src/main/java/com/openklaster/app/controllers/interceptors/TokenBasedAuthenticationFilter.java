package com.openklaster.app.controllers.interceptors;

import com.openklaster.app.model.AuthenticatedUser;
import com.openklaster.app.model.entities.user.UserEntity;
import com.openklaster.app.services.AuthService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

@Component
@AllArgsConstructor
public class TokenBasedAuthenticationFilter extends OncePerRequestFilter {

    private final AuthService authService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        Optional<String> sessionToken = Optional.ofNullable(request.getParameter("sessionToken"));
        Optional<String> apiToken = Optional.ofNullable(request.getParameter("apiToken"));

        if (sessionToken.isPresent()) {
            Optional<UserEntity> userOpt = authService.getUserAuthenticatedBySessionToken(sessionToken.get());
            if (userOpt.isPresent()) {
                authenticate(userOpt.get(), sessionToken.get(), request);
            } else {
                response.setStatus(401);
                logger.warn(String.format("Unknown session token: %s", sessionToken.get()));
                return;
            }

        } else if (apiToken.isPresent()) {
            Optional<UserEntity> userOpt = authService.getUserAuthenticatedByApiToken(apiToken.get());
            if (userOpt.isPresent()) {
                authenticate(userOpt.get(), apiToken.get(), request);
            } else {
                response.setStatus(401);
                logger.warn(String.format("Unknown api token: %s", apiToken.get()));

                return;
            }
        }

        filterChain.doFilter(request, response);
    }

    private void authenticate(UserEntity userEntity, String token, HttpServletRequest request) {
        AuthenticatedUser authentication = new AuthenticatedUser(userEntity, token);
        authentication.setAuthenticated(userEntity.isActive());
        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}
