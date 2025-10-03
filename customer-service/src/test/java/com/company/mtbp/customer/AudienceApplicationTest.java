package com.company.mtbp.customer;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@SpringBootTest
class AudienceApplicationTest {

    @Test
    void contextLoads() {
        assertDoesNotThrow(() -> AudienceApplication.main(new String[]{}));
    }
}
