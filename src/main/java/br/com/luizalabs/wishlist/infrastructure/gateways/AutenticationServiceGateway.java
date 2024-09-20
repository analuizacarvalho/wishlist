package br.com.luizalabs.wishlist.infrastructure.gateways;

import br.com.luizalabs.wishlist.application.gateways.AuthenticationGateway;
import br.com.luizalabs.wishlist.domain.entity.User;
import br.com.luizalabs.wishlist.infrastructure.security.TokenService;
import org.springframework.security.crypto.password.PasswordEncoder;

public class AutenticationServiceGateway implements AuthenticationGateway {

    private final PasswordEncoder passwordEncoder;
    private final TokenService tokenService;

    public AutenticationServiceGateway(PasswordEncoder passwordEncoder,
                                       TokenService tokenService) {
        this.passwordEncoder = passwordEncoder;
        this.tokenService = tokenService;
    }

    @Override
    public boolean validatePassword(String requestPassword, String userPassword) {
        return passwordEncoder.matches(requestPassword, userPassword);
    }

    @Override
    public String generateToken(User user) {
        return tokenService.generateToken(user);
    }
}
