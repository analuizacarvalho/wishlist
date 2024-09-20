package br.com.luizalabs.wishlist.main;

import br.com.luizalabs.wishlist.application.gateways.ProductGateway;
import br.com.luizalabs.wishlist.application.usecases.ProductInteractor;
import br.com.luizalabs.wishlist.infrastructure.adapters.MessageHelper;
import br.com.luizalabs.wishlist.infrastructure.gateways.ProductDocumentMapper;
import br.com.luizalabs.wishlist.infrastructure.gateways.ProductRepositoryGateway;
import br.com.luizalabs.wishlist.infrastructure.controllers.mapper.ProductDTOMapper;
import br.com.luizalabs.wishlist.infrastructure.persistence.ProductRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ProductConfig {

    @Bean
    ProductInteractor productInteractor(ProductGateway productGateway, MessageHelper messageHelper) {
        return new ProductInteractor(productGateway, messageHelper);
    }

    @Bean
    ProductGateway productGateway(ProductRepository productRepository, ProductDocumentMapper productDocumentMapper) {
        return new ProductRepositoryGateway(productRepository, productDocumentMapper);
    }

    @Bean
    ProductDocumentMapper productDocumentMapper() {
        return new ProductDocumentMapper();
    }

    @Bean
    ProductDTOMapper productDTOMapper() {
        return new ProductDTOMapper();
    }
 }
