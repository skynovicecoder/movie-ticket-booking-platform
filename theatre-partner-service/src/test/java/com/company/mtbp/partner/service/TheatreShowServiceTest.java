package com.company.mtbp.partner.service;

import com.company.mtbp.partner.exception.TheatrePartnerException;
import com.company.mtbp.partner.request.ShowRequest;
import com.company.mtbp.partner.request.ShowUpdateRequest;
import io.github.resilience4j.retry.Retry;
import io.github.resilience4j.retry.Retry.EventPublisher;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.client.RestClient;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TheatreShowServiceTest {

    private RestClient restClientMock;
    private TheatreShowService showService;

    @BeforeEach
    void setup() {
        restClientMock = mock(RestClient.class, RETURNS_DEEP_STUBS);
        Retry retryMock = mock(Retry.class);
        EventPublisher eventPublisherMock = mock(EventPublisher.class);

        when(retryMock.getEventPublisher()).thenReturn(eventPublisherMock);

        when(retryMock.executeSupplier(any())).thenAnswer(invocation ->
                ((java.util.function.Supplier<?>) invocation.getArgument(0)).get()
        );

        showService = new TheatreShowService(restClientMock, retryMock);
        showService.init();
    }

    @Test
    void createShow_ShouldReturnResponse() {
        ShowRequest request = new ShowRequest(1L, 1L, LocalDate.now(), LocalTime.of(10, 0),
                LocalTime.of(12, 0), 200.0, "IMAX");

        when(restClientMock.post().uri("/api/v1/shows").body(request).retrieve().body(String.class))
                .thenReturn("Show created successfully");

        String response = showService.createShow(request);
        assertEquals("Show created successfully", response);
    }

    @Test
    void createShow_ShouldThrowException() {
        ShowRequest request = new ShowRequest();
        when(restClientMock.post().uri("/api/v1/shows").body(request).retrieve().body(String.class))
                .thenThrow(new RuntimeException("Service down"));

        TheatrePartnerException ex = assertThrows(TheatrePartnerException.class,
                () -> showService.createShow(request));

        assertTrue(ex.getMessage().contains("Failed to create show"));
    }

    @Test
    void updateShow_ShouldReturnResponse() {
        ShowUpdateRequest request = new ShowUpdateRequest(LocalDate.now(), LocalTime.of(10, 0),
                LocalTime.of(12, 0), 300.0, "2D");

        when(restClientMock.patch().uri("/api/v1/shows/update/{id}", 1L)
                .body(request).retrieve().body(String.class))
                .thenReturn("Show updated successfully");

        String response = showService.updateShow(1L, request);
        assertEquals("Show updated successfully", response);
    }

    @Test
    void updateShow_ShouldThrowException() {
        ShowUpdateRequest request = new ShowUpdateRequest();
        when(restClientMock.patch().uri("/api/v1/shows/update/{id}", 1L)
                .body(request).retrieve().body(String.class))
                .thenThrow(new RuntimeException("Update failed"));

        TheatrePartnerException ex = assertThrows(TheatrePartnerException.class,
                () -> showService.updateShow(1L, request));

        assertTrue(ex.getMessage().contains("Failed to update show"));
    }

    @Test
    void deleteShow_ShouldCallRestClient() {
        assertDoesNotThrow(() -> showService.deleteShow(1L));
        verify(restClientMock.delete().uri("/api/v1/shows/{id}", 1L).retrieve(), times(1)).toBodilessEntity();
    }

    @Test
    void deleteShow_ShouldThrowException() {
        when(restClientMock.delete().uri("/api/v1/shows/{id}", 1L)
                .retrieve().toBodilessEntity())
                .thenThrow(new RuntimeException("Delete failed"));

        TheatrePartnerException ex = assertThrows(TheatrePartnerException.class,
                () -> showService.deleteShow(1L));

        assertTrue(ex.getMessage().contains("Failed to delete show"));
    }
}
