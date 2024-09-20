package br.com.luizalabs.wishlist.infrastructure.controllers;

import br.com.luizalabs.wishlist.application.usecases.AuthenticationInteractor;
import br.com.luizalabs.wishlist.application.usecases.UserInteractor;
import br.com.luizalabs.wishlist.domain.entity.User;
import br.com.luizalabs.wishlist.infrastructure.controllers.dto.RegisterRequest;
import br.com.luizalabs.wishlist.infrastructure.controllers.dto.RegisterResponse;
import br.com.luizalabs.wishlist.infrastructure.controllers.mapper.UserDTOMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserControllerTest {

    private static final String EMAIL = "email";
    private static final String PASSWORD = "password";
    private static final String TOKEN = "token";

    @InjectMocks
    UserController userController;

    @Mock
    UserInteractor userInteractor;
    @Mock
    UserDTOMapper userDTOMapper;
    @Mock
    AuthenticationInteractor authenticationInteractor;

    @Test
    void should_register_user() {
        RegisterResponse registerResponse = mock(RegisterResponse.class);
        RegisterRequest registerRequest = mock(RegisterRequest.class);
        User user = mock(User.class);
        User savedUser = mock(User.class);

        when(registerRequest.email()).thenReturn(EMAIL);
        when(registerRequest.password()).thenReturn(PASSWORD);
        when(userDTOMapper.toUser(registerRequest)).thenReturn(user);
        when(authenticationInteractor.login(EMAIL, PASSWORD)).thenReturn(TOKEN);
        when(userInteractor.createUser(user)).thenReturn(savedUser);
        when(userDTOMapper.toRegisterResponse(savedUser, TOKEN)).thenReturn(registerResponse);

        ResponseEntity<RegisterResponse> response = userController.register(registerRequest);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(userDTOMapper).toUser(registerRequest);
        verify(userDTOMapper).toRegisterResponse(savedUser, TOKEN);
        verify(authenticationInteractor).login(EMAIL, PASSWORD);
        verify(userInteractor).createUser(user);
    }

}
