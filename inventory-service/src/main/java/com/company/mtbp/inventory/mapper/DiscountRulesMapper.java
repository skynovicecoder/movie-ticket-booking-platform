package com.company.mtbp.inventory.mapper;

import com.company.mtbp.inventory.dto.DiscountRulesDTO;
import com.company.mtbp.inventory.entity.City;
import com.company.mtbp.inventory.entity.DiscountRules;
import com.company.mtbp.inventory.entity.Theatre;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface DiscountRulesMapper {

    @Mapping(source = "discount_type", target = "discountType")
    @Mapping(source = "discount_value", target = "discountValue")
    @Mapping(source = "rule_condition", target = "ruleCondition")
    @Mapping(target = "cityIds", expression = "java(mapCityIds(discountRules.getCities()))")
    @Mapping(target = "theatreIds", expression = "java(mapTheatreIds(discountRules.getTheatres()))")
    DiscountRulesDTO toDTO(DiscountRules discountRules);

    @Mapping(source = "discountType", target = "discount_type")
    @Mapping(source = "discountValue", target = "discount_value")
    @Mapping(source = "ruleCondition", target = "rule_condition")
    @Mapping(target = "cities", ignore = true)
    @Mapping(target = "theatres", ignore = true)
    DiscountRules toEntity(DiscountRulesDTO dto);

    List<DiscountRulesDTO> toDTOList(List<DiscountRules> discounts);

    default Set<Long> mapCityIds(Set<City> cities) {
        if (cities == null) return null;
        return cities.stream().map(City::getId).collect(Collectors.toSet());
    }

    default Set<Long> mapTheatreIds(Set<Theatre> theatres) {
        if (theatres == null) return null;
        return theatres.stream().map(Theatre::getId).collect(Collectors.toSet());
    }
}