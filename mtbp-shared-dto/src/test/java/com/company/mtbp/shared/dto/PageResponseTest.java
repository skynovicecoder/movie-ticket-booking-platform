package com.company.mtbp.shared.dto;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PageResponseTest {

    @Test
    void constructorAndGetters_shouldInitializeFieldsCorrectly() {
        List<String> content = List.of("A", "B", "C");
        int pageNumber = 1;
        int pageSize = 3;
        long totalElements = 10;
        int totalPages = 4;
        boolean last = false;

        PageResponse<String> pageResponse = new PageResponse<>(
                content, pageNumber, pageSize, totalElements, totalPages, last
        );

        assertEquals(content, pageResponse.getContent());
        assertEquals(pageNumber, pageResponse.getPageNumber());
        assertEquals(pageSize, pageResponse.getPageSize());
        assertEquals(totalElements, pageResponse.getTotalElements());
        assertEquals(totalPages, pageResponse.getTotalPages());
        assertEquals(last, pageResponse.isLast());
    }

    @Test
    void emptyContent_shouldBeAllowed() {
        PageResponse<String> pageResponse = new PageResponse<>(
                List.of(), 0, 10, 0, 0, true
        );

        assertTrue(pageResponse.getContent().isEmpty());
        assertEquals(0, pageResponse.getPageNumber());
        assertEquals(10, pageResponse.getPageSize());
        assertEquals(0, pageResponse.getTotalElements());
        assertEquals(0, pageResponse.getTotalPages());
        assertTrue(pageResponse.isLast());
    }

    @Test
    void setters_shouldUpdateFields() {
        PageResponse<String> pageResponse = new PageResponse<>(
                List.of(), 0, 0, 0, 0, false
        );

        List<String> newContent = List.of("X", "Y");
        pageResponse.setContent(newContent);
        pageResponse.setPageNumber(2);
        pageResponse.setPageSize(5);
        pageResponse.setTotalElements(20);
        pageResponse.setTotalPages(4);
        pageResponse.setLast(true);

        assertEquals(newContent, pageResponse.getContent());
        assertEquals(2, pageResponse.getPageNumber());
        assertEquals(5, pageResponse.getPageSize());
        assertEquals(20, pageResponse.getTotalElements());
        assertEquals(4, pageResponse.getTotalPages());
        assertTrue(pageResponse.isLast());
    }
}
