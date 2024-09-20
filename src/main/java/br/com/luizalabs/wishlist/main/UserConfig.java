package br.com.luizalabs.wishlist.main;

import br.com.luizalabs.wishlist.application.gateways.UserGateway;
import br.com.luizalabs.wishlist.application.usecases.UserInteractor;
import br.com.luizalabs.wishlist.infrastructure.adapters.MessageHelper;
import br.com.luizalabs.wishlist.infrastructure.gateways.UserDocumentMapper;
import br.com.luizalabs.wishlist.infrastructure.gateways.UserRepositoryGateway;
import br.com.luizalabs.wishlist.infrastructure.persistence.UserRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UserConfig {

    @Bean
    UserInteractor userInteractor(UserGateway userGateway, MessageHelper messageHelper) {
        return new UserInteractor(userGateway, messageHelper);
    }

    @Bean
    UserGateway userGateway(UserRepository userRepository, UserDocumentMapper userDocumentMapper) {
        return new UserRepositoryGateway(userRepository, userDocumentMapper);
    }

 }
