package com.company.mtbp.partner.service;

import com.company.mtbp.partner.exception.TheatrePartnerException;
import com.company.mtbp.partner.request.ShowRequest;
import com.company.mtbp.partner.request.ShowUpdateRequest;
import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.retry.Retry;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
@Slf4j
public class TheatreShowService {
    private final RestClient restClient;
    CircuitBreaker circuitBreaker = CircuitBreaker.ofDefaults("theatreShowService");
    private final Retry showServiceRetry;

    public TheatreShowService(RestClient restClient,@Qualifier("theatreShowRetry") Retry showServiceRetry) {
        this.restClient = restClient;
        this.showServiceRetry = showServiceRetry;
    }

    @PostConstruct
    public void init() {
        circuitBreaker.getEventPublisher()
                .onStateTransition(event ->
                        log.info("TheatreShowService CircuitBreaker '{}' state transition: {} -> {}",
                                event.getCircuitBreakerName(),
                                event.getStateTransition().getFromState(),
                                event.getStateTransition().getToState()))
                .onCallNotPermitted(event ->
                        log.warn("TheatreShowService CircuitBreaker '{}' blocked a call!", event.getCircuitBreakerName()))
                .onError(event ->
                        log.error("TheatreShowService CircuitBreaker '{}' recorded an error: {}",
                                event.getCircuitBreakerName(),
                                event.getThrowable().getMessage()));

        showServiceRetry.getEventPublisher()
                .onRetry(event ->
                        log.warn("Resilience4j inside TheatreSeatService Inventory Service retry attempt #{} due to {}",
                                event.getNumberOfRetryAttempts(),
                                event.getLastThrowable().getMessage()));
    }

    public String createShow(ShowRequest showRequest) {
        try {
            return circuitBreaker.executeSupplier(() ->
                    showServiceRetry.executeSupplier(() ->
                            restClient.post()
                                    .uri("/api/v1/shows")
                                    .body(showRequest)
                                    .retrieve()
                                    .body(String.class)
                    )
            );
        } catch (Exception e) {
            log.error("Error calling Show Service API :{}", e.getMessage());
            throw new TheatrePartnerException("Failed to create show :{}" + e.getMessage(), e);
        }
    }

    public String updateShow(Long showId, ShowUpdateRequest request) {
        try {
            return circuitBreaker.executeSupplier(() ->
                    showServiceRetry.executeSupplier(() ->
                            restClient.patch()
                                    .uri("/api/v1/shows/update/{id}", showId)
                                    .body(request)
                                    .retrieve()
                                    .body(String.class)
                    )
            );
        } catch (Exception e) {
            log.error("Error calling Show Update Service API :{}", e.getMessage());
            throw new TheatrePartnerException("Failed to update show :{}" + e.getMessage());
        }
    }

    public void deleteShow(Long id) {
        try {
            circuitBreaker.executeSupplier(() ->
                    showServiceRetry.executeSupplier(() ->
                            restClient
                                    .delete()
                                    .uri("/api/v1/shows/{id}", id)
                                    .retrieve()
                                    .toBodilessEntity()
                    )
            );
        } catch (Exception e) {
            log.error("Error Show Delete Service API :{}", e.getMessage());
            throw new TheatrePartnerException("Failed to delete show with ID " + id + ": " + e.getMessage());
        }
    }
}
