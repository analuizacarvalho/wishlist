package br.com.luizalabs.wishlist.infrastructure.gateways;

import br.com.luizalabs.wishlist.domain.entity.User;

import br.com.luizalabs.wishlist.infrastructure.persistence.UserDocument;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserDocumentMapperTest {

    private static final String USER_ID = "userId";
    private static final String NAME = "Nome";
    private static final String EMAIL = "email@gmail.com";
    private static final String PASSWORD = "password";
    private static final String ENCODED_PASSWORD = "encodedPassword";

    @InjectMocks
    UserDocumentMapper userDocumentMapper;

    @Mock
    PasswordEncoder passwordEncoder;

    @Test
    void should_mapper_user_document_to_user() {
        UserDocument userDocument = mock(UserDocument.class);

        when(userDocument.getId()).thenReturn(USER_ID);
        when(userDocument.getName()).thenReturn(NAME);
        when(userDocument.getEmail()).thenReturn(EMAIL);

        User user = userDocumentMapper.toUser(userDocument);

        assertEquals(USER_ID, user.id());
        assertEquals(NAME, user.name());
        assertEquals(EMAIL, user.email());
    }

    @Test
    void should_mapper_user_to_user_document() {
        User user = mock(User.class);

        when(user.name()).thenReturn(NAME);
        when(user.email()).thenReturn(EMAIL);
        when(user.password()).thenReturn(PASSWORD);
        when(passwordEncoder.encode(PASSWORD)).thenReturn(ENCODED_PASSWORD);

        UserDocument userDocument = userDocumentMapper.toUserDocument(user);

        assertNull(userDocument.getId());
        assertEquals(NAME, userDocument.getName());
        assertEquals(EMAIL, userDocument.getEmail());
        assertEquals(ENCODED_PASSWORD, userDocument.getPassword());
    }

}
