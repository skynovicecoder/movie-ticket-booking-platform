package com.company.mtbp.inventory.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "discount_rules")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DiscountRule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String ruleType; // THIRD_TICKET, AFTERNOON_SHOW

    private Double value; // 50.0, 20.0

    private String condition; // Optional JSON or enum
}
