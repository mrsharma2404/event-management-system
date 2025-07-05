package com.evs.ticktes.filters;

import com.evs.ticktes.domain.User;
import com.evs.ticktes.repositories.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.UUID;

@Component

public class UserProvisioningFilter extends OncePerRequestFilter {

    private final UserRepository userRepository;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if(authentication != null &&
                authentication.isAuthenticated() &&
                authentication.getPrincipal() instanceof Jwt jwt
        ) {
            UUID keyclockId =  UUID.fromString(jwt.getSubject());

            if(!userRepository.existsById(keyclockId)){
                User user = new User();
                user.setId(keyclockId);
                user.setName(jwt.getClaims().get("preferred_username").toString());
                user.setEmail(jwt.getClaims().get("preferred_email").toString());
                userRepository.save(user);
            }
        }
        filterChain.doFilter(request, response);
    }
}
