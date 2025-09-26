package com.company.mtbp.inventory.repository;

import com.company.mtbp.inventory.entity.DiscountRule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DiscountRuleRepository extends JpaRepository<DiscountRule, Long> {

    Optional<DiscountRule> findByRuleType(String ruleType);
}