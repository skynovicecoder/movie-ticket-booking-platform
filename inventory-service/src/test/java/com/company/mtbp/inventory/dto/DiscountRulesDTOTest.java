package com.company.mtbp.inventory.dto;

import com.company.mtbp.inventory.enums.ConditionType;
import com.company.mtbp.inventory.enums.DiscountType;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class DiscountRulesDTOTest {

    @Test
    void testAllArgsConstructorAndGetters() {
        Set<Long> cityIds = new HashSet<>(Set.of(1L, 2L));
        Set<Long> theatreIds = new HashSet<>(Set.of(10L, 20L));

        DiscountRulesDTO dto = new DiscountRulesDTO(
                100L,
                "Super Saver",
                DiscountType.PERCENTAGE,
                50.0,
                ConditionType.CUSTOMER_TYPE,
                "{\"minTickets\":2}",
                true,
                cityIds,
                theatreIds
        );

        assertEquals(100L, dto.getId());
        assertEquals("Super Saver", dto.getName());
        assertEquals(DiscountType.PERCENTAGE, dto.getDiscountType());
        assertEquals(50.0, dto.getDiscountValue());
        assertEquals(ConditionType.CUSTOMER_TYPE, dto.getConditionType());
        assertEquals("{\"minTickets\":2}", dto.getRuleCondition());
        assertTrue(dto.getActive());
        assertEquals(cityIds, dto.getCityIds());
        assertEquals(theatreIds, dto.getTheatreIds());
    }

    @Test
    void testSetters() {
        DiscountRulesDTO dto = new DiscountRulesDTO();
        Set<Long> cityIds = new HashSet<>(Set.of(3L, 4L));
        Set<Long> theatreIds = new HashSet<>(Set.of(30L, 40L));

        dto.setId(101L);
        dto.setName("Mega Discount");
        dto.setDiscountType(DiscountType.FIXED_AMOUNT);
        dto.setDiscountValue(100.0);
        dto.setConditionType(ConditionType.SHOW_TIME);
        dto.setRuleCondition("{\"minAmount\":500}");
        dto.setActive(false);
        dto.setCityIds(cityIds);
        dto.setTheatreIds(theatreIds);

        assertEquals(101L, dto.getId());
        assertEquals("Mega Discount", dto.getName());
        assertEquals(DiscountType.FIXED_AMOUNT, dto.getDiscountType());
        assertEquals(100.0, dto.getDiscountValue());
        assertEquals(ConditionType.SHOW_TIME, dto.getConditionType());
        assertEquals("{\"minAmount\":500}", dto.getRuleCondition());
        assertFalse(dto.getActive());
        assertEquals(cityIds, dto.getCityIds());
        assertEquals(theatreIds, dto.getTheatreIds());
    }

    @Test
    void testBuilder() {
        Set<Long> cityIds = Set.of(5L, 6L);
        Set<Long> theatreIds = Set.of(50L, 60L);

        DiscountRulesDTO dto = DiscountRulesDTO.builder()
                .id(102L)
                .name("Flash Sale")
                .discountType(DiscountType.PERCENTAGE)
                .discountValue(25.0)
                .conditionType(ConditionType.TICKET_INDEX)
                .ruleCondition("{\"maxTickets\":5}")
                .active(true)
                .cityIds(cityIds)
                .theatreIds(theatreIds)
                .build();

        assertEquals(102L, dto.getId());
        assertEquals("Flash Sale", dto.getName());
        assertEquals(DiscountType.PERCENTAGE, dto.getDiscountType());
        assertEquals(25.0, dto.getDiscountValue());
        assertEquals(ConditionType.TICKET_INDEX, dto.getConditionType());
        assertEquals("{\"maxTickets\":5}", dto.getRuleCondition());
        assertTrue(dto.getActive());
        assertEquals(cityIds, dto.getCityIds());
        assertEquals(theatreIds, dto.getTheatreIds());
    }

}
