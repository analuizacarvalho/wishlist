package br.com.luizalabs.wishlist.infrastructure.persistence;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface WishRepository extends MongoRepository<WishDocument, String> {

    List<WishDocument> findAllByUserId(String userId);
    Optional<WishDocument> findAllByUserIdAndProductId(String userId, String productId);
    boolean existsByUserIdAndProductId(String userId, String productId);
    void deleteByUserIdAndProductId(String userId, String productId);

}
