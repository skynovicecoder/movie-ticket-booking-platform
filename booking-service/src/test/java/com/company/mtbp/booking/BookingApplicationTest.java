package com.company.mtbp.booking;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatCode;

@SpringBootTest
class BookingApplicationTest {

    @Test
    void contextLoads() {
    }

    @Test
    void mainMethodRunsWithoutException() {
        assertThatCode(() ->
                BookingApplication.main(new String[]{})
        ).doesNotThrowAnyException();
    }
}