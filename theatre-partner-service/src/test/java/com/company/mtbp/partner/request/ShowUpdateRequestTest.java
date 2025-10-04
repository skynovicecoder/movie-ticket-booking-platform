package com.company.mtbp.partner.request;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.assertj.core.api.Assertions.assertThat;

class ShowUpdateRequestTest {

    @Test
    void testNoArgsConstructorAndSettersGetters() {
        ShowUpdateRequest request = new ShowUpdateRequest();

        request.setShowDate(LocalDate.of(2025, 10, 5));
        request.setStartTime(LocalTime.of(18, 0));
        request.setEndTime(LocalTime.of(20, 30));
        request.setPricePerTicket(250.0);
        request.setShowType("IMAX");

        assertThat(request.getShowDate()).isEqualTo(LocalDate.of(2025, 10, 5));
        assertThat(request.getStartTime()).isEqualTo(LocalTime.of(18, 0));
        assertThat(request.getEndTime()).isEqualTo(LocalTime.of(20, 30));
        assertThat(request.getPricePerTicket()).isEqualTo(250.0);
        assertThat(request.getShowType()).isEqualTo("IMAX");
    }

    @Test
    void testAllArgsConstructor() {
        ShowUpdateRequest request = new ShowUpdateRequest(
                LocalDate.of(2025, 11, 1),
                LocalTime.of(19, 0),
                LocalTime.of(21, 0),
                300.0,
                "3D"
        );

        assertThat(request.getShowDate()).isEqualTo(LocalDate.of(2025, 11, 1));
        assertThat(request.getStartTime()).isEqualTo(LocalTime.of(19, 0));
        assertThat(request.getEndTime()).isEqualTo(LocalTime.of(21, 0));
        assertThat(request.getPricePerTicket()).isEqualTo(300.0);
        assertThat(request.getShowType()).isEqualTo("3D");
    }
}
