package br.com.luizalabs.wishlist.infrastructure.controllers;

import br.com.luizalabs.wishlist.application.usecases.ProductInteractor;
import br.com.luizalabs.wishlist.domain.entity.Product;
import br.com.luizalabs.wishlist.infrastructure.controllers.dto.ProductResponse;
import br.com.luizalabs.wishlist.infrastructure.controllers.mapper.ProductDTOMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ProductControllerTest {

    private static final String PRODUCT_ID = "productId";

    @InjectMocks
    ProductController productController;

    @Mock
    ProductInteractor productInteractor;

    @Mock
    ProductDTOMapper productDTOMapper;

    @Test
    void should_find_all_products() {
        ProductResponse productResponse = mock(ProductResponse.class);
        Product product = mock(Product.class);

        when(productResponse.id()).thenReturn(PRODUCT_ID);
        when(productDTOMapper.toProductResponse(product)).thenReturn(productResponse);
        when(productInteractor.getAllProducts()).thenReturn(List.of(product));

        ResponseEntity<List<ProductResponse>> response = productController.getAllProducts();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(PRODUCT_ID, response.getBody().get(0).id());
    }

}
