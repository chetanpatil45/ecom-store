package com.ecom.api.security;

import com.auth0.jwt.exceptions.JWTDecodeException;
import com.ecom.entity.User;
import com.ecom.repository.UserRepository;
import com.ecom.service.JWTService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.jspecify.annotations.Nullable;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.SimpMessageType;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component
public class JWTRequestFilter extends OncePerRequestFilter implements ChannelInterceptor {

    private final JWTService jwtService;
    private final UserRepository repository;

    public JWTRequestFilter(JWTService jwtService, UserRepository repository) {
        this.jwtService = jwtService;
        this.repository = repository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String tokenHeader = request.getHeader("Authorization");
        UsernamePasswordAuthenticationToken token = checkToken(tokenHeader);
        if (token!=null) {
            token.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        }

        String path = request.getRequestURI();

        if (path.startsWith("/websocket")) {
            filterChain.doFilter(request, response);
            return;
        }

        filterChain.doFilter(request, response);
    }

    private UsernamePasswordAuthenticationToken checkToken(String token) {
        if (token!=null && token.startsWith("Bearer ")) {
            token = token.substring(7);
            try {
                String username = jwtService.getUsername(token);
                Optional<User> opUser = repository.findByUsernameIgnoreCase(username);
                if (opUser.isPresent()) {
                    User user = opUser.get();
                    if (user.getEmailVerified()) {
                        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(user, null, new ArrayList<>());
                        SecurityContextHolder.getContext().setAuthentication(authentication);
                        return authentication;
                    }
                    //authentication object
                }
            } catch (JWTDecodeException e) {

            }
        }

        SecurityContextHolder.getContext().setAuthentication(null);
        return null;
    }

    // TODO: Limit this to only CONNECT messages.
    @Override
    public @Nullable Message<?> preSend(Message<?> message, MessageChannel channel) {
        if (message.getHeaders().get("simpMessageType").equals(SimpMessageType.SUBSCRIBE)) {
            Map nativeHeaders = (Map) message.getHeaders().get("nativeHeaders");
            if (nativeHeaders!= null) {
                List authTokenList = (List) nativeHeaders.get("Authorization");
                if (authTokenList != null) {
                    String tokenHeader = (String) authTokenList.get(0);
                    checkToken(tokenHeader);
                }
            }
        }
        return message;
    }
}
