package br.com.luizalabs.wishlist.infrastructure.controllers;

import br.com.luizalabs.wishlist.application.usecases.WishInteractor;
import br.com.luizalabs.wishlist.domain.entity.Product;
import br.com.luizalabs.wishlist.infrastructure.adapters.MessageHelper;
import br.com.luizalabs.wishlist.infrastructure.controllers.dto.WishlistResponse;
import br.com.luizalabs.wishlist.infrastructure.controllers.mapper.WishDTOMapper;
import br.com.luizalabs.wishlist.infrastructure.persistence.UserDocument;
import br.com.luizalabs.wishlist.infrastructure.security.CustomUserDetails;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class WishlistControllerTest {

    private static final String PRODUCT_ID = "productId";
    private static final String USER_ID = "userId";
    private static final String PRODUCT_REMOVED_SUCCESSFULY_MESSAGE = "Produto removido com sucesso da Wishlist";
    private static final String PRODUCT_REMOVED_SUCCESSFULY_CODE = "product.removed.successfully";
    private static final String PRODUCT_SAVE_SUCCESSFULY_MESSAGE = "Produto adicionado com sucesso à Wishlist";
    private static final String PRODUCT_SAVE_SUCCESSFULY_CODE = "product.added.successfully";

    @InjectMocks
    WishlistController wishlistController;

    @Mock
    MessageHelper messageHelper;
    @Mock
    WishInteractor wishInteractor;
    @Mock
    WishDTOMapper wishDTOMapper;

    @Test
    void should_find_wishlist() {
        WishlistResponse wishlistResponse = mock(WishlistResponse.class);
        Product product = mock(Product.class);
        CustomUserDetails userDetails = mock(CustomUserDetails.class);
        UserDocument user = mock(UserDocument.class);

        when(userDetails.getUser()).thenReturn(user);
        when(userDetails.getUser().getId()).thenReturn(USER_ID);
        when(wishInteractor.findAllWishes(USER_ID)).thenReturn(List.of(product));
        when(wishDTOMapper.toWishResponse(product)).thenReturn(wishlistResponse);

        ResponseEntity<List<WishlistResponse>> response = wishlistController.getWishlist(userDetails);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
        assertEquals(wishlistResponse, response.getBody().get(0));
    }

    @ParameterizedTest
    @ValueSource(booleans = {true, false})
    void should_verify_if_exists_wishlist_product(final boolean exists) {
        CustomUserDetails userDetails = mock(CustomUserDetails.class);
        UserDocument user = mock(UserDocument.class);

        when(userDetails.getUser()).thenReturn(user);
        when(userDetails.getUser().getId()).thenReturn(USER_ID);
        when(wishInteractor.productIdExistsInWishlist(USER_ID, PRODUCT_ID)).thenReturn(exists);

        boolean response = wishlistController.productIdExistsInWishlist(PRODUCT_ID, userDetails);

        assertEquals(exists, response);
    }

    @Test
    void should_remove_wishlist_product() {
        CustomUserDetails userDetails = mock(CustomUserDetails.class);
        UserDocument user = mock(UserDocument.class);

        when(userDetails.getUser()).thenReturn(user);
        when(userDetails.getUser().getId()).thenReturn(USER_ID);
        when(messageHelper.getMessage(PRODUCT_REMOVED_SUCCESSFULY_CODE)).thenReturn(PRODUCT_REMOVED_SUCCESSFULY_MESSAGE);

        ResponseEntity<String> response = wishlistController.removeProductFromWishlist(PRODUCT_ID, userDetails);

        verify(wishInteractor).removeProductFromWishlist(USER_ID, PRODUCT_ID);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(PRODUCT_REMOVED_SUCCESSFULY_MESSAGE, response.getBody());
    }

    @Test
    void should_save_wishlist_product() {
        CustomUserDetails userDetails = mock(CustomUserDetails.class);
        UserDocument user = mock(UserDocument.class);

        when(userDetails.getUser()).thenReturn(user);
        when(userDetails.getUser().getId()).thenReturn(USER_ID);
        when(messageHelper.getMessage(PRODUCT_SAVE_SUCCESSFULY_CODE)).thenReturn(PRODUCT_SAVE_SUCCESSFULY_MESSAGE);

        ResponseEntity<String> response = wishlistController.addProductToWishlist(PRODUCT_ID, userDetails);

        verify(wishInteractor).addProductToWishlist(USER_ID, PRODUCT_ID);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(PRODUCT_SAVE_SUCCESSFULY_MESSAGE, response.getBody());
    }

}
