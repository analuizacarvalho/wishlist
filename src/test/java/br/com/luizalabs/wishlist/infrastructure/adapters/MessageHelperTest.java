package br.com.luizalabs.wishlist.infrastructure.adapters;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class MessageHelperTest {

    private static final String MESSAGE = "Message";
    private static final String MESSAGE_CODE = "message.code";

    @InjectMocks
    MessageHelper messageHelper;

    @Mock
    MessageSource messageSource;

    @Test
    void should_get_message() {
        when(messageSource.getMessage(eq(MESSAGE_CODE), any(), any())).thenReturn(MESSAGE);
        assertEquals(MESSAGE, messageHelper.getMessage(MESSAGE_CODE));
    }

}
