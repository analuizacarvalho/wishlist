package br.com.luizalabs.wishlist.infrastructure.controllers.mapper;

import br.com.luizalabs.wishlist.domain.entity.User;
import br.com.luizalabs.wishlist.infrastructure.controllers.dto.RegisterRequest;
import br.com.luizalabs.wishlist.infrastructure.controllers.dto.RegisterResponse;

public class UserDTOMapper {

    public User toUser(RegisterRequest registerRequest) {
        return new User(null, registerRequest.name(), registerRequest.email(), registerRequest.password());
    }

    public RegisterResponse toRegisterResponse(User user, String token) {
        return new RegisterResponse(user.id(), user.name(), user.email(), token);
    }
}
