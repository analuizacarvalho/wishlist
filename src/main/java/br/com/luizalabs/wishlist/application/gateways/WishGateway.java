package br.com.luizalabs.wishlist.application.gateways;

import br.com.luizalabs.wishlist.domain.entity.Wish;

import java.util.List;

public interface WishGateway {
    List<Wish> findAllWishes(String userId);
    void removeWishByUserIdAndProductId(String userId, String productId);
    void addWish(Wish wish);
    boolean existsWishByUserIdAndProductId(String userId, String productId);
}
