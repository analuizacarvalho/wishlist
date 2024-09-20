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
    private static final String EMAIL = "email@gmail.com";
    private static final String DUPLICATE_EMAIL_ERROR_CODE = "error.duplicate.email";
    private static final String DUPLICATE_EMAIL_ERROR = "E-mail já usado em outro cadastro";
    private static final String USER_SAVE_ERROR = "Erro ao salvar usuário";
    private static final String USER_SAVE_ERROR_CODE = "error.save.user";

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
        when(messageHelper.getMessage("error.find.wishlist")).thenReturn("Erro ao buscar Wishlist");
        UseCaseException useCaseException = assertThrows(UseCaseException.class, () -> wishInteractor.findAllWishes(USER_ID));
        assertEquals("Erro ao buscar Wishlist", useCaseException.getMessage());
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
        when(messageHelper.getMessage("error.product.not.found")).thenReturn("Produto não encontrado");

        ProductNotFoundException exception = assertThrows(ProductNotFoundException.class, () -> wishInteractor.addProductToWishlist(USER_ID, PRODUCT_ID));
        assertEquals("Produto não encontrado", exception.getMessage());
    }

    @Test
    void should_return_product_already_in_wishlist_exception_when_product_already_in_wishlist() {
        Product product = mock(Product.class);
        Wish wish = mock(Wish.class);

        when(wish.productId()).thenReturn(PRODUCT_ID);
        when(productGateway.findProductById(PRODUCT_ID)).thenReturn(Optional.of(product));
        when(wishGateway.findAllWishes(USER_ID)).thenReturn(List.of(wish));
        when(messageHelper.getMessage("error.product.already.in.wish.list")).thenReturn("O Produto já está adicionado a sua Wishlist");

        ProductAlreadyInWishlistException exception = assertThrows(ProductAlreadyInWishlistException.class, () -> wishInteractor.addProductToWishlist(USER_ID, PRODUCT_ID));
        assertEquals("O Produto já está adicionado a sua Wishlist", exception.getMessage());
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
        when(messageHelper.getMessage("error.size.wishlist.exceeded")).thenReturn("Limite de 20 produtos excedidos em sua Wishlist");

        MaxListSizeExceededException exception = assertThrows(MaxListSizeExceededException.class, () -> wishInteractor.addProductToWishlist(USER_ID, PRODUCT_ID));
        assertEquals("Limite de 20 produtos excedidos em sua Wishlist", exception.getMessage());
    }

    @Test
    void should_return_use_case_exception_when_generic_error_to_create_wish() {
        Product product = mock(Product.class);

        when(productGateway.findProductById(PRODUCT_ID)).thenReturn(Optional.of(product));
        doThrow(new RuntimeException())
                .when(wishGateway).addWish(any(Wish.class));

        when(wishGateway.findAllWishes(USER_ID)).thenReturn(Lists.newArrayList());
        when(messageHelper.getMessage("error.save.product.wishlist")).thenReturn("Erro ao salvar produto na Wishlist");

        UseCaseException exception = assertThrows(UseCaseException.class, () -> wishInteractor.addProductToWishlist(USER_ID, PRODUCT_ID));
        assertEquals("Erro ao salvar produto na Wishlist", exception.getMessage());
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
        when(messageHelper.getMessage("error.remove.product.wishlist")).thenReturn("Erro ao remover o produto de sua Wishlist");

        UseCaseException exception = assertThrows(UseCaseException.class, () -> wishInteractor.removeProductFromWishlist(USER_ID, PRODUCT_ID));
        assertEquals("Erro ao remover o produto de sua Wishlist", exception.getMessage());
    }

    @Test
    void should_return_product_not_find_exception_when_generic_error_to_remove_wish() {
        doThrow(new ProductNotFoundException("Produto não encontrado"))
                .when(wishGateway).removeWishByUserIdAndProductId(USER_ID, PRODUCT_ID);

        ProductNotFoundException exception = assertThrows(ProductNotFoundException.class, () -> wishInteractor.removeProductFromWishlist(USER_ID, PRODUCT_ID));
        assertEquals("Produto não encontrado", exception.getMessage());
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
        when(messageHelper.getMessage("error.verify.exists.product.wishlist")).thenReturn("Erro ao verificar se o produto existe em sua Wishlist");

        UseCaseException exception = assertThrows(UseCaseException.class, () -> wishInteractor.productIdExistsInWishlist(USER_ID, PRODUCT_ID));
        assertEquals("Erro ao verificar se o produto existe em sua Wishlist", exception.getMessage());
    }

}
