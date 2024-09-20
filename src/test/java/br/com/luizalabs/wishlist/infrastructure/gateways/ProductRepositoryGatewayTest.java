package br.com.luizalabs.wishlist.infrastructure.gateways;

import br.com.luizalabs.wishlist.domain.entity.Product;
import br.com.luizalabs.wishlist.infrastructure.persistence.ProductDocument;
import br.com.luizalabs.wishlist.infrastructure.persistence.ProductRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProductRepositoryGatewayTest {

    private static final String PRODUCT_ID = "productId";
    private static final String PRODUCT_ID_2 = "productId2";

    @InjectMocks
    ProductRepositoryGateway productRepositoryGateway;

    @Mock
    ProductRepository productRepository;
    @Mock
    ProductDocumentMapper productDocumentMapper;

    @Test
    void should_return_product_by_id() {
        ProductDocument productDocument = mock(ProductDocument.class);
        Product product = mock(Product.class);

        when(productRepository.findById(PRODUCT_ID)).thenReturn(Optional.of(productDocument));
        when(productDocumentMapper.toProduct(productDocument)).thenReturn(product);

        assertEquals(Optional.of(product), productRepositoryGateway.findProductById(PRODUCT_ID));
    }

    @Test
    void should_return_empty_when_product_not_exists() {
        when(productRepository.findById(PRODUCT_ID)).thenReturn(Optional.empty());
        assertEquals(Optional.empty(), productRepositoryGateway.findProductById(PRODUCT_ID));
    }

    @Test
    void should_return_product_list() {
        ProductDocument productDocument = mock(ProductDocument.class);
        Product product = mock(Product.class);

        when(productRepository.findAll()).thenReturn(List.of(productDocument));
        when(productDocumentMapper.toProduct(productDocument)).thenReturn(product);

        List<Product> productList = productRepositoryGateway.findAllProducts();
        assertEquals(List.of(product), productList);
        assertEquals(1, productList.size());
    }

    @Test
    void should_return_product_list_by_id_list() {
        ProductDocument productDocument1 = mock(ProductDocument.class);
        ProductDocument productDocument2 = mock(ProductDocument.class);
        Product product = mock(Product.class);
        List<String> productIdList = List.of(PRODUCT_ID, PRODUCT_ID_2);

        when(productRepository.findAllById(productIdList)).thenReturn(List.of(productDocument1, productDocument2));
        when(productDocumentMapper.toProduct(any())).thenReturn(product);

        List<Product> productList = productRepositoryGateway.findAllProductsByIdList(productIdList);
        assertEquals(2, productList.size());
    }

}
