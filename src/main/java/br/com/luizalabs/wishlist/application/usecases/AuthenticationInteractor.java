package br.com.luizalabs.wishlist.application.usecases;

import br.com.luizalabs.wishlist.application.gateways.AuthenticationGateway;
import br.com.luizalabs.wishlist.application.gateways.UserGateway;
import br.com.luizalabs.wishlist.domain.entity.User;
import br.com.luizalabs.wishlist.domain.exception.UnauthorizedException;
import br.com.luizalabs.wishlist.infrastructure.adapters.MessageHelper;

public class AuthenticationInteractor {

    private final UserGateway userGateway;
    private final AuthenticationGateway authenticationGateway;
    private final MessageHelper messageHelper;

    public AuthenticationInteractor(UserGateway userGateway,
                                    AuthenticationGateway authenticationGateway,
                                    MessageHelper messageHelper) {
        this.userGateway = userGateway;
        this.messageHelper = messageHelper;
        this.authenticationGateway = authenticationGateway;
    }

    public String login(String email, String password) {
        try {
            User user = userGateway.findUserByEmail(email).orElseThrow(() -> new UnauthorizedException(messageHelper.getMessage("error.find.user")));
            if (authenticationGateway.validatePassword(password, user.password())) {
                return authenticationGateway.generateToken(user);
            }
            throw new UnauthorizedException(messageHelper.getMessage("error.authentication"));
        } catch (Exception ex) {
            throw new UnauthorizedException(messageHelper.getMessage("error.authentication"));
        }
    }
}
