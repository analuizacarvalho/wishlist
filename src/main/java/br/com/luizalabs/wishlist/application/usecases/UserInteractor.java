package br.com.luizalabs.wishlist.application.usecases;

import br.com.luizalabs.wishlist.application.exception.UseCaseException;
import br.com.luizalabs.wishlist.application.gateways.UserGateway;
import br.com.luizalabs.wishlist.domain.entity.User;
import br.com.luizalabs.wishlist.domain.exception.UserAlreadyExistsException;
import br.com.luizalabs.wishlist.infrastructure.adapters.MessageHelper;

public class UserInteractor {

    private final UserGateway userGateway;
    private final MessageHelper messageHelper;

    public UserInteractor(UserGateway userGateway,
                          MessageHelper messageHelper) {
        this.userGateway = userGateway;
        this.messageHelper = messageHelper;
    }

    public User createUser(User user) {
        validateEmail(user.email());
        try {
            return userGateway.createUser(user);
        } catch (Exception ex) {
            throw new UseCaseException(messageHelper.getMessage("error.save.user"));
        }
    }

    private void validateEmail(String email) {
        if(userGateway.findUserByEmail(email).isPresent()) {
            throw new UserAlreadyExistsException(messageHelper.getMessage("error.duplicate.email"));
        }
    }
}
