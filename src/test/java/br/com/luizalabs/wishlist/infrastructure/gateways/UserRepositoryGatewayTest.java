package br.com.luizalabs.wishlist.infrastructure.gateways;

import br.com.luizalabs.wishlist.domain.entity.User;
import br.com.luizalabs.wishlist.infrastructure.persistence.UserDocument;
import br.com.luizalabs.wishlist.infrastructure.persistence.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserRepositoryGatewayTest {

    private static final String USER_EMAIL = "email@gmail.com";

    @InjectMocks
    UserRepositoryGateway userRepositoryGateway;

    @Mock
    UserRepository userRepository;
    @Mock
    UserDocumentMapper userDocumentMapper;

    @Test
    void should_return_user_by_email() {
        UserDocument userDocument = mock(UserDocument.class);
        User user = mock(User.class);

        when(userRepository.findByEmail(USER_EMAIL)).thenReturn(Optional.of(userDocument));
        when(userDocumentMapper.toUser(userDocument)).thenReturn(user);

        assertEquals(Optional.of(user), userRepositoryGateway.findUserByEmail(USER_EMAIL));
    }

    @Test
    void should_return_empty_when_user_not_exists() {
        when(userRepository.findByEmail(USER_EMAIL)).thenReturn(Optional.empty());
        assertEquals(Optional.empty(), userRepositoryGateway.findUserByEmail(USER_EMAIL));
    }

    @Test
    void should_create_user() {
        UserDocument userDocument = mock(UserDocument.class);
        UserDocument savedUserDocument = mock(UserDocument.class);
        User user = mock(User.class);
        User createUser = mock(User.class);

        when(userDocumentMapper.toUserDocument(user)).thenReturn(userDocument);
        when(userRepository.save(userDocument)).thenReturn(savedUserDocument);
        when(userDocumentMapper.toUser(savedUserDocument)).thenReturn(createUser);

        assertEquals(createUser, userRepositoryGateway.createUser(user));
        verify(userDocumentMapper).toUserDocument(user);
        verify(userDocumentMapper).toUser(savedUserDocument);
        verify(userRepository).save(userDocument);
    }

}
