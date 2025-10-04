package com.company.mtbp.partner.service;

import com.company.mtbp.partner.exception.TheatrePartnerException;
import com.company.mtbp.partner.request.SeatRequest;
import com.company.mtbp.partner.request.SeatUpdateRequest;
import io.github.resilience4j.retry.Retry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.client.RestClient;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

class TheatreSeatServiceTest {

    private RestClient restClientMock;
    private TheatreSeatService seatService;

    @BeforeEach
    void setup() {
        restClientMock = mock(RestClient.class, RETURNS_DEEP_STUBS);
        Retry retryMock = mock(Retry.class);
        Retry.EventPublisher eventPublisherMock = mock(Retry.EventPublisher.class);

        when(retryMock.getEventPublisher()).thenReturn(eventPublisherMock);

        when(retryMock.executeSupplier(any())).thenAnswer(invocation ->
                ((java.util.function.Supplier<?>) invocation.getArgument(0)).get());

        seatService = new TheatreSeatService(restClientMock, retryMock);
        seatService.init();
    }

    @Test
    void addSeat_ShouldReturnResponse_WhenSuccess() {
        SeatRequest request = new SeatRequest("A1", "VIP", true, 10L, 100L);

        when(restClientMock.post().uri("/api/v1/seats").body(request).retrieve().body(String.class))
                .thenReturn("Seat created successfully");

        String result = seatService.addSeat(request);

        assertThat(result).isEqualTo("Seat created successfully");
        verify(restClientMock.post().uri("/api/v1/seats").body(request).retrieve(), times(1)).body(String.class);
    }

    @Test
    void addSeat_ShouldThrowTheatrePartnerException_WhenRestClientFails() {
        SeatRequest request = new SeatRequest("A1", "VIP", true, 10L, 100L);

        when(restClientMock.post().uri("/api/v1/seats").body(request).retrieve().body(String.class))
                .thenThrow(new RuntimeException("Connection failed"));

        assertThatThrownBy(() -> seatService.addSeat(request))
                .isInstanceOf(TheatrePartnerException.class)
                .hasMessageContaining("Failed to create seat for show Id 100");

        verify(restClientMock.post().uri("/api/v1/seats").body(request).retrieve(), times(1)).body(String.class);
    }

    @Test
    void updateSeat_ShouldReturnResponse_WhenSuccess() {
        SeatUpdateRequest request = new SeatUpdateRequest("A1", "VIP", true);
        Long theatreId = 10L;
        Long seatId = 100L;

        when(restClientMock.patch()
                .uri("/api/v1/seats/theatre/{theatreId}/seat/{seatId}", theatreId, seatId)
                .body(request)
                .retrieve()
                .body(String.class))
                .thenReturn("Seat updated successfully");

        String result = seatService.updateSeat(theatreId, seatId, request);

        assertThat(result).isEqualTo("Seat updated successfully");
        verify(restClientMock.patch()
                .uri("/api/v1/seats/theatre/{theatreId}/seat/{seatId}", theatreId, seatId)
                .body(request)
                .retrieve(), times(1)).body(String.class);
    }

    @Test
    void updateSeat_ShouldThrowTheatrePartnerException_WhenRestClientFails() {
        SeatUpdateRequest request = new SeatUpdateRequest("A1", "VIP", true);
        Long theatreId = 10L;
        Long seatId = 100L;

        when(restClientMock.patch()
                .uri("/api/v1/seats/theatre/{theatreId}/seat/{seatId}", theatreId, seatId)
                .body(request)
                .retrieve()
                .body(String.class))
                .thenThrow(new RuntimeException("Connection failed"));

        assertThatThrownBy(() -> seatService.updateSeat(theatreId, seatId, request))
                .isInstanceOf(TheatrePartnerException.class)
                .hasMessageContaining("Failed to update seat for Theatre Id 10");

        verify(restClientMock.patch()
                .uri("/api/v1/seats/theatre/{theatreId}/seat/{seatId}", theatreId, seatId)
                .body(request)
                .retrieve(), times(1)).body(String.class);
    }
}
