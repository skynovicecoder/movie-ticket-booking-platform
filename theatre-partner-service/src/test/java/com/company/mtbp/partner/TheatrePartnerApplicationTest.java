package com.company.mtbp.partner;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class TheatrePartnerApplicationTest {

    @Test
    void contextLoads() {
        assertThat(TheatrePartnerApplication.class).isNotNull();
    }

    @Test
    void mainMethod_ShouldStartApplication() {
        TheatrePartnerApplication.main(new String[]{});
    }
}
