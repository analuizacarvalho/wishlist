package br.com.luizalabs.wishlist.infrastructure.controllers.mapper;

import br.com.luizalabs.wishlist.domain.entity.Product;
import br.com.luizalabs.wishlist.infrastructure.controllers.dto.ProductResponse;

public class ProductDTOMapper {
    public ProductResponse toProductResponse(Product product) {
        return new ProductResponse(product.id(), product.sku(), product.seller(), product.title(), product.description(), product.imageUrl(), product.status());
    }
}
