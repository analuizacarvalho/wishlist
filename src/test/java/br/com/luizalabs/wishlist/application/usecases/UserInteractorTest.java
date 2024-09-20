package br.com.luizalabs.wishlist.application.usecases;

import br.com.luizalabs.wishlist.application.exception.UseCaseException;
import br.com.luizalabs.wishlist.application.gateways.UserGateway;
import br.com.luizalabs.wishlist.domain.entity.User;
import br.com.luizalabs.wishlist.domain.exception.UserAlreadyExistsException;
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
public class UserInteractorTest {

    private static final String USER_ID = "userId";
    private static final String EMAIL = "email@gmail.com";
    private static final String DUPLICATE_EMAIL_ERROR_CODE = "error.duplicate.email";
    private static final String DUPLICATE_EMAIL_ERROR = "E-mail já usado em outro cadastro";
    private static final String USER_SAVE_ERROR = "Erro ao salvar usuário";
    private static final String USER_SAVE_ERROR_CODE = "error.save.user";

    @InjectMocks
    UserInteractor userInteractor;

    @Mock
    MessageHelper messageHelper;
    @Mock
    UserGateway userGateway;

    @Test
    void should_create_user() {
        User newUser = mock(User.class);
        User savedUser = mock(User.class);

        when(newUser.email()).thenReturn(EMAIL);
        when(savedUser.id()).thenReturn(USER_ID);
        when(userGateway.findUserByEmail(EMAIL)).thenReturn(Optional.empty());
        when(userGateway.createUser(newUser)).thenReturn(savedUser);

        User responseUser = userInteractor.createUser(newUser);

        assertEquals(USER_ID, responseUser.id());
        verify(userGateway).findUserByEmail(EMAIL);
    }

    @Test
    void should_return_user_already_exists_exception_when_email_is_used_by_another_user() {
        User existingUser = mock(User.class);
        User newUser = mock(User.class);

        when(newUser.email()).thenReturn(EMAIL);
        when(userGateway.findUserByEmail(EMAIL)).thenReturn(Optional.of(existingUser));
        when(messageHelper.getMessage(DUPLICATE_EMAIL_ERROR_CODE)).thenReturn(DUPLICATE_EMAIL_ERROR);

        UserAlreadyExistsException userAlreadyExistsException = assertThrows(UserAlreadyExistsException.class, () -> userInteractor.createUser(newUser));
        assertEquals(DUPLICATE_EMAIL_ERROR, userAlreadyExistsException.getMessage());
    }

    @Test
    void should_return_use_case_exception_when_generic_error() {
        User newUser = mock(User.class);

        when(newUser.email()).thenReturn(EMAIL);
        when(userGateway.findUserByEmail(EMAIL)).thenReturn(Optional.empty());
        when(userGateway.createUser(newUser)).thenThrow(RuntimeException.class);
        when(messageHelper.getMessage(USER_SAVE_ERROR_CODE)).thenReturn(USER_SAVE_ERROR);

        UseCaseException useCaseException = assertThrows(UseCaseException.class, () -> userInteractor.createUser(newUser));
        assertEquals(USER_SAVE_ERROR, useCaseException.getMessage());
    }

}
