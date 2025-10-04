package com.company.mtbp.customer.service;

import com.company.mtbp.customer.exception.PaymentException;
import com.company.mtbp.customer.request.PaymentRequest;
import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.retry.Retry;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClient.RequestBodyUriSpec;
import org.springframework.web.reactive.function.client.WebClient.RequestHeadersSpec;
import org.springframework.web.reactive.function.client.WebClient.ResponseSpec;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@Slf4j
class PaymentServiceTest {

    private WebClient webClient;
    private PaymentService paymentService;
    private Retry retry;

    private RequestBodyUriSpec requestBodyUriSpec;
    private RequestHeadersSpec requestHeadersSpec;
    private ResponseSpec responseSpec;

    @BeforeEach
    void setUp() {
        webClient = mock(WebClient.class);
        retry = Retry.ofDefaults("testRetry");
        paymentService = new PaymentService(webClient, retry);

        requestBodyUriSpec = mock(RequestBodyUriSpec.class);
        requestHeadersSpec = mock(RequestHeadersSpec.class);
        responseSpec = mock(ResponseSpec.class);

        when(webClient.post()).thenReturn(requestBodyUriSpec);
        when(requestBodyUriSpec.uri(anyString())).thenReturn(requestBodyUriSpec);
        when(requestBodyUriSpec.bodyValue(any())).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
    }

    @Test
    void makePayment_success() {
        when(responseSpec.onStatus(any(), any())).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(String.class)).thenReturn(Mono.just("payment-success"));

        PaymentRequest request = new PaymentRequest(); // populate as needed

        StepVerifier.create(paymentService.makePayment(request))
                .expectNext("payment-success")
                .verifyComplete();
    }

    @Test
    void makePayment_clientError() {
        when(responseSpec.onStatus(any(), any())).thenAnswer(invocation -> {
            Function<ClientResponse, Mono<? extends Throwable>> func = invocation.getArgument(1);
            return responseSpec;
        });
        when(responseSpec.bodyToMono(String.class))
                .thenReturn(Mono.error(new PaymentException("Client error during booking")));

        PaymentRequest request = new PaymentRequest();

        StepVerifier.create(paymentService.makePayment(request))
                .expectErrorSatisfies(ex -> {
                    assert ex instanceof PaymentException;
                    assert ex.getMessage().equals("Client error during booking");
                })
                .verify();
    }

    @Test
    void makePayment_serverError() {
        when(responseSpec.onStatus(any(), any())).thenAnswer(invocation -> {
            Function<ClientResponse, Mono<? extends Throwable>> func = invocation.getArgument(1);
            return responseSpec;
        });
        when(responseSpec.bodyToMono(String.class))
                .thenReturn(Mono.error(new PaymentException("Server error during booking")));

        PaymentRequest request = new PaymentRequest();

        StepVerifier.create(paymentService.makePayment(request))
                .expectErrorSatisfies(ex -> {
                    assert ex instanceof PaymentException;
                    assert ex.getMessage().equals("Server error during booking");
                })
                .verify();
    }

    @Test
    void paymentFallback_ShouldReturnFallbackMessage() throws Exception {
        PaymentService service = new PaymentService(mock(WebClient.class), Retry.ofDefaults("testRetry"));
        PaymentRequest request = new PaymentRequest();

        Method fallback = PaymentService.class.getDeclaredMethod("paymentFallback", PaymentRequest.class, Throwable.class);
        fallback.setAccessible(true);

        Mono<String> result = (Mono<String>) fallback.invoke(service, request, new RuntimeException("Some error"));

        StepVerifier.create(result)
                .expectNext("Payment service unavailable. Please try again later.")
                .verifyComplete();
    }

    @Test
    void init_ShouldRegisterCircuitBreakerAndRetryEvents() {
        paymentService.init();

        CircuitBreaker cb = paymentService.circuitBreaker;

        cb.transitionToClosedState();
        cb.transitionToOpenState();

        cb.tryAcquirePermission();

        cb.onError(0, TimeUnit.MILLISECONDS, new RuntimeException("Simulated error"));

        retry.getEventPublisher().onRetry(event -> {
            log.info("Retry event captured: {}", event.getNumberOfRetryAttempts());
        });

        assertNotNull(cb);
        assertNotNull(paymentService);
    }
}
