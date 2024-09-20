package br.com.luizalabs.wishlist.infrastructure.controllers.dto;

public record ProductResponse(String id,
                              String sku,
                              String seller,
                              String title,
                              String description,
                              String imageUrl,
                              String status) {
}
