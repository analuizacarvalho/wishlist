package br.com.luizalabs.wishlist.application.gateways;

import br.com.luizalabs.wishlist.domain.entity.User;

import java.util.Optional;

public interface UserGateway {
    Optional<User> findUserByEmail(String productId);
    User createUser(User user);
}
