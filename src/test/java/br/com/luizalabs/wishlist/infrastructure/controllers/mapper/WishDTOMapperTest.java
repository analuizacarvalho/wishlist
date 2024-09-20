package br.com.luizalabs.wishlist.infrastructure.controllers.mapper;

import br.com.luizalabs.wishlist.domain.entity.Product;
import br.com.luizalabs.wishlist.infrastructure.controllers.dto.WishlistResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class WishDTOMapperTest {

    private static final String PRODUCT_ID = "productId";
    private static final String SKU = "sku";
    private static final String SELLER = "seller";
    private static final String TITLE = "title";
    private static final String STATUS = "ACTIVE";

    @InjectMocks
    WishDTOMapper wishDTOMapper;

    @Test
    void should_mapper_product_to_product_response() {
        Product product = mock(Product.class);

        when(product.id()).thenReturn(PRODUCT_ID);
        when(product.sku()).thenReturn(SKU);
        when(product.seller()).thenReturn(SELLER);
        when(product.title()).thenReturn(TITLE);
        when(product.status()).thenReturn(STATUS);

        WishlistResponse wishlistResponse = wishDTOMapper.toWishResponse(product);

        assertEquals(PRODUCT_ID, wishlistResponse.id());
        assertEquals(SKU, wishlistResponse.sku());
        assertEquals(SELLER, wishlistResponse.seller());
        assertEquals(TITLE, wishlistResponse.title());
        assertEquals(STATUS, wishlistResponse.status());
    }

}
