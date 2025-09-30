package com.company.mtbp.inventory.entity;

import com.company.mtbp.inventory.enums.ConditionType;
import com.company.mtbp.inventory.enums.DiscountType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Entity
@Table(name = "discount_rules")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DiscountRules {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;        // "Third Ticket Discount"

    @Enumerated(EnumType.STRING)
    private DiscountType discount_type;  // PERCENTAGE, FIXED_AMOUNT

    private Double discount_value;       // 50.0

    @Enumerated(EnumType.STRING)
    private ConditionType conditionType; // TICKET_INDEX, SHOW_TIME, CUSTOMER_TYPE, OTHER

    private String rule_condition;   // JSON string describing the condition

    private Boolean active = true; // Flag to enable/disable rule without deleting

    @ManyToMany
    @JoinTable(
            name = "discount_cities",
            joinColumns = @JoinColumn(name = "discount_id"),
            inverseJoinColumns = @JoinColumn(name = "city_id")
    )
    private Set<City> cities;

    @ManyToMany
    @JoinTable(
            name = "discount_theatres",
            joinColumns = @JoinColumn(name = "discount_id"),
            inverseJoinColumns = @JoinColumn(name = "theatre_id")
    )
    private Set<Theatre> theatres;
}
