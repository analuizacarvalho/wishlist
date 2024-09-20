package br.com.luizalabs.wishlist.infrastructure.gateways;

import br.com.luizalabs.wishlist.domain.entity.Wish;
import br.com.luizalabs.wishlist.domain.exception.ProductNotFoundException;
import br.com.luizalabs.wishlist.infrastructure.adapters.MessageHelper;
import br.com.luizalabs.wishlist.infrastructure.persistence.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class WishRepositoryGatewayTest {

    private static final String PRODUCT_ID = "productId";
    private static final String USER_ID = "userId";
    private static final String PRODUCT_NOT_FOUND_ERROR_CODE = "error.product.not.found";
    private static final String PRODUCT_NOT_FOUND_ERROR_MESSAGE = "Produto não encontrado";

    @InjectMocks
    WishRepositoryGateway wishRepositoryGateway;

    @Mock
    WishRepository wishRepository;
    @Mock
    WishDocumentMapper wishDocumentMapper;
    @Mock
    MessageHelper messageHelper;

    @Test
    void should_return_all_wishes() {
        WishDocument wishDocument = mock(WishDocument.class);
        Wish wish = mock(Wish.class);

        when(wishRepository.findAllByUserId(USER_ID)).thenReturn(List.of(wishDocument));
        when(wishDocumentMapper.toWish(wishDocument)).thenReturn(wish);

        List<Wish> wishList = wishRepositoryGateway.findAllWishes(USER_ID);
        assertEquals(1, wishList.size());
    }

    @Test
    void should_add_wish() {
        WishDocument wishDocument = mock(WishDocument.class);
        Wish wish = mock(Wish.class);

        when(wishDocumentMapper.toWishDocument(wish)).thenReturn(wishDocument);

        wishRepositoryGateway.addWish(wish);

        verify(wishRepository).save(wishDocument);
        verify(wishDocumentMapper).toWishDocument(wish);
    }

    @ParameterizedTest
    @ValueSource(booleans = {true, false})
    void should_verify_if_exists_wishlist_product(final boolean exists) {
        when(wishRepository.existsByUserIdAndProductId(USER_ID, PRODUCT_ID)).thenReturn(exists);
        assertEquals(exists, wishRepositoryGateway.existsWishByUserIdAndProductId(USER_ID, PRODUCT_ID));
    }

    @Test
    void should_remove_when_product_exists() {
        WishDocument wishDocument = mock(WishDocument.class);
        when(wishRepository.findAllByUserIdAndProductId(USER_ID, PRODUCT_ID)).thenReturn(Optional.of(wishDocument));
        wishRepositoryGateway.removeWishByUserIdAndProductId(USER_ID, PRODUCT_ID);
        verify(wishRepository).delete(wishDocument);
    }

    @Test
    void throw_product_not_found_exception_when_product_not_exists() {
        WishDocument wishDocument = mock(WishDocument.class);

        when(wishRepository.findAllByUserIdAndProductId(USER_ID, PRODUCT_ID)).thenReturn(Optional.empty());
        when(messageHelper.getMessage(PRODUCT_NOT_FOUND_ERROR_CODE)).thenReturn(PRODUCT_NOT_FOUND_ERROR_MESSAGE);

        ProductNotFoundException productNotFoundException = assertThrows(ProductNotFoundException.class, () -> wishRepositoryGateway.removeWishByUserIdAndProductId(USER_ID, PRODUCT_ID));
        assertEquals(PRODUCT_NOT_FOUND_ERROR_MESSAGE, productNotFoundException.getMessage());
        verify(wishRepository, never()).delete(wishDocument);
    }

}
