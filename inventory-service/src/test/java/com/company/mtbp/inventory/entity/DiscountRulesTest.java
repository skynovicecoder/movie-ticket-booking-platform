package com.company.mtbp.inventory.entity;

import com.company.mtbp.inventory.enums.ConditionType;
import com.company.mtbp.inventory.enums.DiscountType;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class DiscountRulesTest {

    @Test
    void testNoArgsConstructorAndSetters() {
        DiscountRules rule = new DiscountRules();

        City city = new City();
        Theatre theatre = new Theatre();

        rule.setId(1L);
        rule.setName("Third Ticket Discount");
        rule.setDiscount_type(DiscountType.PERCENTAGE);
        rule.setDiscount_value(50.0);
        rule.setConditionType(ConditionType.TICKET_INDEX);
        rule.setRule_condition("{\"index\":3}");
        rule.setActive(false);
        rule.setCities(Set.of(city));
        rule.setTheatres(Set.of(theatre));

        assertThat(rule.getId()).isEqualTo(1L);
        assertThat(rule.getName()).isEqualTo("Third Ticket Discount");
        assertThat(rule.getDiscount_type()).isEqualTo(DiscountType.PERCENTAGE);
        assertThat(rule.getDiscount_value()).isEqualTo(50.0);
        assertThat(rule.getConditionType()).isEqualTo(ConditionType.TICKET_INDEX);
        assertThat(rule.getRule_condition()).isEqualTo("{\"index\":3}");
        assertThat(rule.getActive()).isFalse();
        assertThat(rule.getCities()).containsExactly(city);
        assertThat(rule.getTheatres()).containsExactly(theatre);
    }

    @Test
    void testAllArgsConstructor() {
        City city = new City();
        Theatre theatre = new Theatre();

        DiscountRules rule = new DiscountRules(
                2L,
                "Morning Show Discount",
                DiscountType.FIXED_AMOUNT,
                100.0,
                ConditionType.SHOW_TIME,
                "{\"time\":\"10:00\"}",
                true,
                Set.of(city),
                Set.of(theatre)
        );

        assertThat(rule.getId()).isEqualTo(2L);
        assertThat(rule.getName()).isEqualTo("Morning Show Discount");
        assertThat(rule.getDiscount_type()).isEqualTo(DiscountType.FIXED_AMOUNT);
        assertThat(rule.getDiscount_value()).isEqualTo(100.0);
        assertThat(rule.getConditionType()).isEqualTo(ConditionType.SHOW_TIME);
        assertThat(rule.getRule_condition()).isEqualTo("{\"time\":\"10:00\"}");
        assertThat(rule.getActive()).isTrue();
        assertThat(rule.getCities()).containsExactly(city);
        assertThat(rule.getTheatres()).containsExactly(theatre);
    }

    @Test
    void testBuilder() {
        City city = new City();
        Theatre theatre = new Theatre();

        DiscountRules rule = DiscountRules.builder()
                .id(3L)
                .name("Student Discount")
                .discount_type(DiscountType.PERCENTAGE)
                .discount_value(25.0)
                .conditionType(ConditionType.CUSTOMER_TYPE)
                .rule_condition("{\"type\":\"student\"}")
                .active(true)
                .cities(Set.of(city))
                .theatres(Set.of(theatre))
                .build();

        assertThat(rule.getId()).isEqualTo(3L);
        assertThat(rule.getName()).isEqualTo("Student Discount");
        assertThat(rule.getDiscount_type()).isEqualTo(DiscountType.PERCENTAGE);
        assertThat(rule.getDiscount_value()).isEqualTo(25.0);
        assertThat(rule.getConditionType()).isEqualTo(ConditionType.CUSTOMER_TYPE);
        assertThat(rule.getRule_condition()).isEqualTo("{\"type\":\"student\"}");
        assertThat(rule.getActive()).isTrue();
        assertThat(rule.getCities()).containsExactly(city);
        assertThat(rule.getTheatres()).containsExactly(theatre);
    }

    @Test
    void testDefaultActiveIsTrue() {
        DiscountRules rule = new DiscountRules();
        assertThat(rule.getActive()).isTrue(); // default
    }

}
