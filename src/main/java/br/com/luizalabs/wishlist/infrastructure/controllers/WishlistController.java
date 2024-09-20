package br.com.luizalabs.wishlist.infrastructure.controllers;

import br.com.luizalabs.wishlist.infrastructure.security.CustomUserDetails;
import br.com.luizalabs.wishlist.application.usecases.WishInteractor;
import br.com.luizalabs.wishlist.domain.entity.Product;
import br.com.luizalabs.wishlist.infrastructure.adapters.MessageHelper;
import br.com.luizalabs.wishlist.infrastructure.controllers.dto.WishlistResponse;
import br.com.luizalabs.wishlist.infrastructure.controllers.mapper.WishDTOMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/wishlist/products")
public class WishlistController {

    private final WishInteractor wishInteractor;
    private final WishDTOMapper wishDTOMapper;
    private final MessageHelper messageHelper;

    public WishlistController(WishInteractor wishInteractor,
                              WishDTOMapper wishDTOMapper,
                              MessageHelper messageHelper) {
        this.wishInteractor = wishInteractor;
        this.wishDTOMapper = wishDTOMapper;
        this.messageHelper = messageHelper;
    }

    @GetMapping
    public ResponseEntity<List<WishlistResponse>> getWishlist(@AuthenticationPrincipal CustomUserDetails userDetails) {
        String userId = userDetails.getUser().getId();
        List<Product> productWishList = wishInteractor.findAllWishes(userId);
        return ResponseEntity.ok().body(productWishList.stream().map(wishDTOMapper::toWishResponse).toList());
    }

    @PostMapping("/{productId}")
    public ResponseEntity<String> addProductToWishlist(@PathVariable String productId, @AuthenticationPrincipal CustomUserDetails userDetails) {
        String userId = userDetails.getUser().getId();
        wishInteractor.addProductToWishlist(userId, productId);
        return ResponseEntity.ok(messageHelper.getMessage("product.added.successfully"));
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<String> removeProductFromWishlist(@PathVariable String productId, @AuthenticationPrincipal CustomUserDetails userDetails) {
        String userId = userDetails.getUser().getId();
        wishInteractor.removeProductFromWishlist(userId, productId);
        return ResponseEntity.ok(messageHelper.getMessage("product.removed.successfully"));
    }

    @GetMapping("/{productId}/exists")
    public boolean productIdExistsInWishlist(@PathVariable String productId, @AuthenticationPrincipal CustomUserDetails userDetails) {
        String userId = userDetails.getUser().getId();
        return wishInteractor.productIdExistsInWishlist(userId, productId);
    }
}