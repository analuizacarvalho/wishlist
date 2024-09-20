package br.com.luizalabs.wishlist.application.usecases;

import br.com.luizalabs.wishlist.application.exception.UseCaseException;
import br.com.luizalabs.wishlist.application.gateways.ProductGateway;
import br.com.luizalabs.wishlist.application.gateways.WishGateway;
import br.com.luizalabs.wishlist.domain.entity.Product;
import br.com.luizalabs.wishlist.domain.entity.Wish;
import br.com.luizalabs.wishlist.domain.exception.MaxListSizeExceededException;
import br.com.luizalabs.wishlist.domain.exception.ProductAlreadyInWishlistException;
import br.com.luizalabs.wishlist.domain.exception.ProductNotFoundException;
import br.com.luizalabs.wishlist.infrastructure.adapters.MessageHelper;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class WishInteractorTest {

    private static final String USER_ID = "userId";
    private static final String PRODUCT_ID = "productId";
    private static final String FIND_WISHLIST_ERROR_CODE = "error.find.wishlist";
    private static final String FIND_WISHLIST_ERROR = "Erro ao buscar Wishlist";
    private static final String PRODUCT_NOT_FOUND_ERROR_CODE = "error.product.not.found";
    private static final String PRODUCT_NOT_FOUND_ERROR = "Produto não encontrado";
    private static final String PRODUCT_ALREADY_IN_WISHLIST_ERROR_CODE = "error.product.already.in.wish.list";
    private static final String PRODUCT_ALREADY_IN_WISHLIST_ERROR = "O Produto já está adicionado a sua Wishlist";
    private static final String SIZE_WISHLIST_EXCEEDED_ERROR_CODE = "error.size.wishlist.exceeded";
    private static final String SIZE_WISHLIST_EXCEEDED_ERROR = "Limite de 20 produtos excedidos em sua Wishlist";
    private static final String SAVE_PRODUCT_WISHLIST_ERROR_CODE = "error.save.product.wishlist";
    private static final String SAVE_PRODUCT_WISHLIST_ERROR = "Erro ao salvar produto na Wishlist";
    private static final String REMOVE_PRODUCT_WISHLIST_ERROR_CODE = "error.remove.product.wishlist";
    private static final String REMOVE_PRODUCT_WISHLIST_ERROR = "Erro ao remover o produto de sua Wishlist";
    private static final String VERIFY_EXISTS_PRODUCT_WISHLIST_ERROR_CODE = "error.verify.exists.product.wishlist";
    private static final String VERIFY_EXISTS_PRODUCT_WISHLIST_ERROR = "Erro ao verificar se o produto existe em sua Wishlist";

    @InjectMocks
    WishInteractor wishInteractor;

    @Mock
    MessageHelper messageHelper;
    @Mock
    WishGateway wishGateway;
    @Mock
    ProductGateway productGateway;

    @Test
    void should_find_all_wishlist_products() {
        Wish wish = mock(Wish.class);
        Product product = mock(Product.class);

        List<Wish> wishList = Lists.newArrayList(wish);

        when(wish.productId()).thenReturn(PRODUCT_ID);
        when(product.id()).thenReturn(PRODUCT_ID);
        when(wishGateway.findAllWishes(USER_ID)).thenReturn(List.of(wish));
        when(productGateway.findAllProductsByIdList(wishList.stream().map(Wish::productId).toList())).thenReturn(List.of(product));

        List<Product> products = wishInteractor.findAllWishes(USER_ID);

        assertEquals(1, products.size());
        assertEquals(PRODUCT_ID, products.getFirst().id());
    }

    @Test
    void should_return_use_case_exception_when_generic_error_when_get_all_wishlist_products() {
        when(wishGateway.findAllWishes(USER_ID)).thenThrow(RuntimeException.class);
        when(messageHelper.getMessage(FIND_WISHLIST_ERROR_CODE)).thenReturn(FIND_WISHLIST_ERROR);
        UseCaseException useCaseException = assertThrows(UseCaseException.class, () -> wishInteractor.findAllWishes(USER_ID));
        assertEquals(FIND_WISHLIST_ERROR, useCaseException.getMessage());
    }

    @Test
    void should_create_wish() {
        Product product = mock(Product.class);

        when(productGateway.findProductById(PRODUCT_ID)).thenReturn(Optional.of(product));
        when(wishGateway.findAllWishes(USER_ID)).thenReturn(Lists.newArrayList());

        wishInteractor.addProductToWishlist(USER_ID, PRODUCT_ID);
        verify(wishGateway).addWish(any(Wish.class));
    }

    @Test
    void should_return_product_not_found_exception_when_product_not_exists() {
        when(productGateway.findProductById(PRODUCT_ID)).thenReturn(Optional.empty());
        when(messageHelper.getMessage(PRODUCT_NOT_FOUND_ERROR_CODE)).thenReturn(PRODUCT_NOT_FOUND_ERROR);

        ProductNotFoundException exception = assertThrows(ProductNotFoundException.class, () -> wishInteractor.addProductToWishlist(USER_ID, PRODUCT_ID));
        assertEquals(PRODUCT_NOT_FOUND_ERROR, exception.getMessage());
    }

    @Test
    void should_return_product_already_in_wishlist_exception_when_product_already_in_wishlist() {
        Product product = mock(Product.class);
        Wish wish = mock(Wish.class);

        when(wish.productId()).thenReturn(PRODUCT_ID);
        when(productGateway.findProductById(PRODUCT_ID)).thenReturn(Optional.of(product));
        when(wishGateway.findAllWishes(USER_ID)).thenReturn(List.of(wish));
        when(messageHelper.getMessage(PRODUCT_ALREADY_IN_WISHLIST_ERROR_CODE)).thenReturn(PRODUCT_ALREADY_IN_WISHLIST_ERROR);

        ProductAlreadyInWishlistException exception = assertThrows(ProductAlreadyInWishlistException.class, () -> wishInteractor.addProductToWishlist(USER_ID, PRODUCT_ID));
        assertEquals(PRODUCT_ALREADY_IN_WISHLIST_ERROR, exception.getMessage());
    }

    @Test
    void should_return_max_list_size_exceeded_exception_when_product_already_in_wishlist() {
        Product product = mock(Product.class);

        AtomicInteger counter = new AtomicInteger(1);
        List<Wish> wishes = Stream.generate(() -> new Wish(USER_ID, "PRODUCT_ID_" + counter.getAndIncrement()))
                .limit(20)
                .collect(Collectors.toList());

        when(productGateway.findProductById(PRODUCT_ID)).thenReturn(Optional.of(product));
        when(wishGateway.findAllWishes(USER_ID)).thenReturn(wishes);
        when(messageHelper.getMessage(SIZE_WISHLIST_EXCEEDED_ERROR_CODE)).thenReturn(SIZE_WISHLIST_EXCEEDED_ERROR);

        MaxListSizeExceededException exception = assertThrows(MaxListSizeExceededException.class, () -> wishInteractor.addProductToWishlist(USER_ID, PRODUCT_ID));
        assertEquals(SIZE_WISHLIST_EXCEEDED_ERROR, exception.getMessage());
    }

    @Test
    void should_return_use_case_exception_when_generic_error_to_create_wish() {
        Product product = mock(Product.class);

        when(productGateway.findProductById(PRODUCT_ID)).thenReturn(Optional.of(product));
        doThrow(new RuntimeException())
                .when(wishGateway).addWish(any(Wish.class));

        when(wishGateway.findAllWishes(USER_ID)).thenReturn(Lists.newArrayList());
        when(messageHelper.getMessage(SAVE_PRODUCT_WISHLIST_ERROR_CODE)).thenReturn(SAVE_PRODUCT_WISHLIST_ERROR);

        UseCaseException exception = assertThrows(UseCaseException.class, () -> wishInteractor.addProductToWishlist(USER_ID, PRODUCT_ID));
        assertEquals(SAVE_PRODUCT_WISHLIST_ERROR, exception.getMessage());
    }

    @Test
    void should_remove_wish() {
        wishInteractor.removeProductFromWishlist(USER_ID, PRODUCT_ID);
        verify(wishGateway).removeWishByUserIdAndProductId(USER_ID, PRODUCT_ID);
    }

    @Test
    void should_return_use_case_exception_when_generic_error_to_remove_wish() {
        doThrow(new RuntimeException())
                .when(wishGateway).removeWishByUserIdAndProductId(USER_ID, PRODUCT_ID);
        when(messageHelper.getMessage(REMOVE_PRODUCT_WISHLIST_ERROR_CODE)).thenReturn(REMOVE_PRODUCT_WISHLIST_ERROR);

        UseCaseException exception = assertThrows(UseCaseException.class, () -> wishInteractor.removeProductFromWishlist(USER_ID, PRODUCT_ID));
        assertEquals(REMOVE_PRODUCT_WISHLIST_ERROR, exception.getMessage());
    }

    @Test
    void should_return_product_not_find_exception_when_generic_error_to_remove_wish() {
        doThrow(new ProductNotFoundException(PRODUCT_NOT_FOUND_ERROR))
                .when(wishGateway).removeWishByUserIdAndProductId(USER_ID, PRODUCT_ID);

        ProductNotFoundException exception = assertThrows(ProductNotFoundException.class, () -> wishInteractor.removeProductFromWishlist(USER_ID, PRODUCT_ID));
        assertEquals(PRODUCT_NOT_FOUND_ERROR, exception.getMessage());
    }

    @ParameterizedTest
    @ValueSource(booleans = {true, false})
    void should_verify_if_product_exists_in_wishlist(final boolean exists) {
        when(wishGateway.existsWishByUserIdAndProductId(USER_ID, PRODUCT_ID)).thenReturn(exists);
        assertEquals(exists, wishInteractor.productIdExistsInWishlist(USER_ID, PRODUCT_ID));
        verify(wishGateway).existsWishByUserIdAndProductId(USER_ID, PRODUCT_ID);
    }

    @Test
    void should_return_use_case_exception_when_error_to_verify_if_exists_product_wishlist() {
        when(wishGateway.existsWishByUserIdAndProductId(USER_ID, PRODUCT_ID)).thenThrow(RuntimeException.class);
        when(messageHelper.getMessage(VERIFY_EXISTS_PRODUCT_WISHLIST_ERROR_CODE)).thenReturn(VERIFY_EXISTS_PRODUCT_WISHLIST_ERROR);

        UseCaseException exception = assertThrows(UseCaseException.class, () -> wishInteractor.productIdExistsInWishlist(USER_ID, PRODUCT_ID));
        assertEquals(VERIFY_EXISTS_PRODUCT_WISHLIST_ERROR, exception.getMessage());
    }

}
