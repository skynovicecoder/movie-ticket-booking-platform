package com.company.mtbp.inventory.component;

import com.company.mtbp.inventory.entity.DiscountRules;
import com.company.mtbp.inventory.enums.ConditionType;
import com.company.mtbp.inventory.enums.DiscountType;
import com.company.mtbp.inventory.repository.DiscountRulesRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class DiscountRulesInitialization {

    private final DiscountRulesRepository discountRulesRepository;

    @PostConstruct
    public void init() {
        if (discountRulesRepository.count() == 0) {
            DiscountRules thirdTicket = new DiscountRules();
            thirdTicket.setName("Third Ticket Discount");
            thirdTicket.setDiscount_type(DiscountType.PERCENTAGE);
            thirdTicket.setDiscount_value(50.0);
            thirdTicket.setConditionType(ConditionType.TICKET_INDEX);
            thirdTicket.setRule_condition("{\"index\":2}");
            thirdTicket.setActive(true);

            DiscountRules afternoonShow = new DiscountRules();
            afternoonShow.setName("Afternoon Show");
            afternoonShow.setDiscount_type(DiscountType.PERCENTAGE);
            afternoonShow.setDiscount_value(20.0);
            afternoonShow.setConditionType(ConditionType.SHOW_TIME);
            afternoonShow.setRule_condition("{\"from\":\"12:00\",\"to\":\"16:00\"}");
            afternoonShow.setActive(true);

            discountRulesRepository.saveAll(List.of(thirdTicket, afternoonShow));
            log.info("Discount rules initialized successfully...!!!");
        }
    }
}
