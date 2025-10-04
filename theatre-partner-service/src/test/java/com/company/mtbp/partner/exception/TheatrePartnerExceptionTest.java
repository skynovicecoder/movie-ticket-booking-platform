package com.company.mtbp.partner.exception;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class TheatrePartnerExceptionTest {

    @Test
    void constructor_ShouldSetMessage() {
        String message = "Custom theatre partner error";
        TheatrePartnerException ex = new TheatrePartnerException(message);

        assertThat(ex).hasMessage(message);
        assertThat(ex.getCause()).isNull();
    }

    @Test
    void constructor_ShouldSetMessageAndCause() {
        String message = "Custom theatre partner error";
        Throwable cause = new RuntimeException("Underlying cause");

        TheatrePartnerException ex = new TheatrePartnerException(message, cause);

        assertThat(ex).hasMessage(message);
        assertThat(ex).hasCause(cause);
    }
}
