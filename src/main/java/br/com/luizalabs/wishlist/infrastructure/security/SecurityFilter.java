package br.com.luizalabs.wishlist.infrastructure.security;

import br.com.luizalabs.wishlist.domain.exception.UnauthorizedException;
import br.com.luizalabs.wishlist.infrastructure.adapters.MessageHelper;
import br.com.luizalabs.wishlist.infrastructure.persistence.UserDocument;
import br.com.luizalabs.wishlist.infrastructure.persistence.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

import static java.util.Objects.nonNull;

@Component
public class SecurityFilter extends OncePerRequestFilter {

    private final TokenService tokenService;
    private final UserRepository userRepository;
    private final MessageHelper messageHelper;

    public SecurityFilter(TokenService tokenService, UserRepository userRepository, MessageHelper messageHelper) {
        this.tokenService = tokenService;
        this.userRepository = userRepository;
        this.messageHelper = messageHelper;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        var token = this.recoverToken(request);
        var login = tokenService.validateToken(token);

        if (nonNull(login)) {
            UserDocument user = userRepository.findByEmail(login).orElseThrow(() -> new UnauthorizedException(messageHelper.getMessage("error.authentication")));
            CustomUserDetails userDetails = new CustomUserDetails(user);
            var authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        filterChain.doFilter(request, response);
    }

    private String recoverToken(HttpServletRequest request){
        var authHeader = request.getHeader("Authorization");
        if(authHeader == null) return null;
        return authHeader.replace("Bearer ", "");
    }
}
