package com.company.mtbp.inventory.record;

import com.company.mtbp.inventory.dto.CustomerDTO;
import com.company.mtbp.inventory.dto.ShowDTO;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.*;

class BookingContextValidationTest {

    @Test
    void testRecordAccessors() {
        CustomerDTO customer = CustomerDTO.builder()
                .id(1L)
                .name("John Doe")
                .email("john@example.com")
                .phone("1234567890")
                .build();

        ShowDTO show = new ShowDTO(
                10L,
                100L,
                "Movie Title",
                20L,
                "Theatre Name",
                LocalDate.of(2025, 10, 1),
                LocalTime.of(18, 0),
                LocalTime.of(20, 0),
                250.0,
                "REGULAR"
        );

        BookingContextValidation bookingContext = new BookingContextValidation(customer, show);

        assertEquals(customer, bookingContext.customer());
        assertEquals(show, bookingContext.show());
    }

    @Test
    void testEqualsAndHashCode() {
        CustomerDTO customer = CustomerDTO.builder().id(1L).name("Alice").build();
        ShowDTO show = new ShowDTO(10L, 100L, "Title", 20L, "Theatre", LocalDate.now(), LocalTime.NOON, LocalTime.NOON, 100.0, "REGULAR");

        BookingContextValidation b1 = new BookingContextValidation(customer, show);
        BookingContextValidation b2 = new BookingContextValidation(customer, show);

        assertEquals(b1, b2);
        assertEquals(b1.hashCode(), b2.hashCode());
    }

    @Test
    void testToString() {
        CustomerDTO customer = CustomerDTO.builder().id(1L).name("Bob").build();
        ShowDTO show = new ShowDTO(10L, 100L, "Movie", 20L, "Theatre", LocalDate.now(), LocalTime.NOON, LocalTime.NOON, 150.0, "VIP");

        BookingContextValidation bookingContext = new BookingContextValidation(customer, show);
        String str = bookingContext.toString();

        assertNotNull(str);
    }
}
