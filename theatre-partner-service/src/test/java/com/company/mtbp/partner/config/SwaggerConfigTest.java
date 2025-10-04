package com.company.mtbp.partner.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class SwaggerConfigTest {
    @Test
    void customOpenAPI_shouldReturnOpenAPIBean() {
        SwaggerConfig swaggerConfig = new SwaggerConfig();
        OpenAPI openAPI = swaggerConfig.customOpenAPI();

        assertNotNull(openAPI, "OpenAPI bean should not be null");

        Info info = openAPI.getInfo();
        assertNotNull(info, "OpenAPI info should not be null");
        assertEquals("MTBP Theatre Partner API", info.getTitle());
        assertEquals("1.0", info.getVersion());
        assertEquals("API documentation for Movie Ticket Booking Platform Theatre Partner Service", info.getDescription());
    }
}