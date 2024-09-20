package br.com.luizalabs.wishlist.domain.entity;

public record User(String id,
                   String name,
                   String email,
                   String password) {
}
