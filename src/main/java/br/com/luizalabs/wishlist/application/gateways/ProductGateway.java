package br.com.luizalabs.wishlist.application.gateways;

import br.com.luizalabs.wishlist.domain.entity.Product;

import java.util.List;
import java.util.Optional;

public interface ProductGateway {
    List<Product> findAllProducts();
    List<Product> findAllProductsByIdList(List<String> productIdList);
    Optional<Product> findProductById(String productId);
}
