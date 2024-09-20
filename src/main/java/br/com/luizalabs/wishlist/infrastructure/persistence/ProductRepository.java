package br.com.luizalabs.wishlist.infrastructure.persistence;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface ProductRepository extends MongoRepository<ProductDocument, String> {
}
