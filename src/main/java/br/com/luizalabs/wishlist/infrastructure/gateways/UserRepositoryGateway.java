package br.com.luizalabs.wishlist.infrastructure.gateways;

import br.com.luizalabs.wishlist.application.gateways.UserGateway;
import br.com.luizalabs.wishlist.domain.entity.User;
import br.com.luizalabs.wishlist.infrastructure.persistence.UserDocument;
import br.com.luizalabs.wishlist.infrastructure.persistence.UserRepository;

import java.util.Optional;

public class UserRepositoryGateway implements UserGateway {

    private final UserRepository userRepository;
    private final UserDocumentMapper userDocumentMapper;

    public UserRepositoryGateway(UserRepository userRepository,
                                 UserDocumentMapper userDocumentMapper) {
        this.userRepository = userRepository;
        this.userDocumentMapper = userDocumentMapper;
    }

    @Override
    public Optional<User> findUserByEmail(String email) {
        Optional<UserDocument> user = userRepository.findByEmail(email);
        return user.map(userDocumentMapper::toUser);
    }

    @Override
    public User createUser(User user) {
        UserDocument userDocument = userDocumentMapper.toUserDocument(user);
        UserDocument savedUserDocument = userRepository.save(userDocument);
        return userDocumentMapper.toUser(savedUserDocument);
    }
}
