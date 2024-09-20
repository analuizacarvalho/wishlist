package br.com.luizalabs.wishlist.application.usecases;

import br.com.luizalabs.wishlist.application.gateways.AuthenticationGateway;
import br.com.luizalabs.wishlist.application.gateways.UserGateway;
import br.com.luizalabs.wishlist.domain.entity.User;
import br.com.luizalabs.wishlist.domain.exception.UnauthorizedException;
import br.com.luizalabs.wishlist.infrastructure.adapters.MessageHelper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AuthenticationInteractorTest {

    private static final String ENCODED_PASSWORD = "encodedPassword";
    private static final String DECODED_PASSWORD = "decodedPassword";
    private static final String EMAIL = "email@gmail.com";
    private static final String TOKEN = "token";
    private static final String AUTHENTICATION_ERROR = "Erro ao autenticar";
    private static final String AUTHENTICATION_ERROR_CODE = "error.authentication";
    private static final String USER_NOT_FOUND_ERROR = "Erro ao autenticar";
    private static final String USER_NOT_FOUND_ERROR_CODE = "error.find.user";

    @InjectMocks
    AuthenticationInteractor authenticationInteractor;

    @Mock
    MessageHelper messageHelper;
    @Mock
    UserGateway userGateway;
    @Mock
    AuthenticationGateway authenticationGateway;

    @Test
    void should_login_successfully_when_valid_credentials() {
        Optional<User> user = Optional.of(mock(User.class));

        when(user.get().password()).thenReturn(ENCODED_PASSWORD);
        when(userGateway.findUserByEmail(EMAIL)).thenReturn(user);
        when(authenticationGateway.validatePassword(DECODED_PASSWORD, ENCODED_PASSWORD)).thenReturn(true);
        when(authenticationGateway.generateToken(user.get())).thenReturn(TOKEN);

        String token = authenticationInteractor.login(EMAIL, DECODED_PASSWORD);

        assertEquals(TOKEN, token);
    }

    @Test
    void should_return_unauthorized_exception_when_email_not_exists() {
        when(userGateway.findUserByEmail(EMAIL)).thenReturn(Optional.empty());
        when(messageHelper.getMessage(AUTHENTICATION_ERROR_CODE)).thenReturn(AUTHENTICATION_ERROR);
        when(messageHelper.getMessage(USER_NOT_FOUND_ERROR_CODE)).thenReturn(USER_NOT_FOUND_ERROR);
        assertThrows(UnauthorizedException.class, () -> authenticationInteractor.login(EMAIL, DECODED_PASSWORD));
    }

    @Test
    void should_return_unauthorized_exception_when_invalid_password() {
        Optional<User> user = Optional.of(mock(User.class));

        when(user.get().password()).thenReturn(ENCODED_PASSWORD);
        when(userGateway.findUserByEmail(EMAIL)).thenReturn(user);
        when(authenticationGateway.validatePassword(DECODED_PASSWORD, ENCODED_PASSWORD)).thenReturn(false);
        when(messageHelper.getMessage(AUTHENTICATION_ERROR_CODE)).thenReturn(AUTHENTICATION_ERROR);
        assertThrows(UnauthorizedException.class, () -> authenticationInteractor.login(EMAIL, DECODED_PASSWORD));
    }

    @Test
    void should_return_unauthorized_exception_when_generic_error() {
        when(userGateway.findUserByEmail(EMAIL)).thenThrow(RuntimeException.class);
        when(messageHelper.getMessage(AUTHENTICATION_ERROR_CODE)).thenReturn(AUTHENTICATION_ERROR);
        assertThrows(UnauthorizedException.class, () -> authenticationInteractor.login(EMAIL, DECODED_PASSWORD));
    }
}
