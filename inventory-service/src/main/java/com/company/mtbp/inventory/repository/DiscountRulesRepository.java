package com.company.mtbp.inventory.repository;

import com.company.mtbp.inventory.entity.DiscountRules;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DiscountRulesRepository extends JpaRepository<DiscountRules, Long> {
    List<DiscountRules> findByActiveTrue();
}