package br.com.luizalabs.wishlist.domain.entity;

public record Product(String id,
                      String sku,
                      String seller,
                      String title,
                      String description,
                      String imageUrl,
                      String status) {
}
