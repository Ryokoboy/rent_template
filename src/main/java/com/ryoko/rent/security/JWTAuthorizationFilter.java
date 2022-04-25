package com.ryoko.rent.security;

import com.ryoko.rent.enviroment.JWTEnvironmentBuilder;
import com.ryoko.rent.service.serviceInterface.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.http.HttpStatus.UNAUTHORIZED;

@Component
public class JWTAuthorizationFilter extends OncePerRequestFilter {

    private final JWTEnvironmentBuilder jwtEnvironmentBuilder;
    private final JWTTokenProvider jwtTokenProvider;
    private final UserService userService;

    @Autowired
    public JWTAuthorizationFilter(JWTEnvironmentBuilder jwtEnvironmentBuilder, JWTTokenProvider jwtTokenProvider, UserService userService) {
        this.jwtEnvironmentBuilder = jwtEnvironmentBuilder;
        this.jwtTokenProvider = jwtTokenProvider;
        this.userService = userService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (request.getMethod().equalsIgnoreCase(HttpMethod.OPTIONS.name())) {
            response.setStatus(HttpStatus.OK.value());
        } else {
            String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
            if (authorizationHeader == null || !authorizationHeader.startsWith(jwtEnvironmentBuilder.getTOKEN_PREFIX())) {
                filterChain.doFilter(request, response);
                return;
            }
            String token = authorizationHeader.substring(jwtEnvironmentBuilder.getTOKEN_PREFIX().length());
            String username;
            UserPrincipal userPrincipal;
            try {
                username = jwtTokenProvider.getSubject(token);
                userPrincipal = new UserPrincipal(this.userService.getByEmailThrowException(username));
                if (!userPrincipal.isAccountNonLocked()) {
                    response.setStatus(UNAUTHORIZED.value());
                    return;
                }
                if (jwtTokenProvider.isTokenValid(username, token, request)) {
                    List<GrantedAuthority> authorities = new ArrayList<>(userPrincipal.getAuthorities());
                    Authentication authentication = jwtTokenProvider.getAuthentication(username, authorities, request);
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                } else {
                    SecurityContextHolder.clearContext();
                }
            } catch (Exception e) {
                response.setStatus(UNAUTHORIZED.value());
                return;
            }
        }
        filterChain.doFilter(request, response);
    }
}
