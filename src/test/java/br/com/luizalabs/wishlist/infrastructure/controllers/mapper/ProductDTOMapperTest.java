package br.com.luizalabs.wishlist.infrastructure.controllers.mapper;

import br.com.luizalabs.wishlist.domain.entity.Product;
import br.com.luizalabs.wishlist.infrastructure.controllers.dto.ProductResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ProductDTOMapperTest {

    private static final String PRODUCT_ID = "productId";
    private static final String SKU = "sku";
    private static final String SELLER = "seller";
    private static final String TITLE = "title";
    private static final String DESCRIPTION = "description";
    private static final String IMAGE_URL = "image";
    private static final String STATUS = "ACTIVE";

    @InjectMocks
    ProductDTOMapper productDTOMapper;

    @Test
    void should_mapper_product_to_product_response() {
        Product product = mock(Product.class);

        when(product.id()).thenReturn(PRODUCT_ID);
        when(product.sku()).thenReturn(SKU);
        when(product.seller()).thenReturn(SELLER);
        when(product.title()).thenReturn(TITLE);
        when(product.description()).thenReturn(DESCRIPTION);
        when(product.imageUrl()).thenReturn(IMAGE_URL);
        when(product.status()).thenReturn(STATUS);

        ProductResponse productResponse = productDTOMapper.toProductResponse(product);

        assertEquals(PRODUCT_ID, productResponse.id());
        assertEquals(SKU, productResponse.sku());
        assertEquals(SELLER, productResponse.seller());
        assertEquals(TITLE, productResponse.title());
        assertEquals(DESCRIPTION, productResponse.description());
        assertEquals(IMAGE_URL, productResponse.imageUrl());
        assertEquals(STATUS, productResponse.status());
    }

}
