package br.com.luizalabs.wishlist.infrastructure.gateways;

import br.com.luizalabs.wishlist.domain.entity.Product;
import br.com.luizalabs.wishlist.infrastructure.persistence.ProductDocument;
import br.com.luizalabs.wishlist.infrastructure.persistence.ProductStatus;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ProductDocumentMapperTest {

    private static final String PRODUCT_ID = "productId";
    private static final String SKU = "sku";
    private static final String SELLER = "seller";
    private static final String TITLE = "title";
    private static final String DESCRIPTION = "description";
    private static final String IMAGE_URL = "image";
    private static final String STATUS = "INACTIVE";

    @InjectMocks
    ProductDocumentMapper productDocumentMapper;

    @Test
    void should_mapper_product_document_to_product() {
        ProductDocument productDocument = mock(ProductDocument.class);

        when(productDocument.getId()).thenReturn(PRODUCT_ID);
        when(productDocument.getSku()).thenReturn(SKU);
        when(productDocument.getSeller()).thenReturn(SELLER);
        when(productDocument.getTitle()).thenReturn(TITLE);
        when(productDocument.getDescription()).thenReturn(DESCRIPTION);
        when(productDocument.getImageUrl()).thenReturn(IMAGE_URL);
        when(productDocument.getStatus()).thenReturn(ProductStatus.INACTIVE);

        Product product = productDocumentMapper.toProduct(productDocument);

        assertEquals(PRODUCT_ID, product.id());
        assertEquals(SKU, product.sku());
        assertEquals(SELLER, product.seller());
        assertEquals(TITLE, product.title());
        assertEquals(DESCRIPTION, product.description());
        assertEquals(IMAGE_URL, product.imageUrl());
        assertEquals(STATUS, product.status());
    }

}
