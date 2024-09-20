package br.com.luizalabs.wishlist.infrastructure.gateways;

import br.com.luizalabs.wishlist.application.gateways.WishGateway;
import br.com.luizalabs.wishlist.domain.entity.Wish;
import br.com.luizalabs.wishlist.domain.exception.ProductNotFoundException;
import br.com.luizalabs.wishlist.infrastructure.adapters.MessageHelper;
import br.com.luizalabs.wishlist.infrastructure.persistence.WishDocument;
import br.com.luizalabs.wishlist.infrastructure.persistence.WishRepository;

import java.util.List;
import java.util.Optional;

public class WishRepositoryGateway implements WishGateway {

    private final WishRepository wishRepository;
    private final WishDocumentMapper wishDocumentMapper;
    private final MessageHelper messageHelper;

    public WishRepositoryGateway(WishRepository wishRepository,
                                 WishDocumentMapper wishDocumentMapper,
                                 MessageHelper messageHelper) {
        this.wishRepository = wishRepository;
        this.wishDocumentMapper = wishDocumentMapper;
        this.messageHelper = messageHelper;
    }

    @Override
    public List<Wish> findAllWishes(String userId) {
        List<WishDocument> wishList = wishRepository.findAllByUserId(userId);
        return wishList.stream().map(wishDocumentMapper::toWish).toList();
    }

    @Override
    public void addWish(Wish wish) {
        wishRepository.save(wishDocumentMapper.toWishDocument(wish));
    }

    @Override
    public void removeWishByUserIdAndProductId(String userId, String productId) {
        Optional<WishDocument> wish = wishRepository.findAllByUserIdAndProductId(userId, productId);
        if (wish.isEmpty()) {
            throw new ProductNotFoundException(messageHelper.getMessage("error.product.not.found"));
        }
        wishRepository.delete(wish.get());
    }

    @Override
    public boolean existsWishByUserIdAndProductId(String userId, String productId) {
        return wishRepository.existsByUserIdAndProductId(userId, productId);
    }

}
