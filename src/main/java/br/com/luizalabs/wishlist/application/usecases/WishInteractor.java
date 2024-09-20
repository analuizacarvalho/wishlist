package br.com.luizalabs.wishlist.application.usecases;

import br.com.luizalabs.wishlist.application.exception.UseCaseException;
import br.com.luizalabs.wishlist.application.gateways.ProductGateway;
import br.com.luizalabs.wishlist.application.gateways.WishGateway;
import br.com.luizalabs.wishlist.domain.entity.Product;
import br.com.luizalabs.wishlist.domain.entity.Wish;
import br.com.luizalabs.wishlist.domain.exception.MaxListSizeExceededException;
import br.com.luizalabs.wishlist.domain.exception.ProductAlreadyInWishlistException;
import br.com.luizalabs.wishlist.domain.exception.ProductNotFoundException;
import br.com.luizalabs.wishlist.infrastructure.adapters.MessageHelper;

import java.util.List;

public class WishInteractor {

    private static final int MAX_WISH_LIST_SIZE = 20;

    private final ProductGateway productGateway;
    private final WishGateway wishGateway;
    private final MessageHelper messageHelper;

    public WishInteractor(WishGateway wishGateway,
                          ProductGateway productGateway,
                          MessageHelper messageHelper) {
        this.wishGateway = wishGateway;
        this.productGateway = productGateway;
        this.messageHelper = messageHelper;
    }

    public List<Product> findAllWishes(String userId) {
        try {
            List<Wish> wishList = wishGateway.findAllWishes(userId);
            return productGateway.findAllProductsByIdList(wishList.stream().map(Wish::productId).toList());
        } catch (Exception ex) {
            throw new UseCaseException(messageHelper.getMessage("error.find.wishlist"));
        }
    }

    public void addProductToWishlist(String userId, String productId) {
        validate(userId, productId);
        try {
            wishGateway.addWish(new Wish(userId, productId));
        } catch (Exception ex) {
            throw new UseCaseException(messageHelper.getMessage("error.save.product.wishlist"));
        }
    }

    private void validate(String userId, String productId) {
        validateProduct(productId);
        validateWishlist(userId, productId);
    }

    private void validateProduct(String productId) {
        productGateway.findProductById(productId)
                .orElseThrow(() -> new ProductNotFoundException(messageHelper.getMessage("error.product.not.found")));
    }

    private void validateWishlist(String userId, String productId) {
        List<Wish> wishList = wishGateway.findAllWishes(userId);

        if (wishList.stream().anyMatch(wish -> wish.productId().equals(productId))) {
            throw new ProductAlreadyInWishlistException(messageHelper.getMessage("error.product.already.in.wish.list"));
        }

        if (wishList.size() == MAX_WISH_LIST_SIZE) {
            throw new MaxListSizeExceededException(messageHelper.getMessage("error.size.wishlist.exceeded"));
        }
    }

    public void removeProductFromWishlist(String userId, String productId) {
        try {
            wishGateway.removeWishByUserIdAndProductId(userId, productId);
        } catch (ProductNotFoundException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new UseCaseException(messageHelper.getMessage("error.remove.product.wishlist"));
        }
    }

    public boolean productIdExistsInWishlist(String userId, String productId) {
        try {
            return wishGateway.existsWishByUserIdAndProductId(userId, productId);
        } catch (Exception ex) {
            throw new UseCaseException(messageHelper.getMessage("error.verify.exists.product.wishlist"));
        }
    }
}
