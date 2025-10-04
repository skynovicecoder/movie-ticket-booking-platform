package com.company.mtbp.partner.exception;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.support.WebExchangeBindException;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class GlobalExceptionHandlerTest {

    private MockMvc mockMvc;
    GlobalExceptionHandler exceptionHandler = new GlobalExceptionHandler();

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(new TestController())
                .setControllerAdvice(exceptionHandler)
                .build();
    }

    @RestController
    static class TestController {

        @GetMapping("/theatre-partner-exception")
        public void throwTheatrePartnerException() {
            throw new TheatrePartnerException("Theatre partner conflict!");
        }

        @GetMapping("/general-exception")
        public void throwGeneralException() {
            throw new RuntimeException("Some general error");
        }

        @GetMapping("/validation-exception")
        public void throwValidationException() throws WebExchangeBindException {
            BindingResult bindingResult = mock(BindingResult.class);
            FieldError error = new FieldError("showRequest", "movieId", "must not be null");
            when(bindingResult.getFieldErrors()).thenReturn(List.of(error));
            throw new WebExchangeBindException(null, bindingResult);
        }
    }

    @Test
    void handleTheatrePartnerException_ShouldReturnProblemDetailWith500() throws Exception {
        mockMvc.perform(get("/theatre-partner-exception"))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.title").value("Theatre Partners Service Error"))
                .andExpect(jsonPath("$.detail").value(containsString("Theatre partner conflict!")))
                .andExpect(jsonPath("$.type").value("urn:problem-type:theatre-partner-service-problem"));
    }

    @Test
    void handleGeneralException_ShouldReturnProblemDetailWith500() throws Exception {
        mockMvc.perform(get("/general-exception"))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.title").value("Theatre Partner Service Error"))
                .andExpect(jsonPath("$.detail").value(containsString("Some general error")))
                .andExpect(jsonPath("$.type").value("urn:problem-type:theatre-partner-service-problem"));
    }

    @Test
    void handleValidationException_ShouldReturnBadRequestWithFieldError() throws Exception {
        BindingResult bindingResult = mock(BindingResult.class);
        FieldError fieldError = new FieldError("showRequest", "movieId", "must not be null");
        when(bindingResult.getFieldErrors()).thenReturn(List.of(fieldError));

        WebExchangeBindException ex = new WebExchangeBindException(null, bindingResult);

        var response = exceptionHandler.handleValidationException(ex);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).contains("movieId: must not be null");
    }
}
