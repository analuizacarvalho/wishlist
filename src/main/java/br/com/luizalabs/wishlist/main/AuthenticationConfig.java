package br.com.luizalabs.wishlist.main;

import br.com.luizalabs.wishlist.application.gateways.AuthenticationGateway;
import br.com.luizalabs.wishlist.application.gateways.UserGateway;
import br.com.luizalabs.wishlist.application.usecases.AuthenticationInteractor;
import br.com.luizalabs.wishlist.infrastructure.adapters.MessageHelper;
import br.com.luizalabs.wishlist.infrastructure.gateways.AuthenticationServiceGateway;
import br.com.luizalabs.wishlist.infrastructure.security.TokenService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class AuthenticationConfig {

    @Bean
    AuthenticationInteractor authenticationInteractor(UserGateway userGateway, AuthenticationGateway authenticationGateway, MessageHelper messageHelper) {
        return new AuthenticationInteractor(userGateway, authenticationGateway, messageHelper);
    }

    @Bean
    AuthenticationGateway authenticationGateway(PasswordEncoder passwordEncoder, TokenService tokenService) {
        return new AuthenticationServiceGateway(passwordEncoder, tokenService);
    }
 }
