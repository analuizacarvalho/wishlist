package br.com.luizalabs.wishlist.infrastructure.controllers.mapper;

import br.com.luizalabs.wishlist.domain.entity.Product;
import br.com.luizalabs.wishlist.infrastructure.controllers.dto.WishlistResponse;
import org.springframework.stereotype.Component;

@Component
public class WishDTOMapper {
    public WishlistResponse toWishResponse(Product product) {
        return new WishlistResponse(product.id(), product.sku(), product.seller(), product.title(), product.status());
    }
}
