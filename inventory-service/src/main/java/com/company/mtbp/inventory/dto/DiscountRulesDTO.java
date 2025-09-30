package com.company.mtbp.inventory.dto;

import com.company.mtbp.inventory.enums.ConditionType;
import com.company.mtbp.inventory.enums.DiscountType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DiscountRulesDTO {

    private Long id;

    private String name;

    private DiscountType discountType;  // PERCENTAGE, FIXED_AMOUNT

    private Double discountValue;       // 50.0

    private ConditionType conditionType; // TICKET_INDEX, SHOW_TIME, CUSTOMER_TYPE, OTHER

    private String ruleCondition;   // JSON string describing the condition

    private Boolean active = true;

    // Only IDs of related entities
    private Set<Long> cityIds;
    private Set<Long> theatreIds;
}