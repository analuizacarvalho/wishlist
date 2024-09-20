package br.com.luizalabs.wishlist.infrastructure.controllers;

import br.com.luizalabs.wishlist.application.usecases.AuthenticationInteractor;
import br.com.luizalabs.wishlist.infrastructure.controllers.dto.LoginRequest;
import br.com.luizalabs.wishlist.infrastructure.controllers.dto.LoginResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AuthenticationControllerTest {

    private static final String PASSWORD = "password";
    private static final String EMAIL = "email@gmail.com";
    private static final String TOKEN = "token";

    @InjectMocks
    AuthenticationController authenticationController;

    @Mock
    AuthenticationInteractor authenticationInteractor;

    @Test
    void should_login() {
        LoginRequest loginRequest = mock(LoginRequest.class);

        when(loginRequest.email()).thenReturn(EMAIL);
        when(loginRequest.password()).thenReturn(PASSWORD);
        when(authenticationInteractor.login(EMAIL, PASSWORD)).thenReturn(TOKEN);

        ResponseEntity<LoginResponse> response = authenticationController.login(loginRequest);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(TOKEN, response.getBody().token());
    }

}
