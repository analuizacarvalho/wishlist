package br.com.luizalabs.wishlist.application.gateways;

import br.com.luizalabs.wishlist.domain.entity.User;

public interface AuthenticationGateway {
    boolean validatePassword(String requestPassword, String userPassword);
    String generateToken(User user);
}
