package br.com.luizalabs.wishlist.infrastructure.gateways;

import br.com.luizalabs.wishlist.domain.entity.Wish;
import br.com.luizalabs.wishlist.infrastructure.persistence.WishDocument;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class WishDocumentMapperTest {

    private static final String PRODUCT_ID = "productId";
    private static final String USER_ID = "userId";

    @InjectMocks
    WishDocumentMapper WishDocumentMapper;

    @Test
    void should_mapper_wish_document_to_wish() {
        WishDocument wishDocument = mock(WishDocument.class);

        when(wishDocument.getUserId()).thenReturn(USER_ID);
        when(wishDocument.getProductId()).thenReturn(PRODUCT_ID);

        Wish wish = WishDocumentMapper.toWish(wishDocument);

        assertEquals(USER_ID, wish.userId());
        assertEquals(PRODUCT_ID, wish.productId());
    }

    @Test
    void should_mapper_wish_to_wish_document() {
        Wish wish = mock(Wish.class);

        when(wish.userId()).thenReturn(USER_ID);
        when(wish.productId()).thenReturn(PRODUCT_ID);

        WishDocument wishDocument = WishDocumentMapper.toWishDocument(wish);

        assertEquals(USER_ID, wishDocument.getUserId());
        assertEquals(PRODUCT_ID, wishDocument.getProductId());
    }

}
