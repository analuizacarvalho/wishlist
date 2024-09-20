package br.com.luizalabs.wishlist.main;

import br.com.luizalabs.wishlist.application.gateways.ProductGateway;
import br.com.luizalabs.wishlist.application.gateways.WishGateway;
import br.com.luizalabs.wishlist.application.usecases.WishInteractor;
import br.com.luizalabs.wishlist.infrastructure.adapters.MessageHelper;
import br.com.luizalabs.wishlist.infrastructure.gateways.WishDocumentMapper;
import br.com.luizalabs.wishlist.infrastructure.gateways.WishRepositoryGateway;
import br.com.luizalabs.wishlist.infrastructure.persistence.WishRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class WishConfig {

    @Bean
    WishInteractor wishInteractor(WishGateway wishGateway, ProductGateway productGateway, MessageHelper messageHelper) {
        return new WishInteractor(wishGateway, productGateway, messageHelper);
    }

    @Bean
    WishGateway wishGateway(WishRepository wishRepository, WishDocumentMapper wishDocumentMapper, MessageHelper messageHelper) {
        return new WishRepositoryGateway(wishRepository, wishDocumentMapper, messageHelper);
    }

 }
