package br.com.luizalabs.wishlist.application.usecases;

import br.com.luizalabs.wishlist.application.exception.UseCaseException;
import br.com.luizalabs.wishlist.application.gateways.ProductGateway;
import br.com.luizalabs.wishlist.domain.entity.Product;
import br.com.luizalabs.wishlist.infrastructure.adapters.MessageHelper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ProductInteractorTest {

    private static final String PRODUCT_ID = "productId";
    private static final String ERROR_FIND_PRODUCT= "Erro ao buscar os produtos";
    private static final String ERROR_FIND_PRODUCT_CODE = "error.find.product";

    @InjectMocks
    ProductInteractor productInteractor;

    @Mock
    ProductGateway productGateway;
    @Mock
    MessageHelper messageHelper;

    @Test
    void should_find_all_products() {
        Product product = mock(Product.class);

        when(product.id()).thenReturn(PRODUCT_ID);
        when(productGateway.findAllProducts()).thenReturn(List.of(product));

        List<Product> products = productInteractor.getAllProducts();

        assertEquals(1, products.size());
        assertEquals(PRODUCT_ID, products.getFirst().id());
    }

    @Test
    void should_return_use_case_exception_when_generic_error() {
        when(productGateway.findAllProducts()).thenThrow(RuntimeException.class);
        when(messageHelper.getMessage(ERROR_FIND_PRODUCT_CODE)).thenReturn(ERROR_FIND_PRODUCT);
        assertThrows(UseCaseException.class, () -> productInteractor.getAllProducts());
    }

}
