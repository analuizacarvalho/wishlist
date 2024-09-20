package br.com.luizalabs.wishlist.infrastructure.exception;

import br.com.luizalabs.wishlist.application.exception.UseCaseException;
import br.com.luizalabs.wishlist.domain.exception.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;

import static org.springframework.http.HttpStatus.*;

@ExtendWith(MockitoExtension.class)
public class RestExceptionHandlerTest {

    private static final String EXCEPTION_ERROR_MESSAGE = "Erro";

    @InjectMocks
    RestExceptionHandler restExceptionHandler;

    @Test
    public void should_handle_max_list_size_exceeded_exception() {
        assertEquals(BAD_REQUEST, restExceptionHandler.handleMaxListSizeExceededException(new MaxListSizeExceededException(EXCEPTION_ERROR_MESSAGE)).getStatusCode());
    }

    @Test
    public void should_handle_product_not_found_exception() {
        assertEquals(BAD_REQUEST, restExceptionHandler.handleProductNotFoundException(new ProductNotFoundException(EXCEPTION_ERROR_MESSAGE)).getStatusCode());
    }

    @Test
    public void should_handle_product_already_in_wishlist_exception() {
        assertEquals(BAD_REQUEST, restExceptionHandler.handleProductAlreadyInWishlistException(new ProductAlreadyInWishlistException(EXCEPTION_ERROR_MESSAGE)).getStatusCode());
    }

    @Test
    public void should_handle_user_already_exists_exception() {
        assertEquals(BAD_REQUEST, restExceptionHandler.handleUserAlreadyExistsException(new UserAlreadyExistsException(EXCEPTION_ERROR_MESSAGE)).getStatusCode());
    }

    @Test
    public void should_handle_use_case_exception() {
        assertEquals(INTERNAL_SERVER_ERROR, restExceptionHandler.handleUseCaseException(new UseCaseException(EXCEPTION_ERROR_MESSAGE)).getStatusCode());
    }

    @Test
    public void should_handle_unauthorized_exception() {
        assertEquals(UNAUTHORIZED, restExceptionHandler.handleUnauthorizedException(new UnauthorizedException(EXCEPTION_ERROR_MESSAGE)).getStatusCode());
    }

}
