package br.com.luizalabs.wishlist.infrastructure.gateways;

import br.com.luizalabs.wishlist.domain.entity.User;
import br.com.luizalabs.wishlist.infrastructure.security.TokenService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AuthenticationServiceGatewayTest {

    private static final String PASSWORD = "password";
    private static final String USER_PASSWORD = "userPassword";
    private static final String TOKEN = "token";

    @InjectMocks
    AuthenticationServiceGateway authenticationServiceGateway;

    @Mock
    PasswordEncoder passwordEncoder;
    @Mock
    TokenService tokenService;

    @Test
    void should_generate_token() {
        User user = mock(User.class);
        when(tokenService.generateToken(user)).thenReturn(TOKEN);
        assertEquals(TOKEN, authenticationServiceGateway.generateToken(user));
    }

    @ParameterizedTest
    @ValueSource(booleans = {true, false})
    void should_validate_password(final boolean isValidPassword) {
        when(passwordEncoder.matches(PASSWORD, USER_PASSWORD)).thenReturn(isValidPassword);
        assertEquals(isValidPassword, authenticationServiceGateway.validatePassword(PASSWORD, USER_PASSWORD));
    }

}
