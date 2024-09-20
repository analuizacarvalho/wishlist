package br.com.luizalabs.wishlist.infrastructure.controllers.dto;

public record WishlistResponse(String id,
                               String sku,
                               String seller,
                               String title,
                               String status) {
}
