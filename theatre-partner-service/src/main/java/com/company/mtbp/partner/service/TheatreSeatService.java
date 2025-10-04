package com.company.mtbp.partner.service;

import com.company.mtbp.partner.exception.TheatrePartnerException;
import com.company.mtbp.partner.request.SeatRequest;
import com.company.mtbp.partner.request.SeatUpdateRequest;
import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.retry.Retry;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
@Slf4j
public class TheatreSeatService {
    private final RestClient restClient;
    CircuitBreaker circuitBreaker = CircuitBreaker.ofDefaults("theatreSeatService");
    private final Retry seatServiceRetry;

    public TheatreSeatService(RestClient restClient,@Qualifier("theatreSeatRetry") Retry seatServiceRetry) {
        this.restClient = restClient;
        this.seatServiceRetry = seatServiceRetry;
    }

    @PostConstruct
    public void init() {
        circuitBreaker.getEventPublisher()
                .onStateTransition(event ->
                        log.info("TheatreSeatService CircuitBreaker '{}' state transition: {} -> {}",
                                event.getCircuitBreakerName(),
                                event.getStateTransition().getFromState(),
                                event.getStateTransition().getToState()))
                .onCallNotPermitted(event ->
                        log.warn("TheatreSeatService CircuitBreaker '{}' blocked a call!", event.getCircuitBreakerName()))
                .onError(event ->
                        log.error("TheatreSeatService CircuitBreaker '{}' recorded an error: {}",
                                event.getCircuitBreakerName(),
                                event.getThrowable().getMessage()));

        seatServiceRetry.getEventPublisher()
                .onRetry(event ->
                        log.warn("Resilience4j inside TheatreSeatService Inventory Service retry attempt #{} due to {}",
                                event.getNumberOfRetryAttempts(),
                                event.getLastThrowable().getMessage()));
    }

    public String addSeat(SeatRequest request) {
        try {
            return circuitBreaker.executeSupplier(() ->
                    seatServiceRetry.executeSupplier(() ->
                            restClient.post()
                                    .uri("/api/v1/seats")
                                    .body(request)
                                    .retrieve()
                                    .body(String.class)
                    )
            );

        } catch (Exception e) {
            log.error("Error Seat Create Service API :{}", e.getMessage());
            throw new TheatrePartnerException("Failed to create seat for show Id " + request.getShowId() + " : Due to: " + e.getMessage());
        }
    }

    public String updateSeat(Long theatreId, Long seatId, SeatUpdateRequest request) {
        try {
            return circuitBreaker.executeSupplier(() ->
                    seatServiceRetry.executeSupplier(() ->
                            restClient.patch()
                                    .uri("/api/v1/seats/theatre/{theatreId}/seat/{seatId}", theatreId, seatId)
                                    .body(request)
                                    .retrieve()
                                    .body(String.class)
                    )
            );
        } catch (Exception e) {
            log.error("Error Update Seat Service API :{}", e.getMessage());
            throw new TheatrePartnerException("Failed to update seat for Theatre Id " + theatreId + " : Seat ID: " + seatId + " : Due to: " + e.getMessage());
        }
    }
}
