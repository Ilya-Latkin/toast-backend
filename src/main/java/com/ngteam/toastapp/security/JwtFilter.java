package com.ngteam.toastapp.security;

import com.ngteam.toastapp.model.User;
import com.ngteam.toastapp.repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Collections;
import java.util.Optional;
import java.util.Set;

@Component
@AllArgsConstructor
public class JwtFilter extends GenericFilterBean {

    private final JwtHelper jwtHelper;
    private final UserRepository userRepository;

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        String token = JwtHelper.getTokenFromRequest((HttpServletRequest) servletRequest);
        if (token != null && jwtHelper.validateToken(token)) {
            String email = jwtHelper.getEmailFromToken(token);
            String password = jwtHelper.getPasswordFromToken(token);
            Optional<User> optionalUser = userRepository.findByEmail(email);
            if(optionalUser.isPresent() && optionalUser.get().getPassword().equals(password)) {
                User user = optionalUser.get();
                Set<GrantedAuthority> authorities = Collections.singleton(new SimpleGrantedAuthority("ROLE_USER"));
                UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(user, null, authorities);
                SecurityContextHolder.getContext().setAuthentication(auth);
            }
        }
        filterChain.doFilter(servletRequest, servletResponse);
    }
}
