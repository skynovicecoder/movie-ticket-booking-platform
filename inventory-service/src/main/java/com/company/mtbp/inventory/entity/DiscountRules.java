package com.company.mtbp.inventory.entity;

import com.company.mtbp.inventory.enums.ConditionType;
import com.company.mtbp.inventory.enums.DiscountType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "discount_rules")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DiscountRules {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;        // e.g., "Third Ticket Discount"

    @Enumerated(EnumType.STRING)
    private DiscountType discount_type;  // PERCENTAGE, FIXED_AMOUNT

    private Double discount_value;       // e.g., 50.0

    @Enumerated(EnumType.STRING)
    private ConditionType conditionType; // TICKET_INDEX, SHOW_TIME, CUSTOMER_TYPE, OTHER

    private String rule_condition;   // JSON string describing the condition

    private Boolean active = true; // flag to enable/disable rule without deleting
}
