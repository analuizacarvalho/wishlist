package br.com.luizalabs.wishlist.infrastructure.controllers.mapper;

import br.com.luizalabs.wishlist.domain.entity.User;
import br.com.luizalabs.wishlist.infrastructure.controllers.dto.RegisterRequest;
import br.com.luizalabs.wishlist.infrastructure.controllers.dto.RegisterResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserDTOMapperTest {

    private static final String USER_ID = "userId";
    private static final String NAME = "Nome";
    private static final String EMAIL = "email@gmail.com";
    private static final String PASSWORD = "password";
    private static final String TOKEN = "TOKEN";

    @InjectMocks
    UserDTOMapper userDTOMapper;

    @Test
    void should_mapper_user_to_register_response() {
        User user = mock(User.class);

        when(user.id()).thenReturn(USER_ID);
        when(user.name()).thenReturn(NAME);
        when(user.email()).thenReturn(EMAIL);

        RegisterResponse registerResponse = userDTOMapper.toRegisterResponse(user, TOKEN);

        assertEquals(USER_ID, registerResponse.id());
        assertEquals(NAME, registerResponse.name());
        assertEquals(EMAIL, registerResponse.email());
        assertEquals(TOKEN, registerResponse.token());
    }

    @Test
    void should_mapper_register_request_to_user() {
        RegisterRequest registerRequest = mock(RegisterRequest.class);

        when(registerRequest.name()).thenReturn(NAME);
        when(registerRequest.email()).thenReturn(EMAIL);
        when(registerRequest.password()).thenReturn(PASSWORD);

        User user = userDTOMapper.toUser(registerRequest);

        assertNull(user.id());
        assertEquals(NAME, user.name());
        assertEquals(EMAIL, user.email());
        assertEquals(PASSWORD, user.password());
    }

}
