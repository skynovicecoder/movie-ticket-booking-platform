package com.company.mtbp.payment;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@SpringBootTest
class PaymentApplicationTest {

    @Test
    void contextLoads() {
    }

    @Test
    void mainMethodRuns() {
        assertDoesNotThrow(() -> PaymentApplication.main(new String[]{}));
    }
}