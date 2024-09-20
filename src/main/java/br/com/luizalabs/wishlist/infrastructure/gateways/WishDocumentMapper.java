package br.com.luizalabs.wishlist.infrastructure.gateways;

import br.com.luizalabs.wishlist.domain.entity.Wish;
import br.com.luizalabs.wishlist.infrastructure.persistence.WishDocument;

public class WishDocumentMapper {

    Wish toWish(WishDocument wishDocument) {
        return new Wish(wishDocument.getUserId(), wishDocument.getProductId());
    }

    WishDocument toWishDocument(Wish wish) {
        return WishDocument.builder().userId(wish.userId()).productId(wish.productId()).build();
    }
}
