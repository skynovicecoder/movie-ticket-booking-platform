package com.company.mtbp.inventory.mapper;

import com.company.mtbp.inventory.dto.DiscountRulesDTO;
import com.company.mtbp.inventory.entity.City;
import com.company.mtbp.inventory.entity.DiscountRules;
import com.company.mtbp.inventory.entity.Theatre;
import com.company.mtbp.inventory.enums.DiscountType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class DiscountRulesMapperTest {

    private DiscountRulesMapper mapper;

    @BeforeEach
    void setup() {
        mapper = Mappers.getMapper(DiscountRulesMapper.class);
    }

    @Test
    void toDTO_shouldMapEntityToDTO_withCityAndTheatreIds() {
        City city1 = new City();
        city1.setId(1L);
        City city2 = new City();
        city2.setId(2L);

        Theatre theatre1 = new Theatre();
        theatre1.setId(10L);
        Theatre theatre2 = new Theatre();
        theatre2.setId(20L);

        DiscountRules entity = new DiscountRules();
        entity.setDiscount_type(DiscountType.PERCENTAGE);
        entity.setDiscount_value(15.0);
        entity.setRule_condition("MIN_2_TICKETS");
        entity.setCities(Set.of(city1, city2));
        entity.setTheatres(Set.of(theatre1, theatre2));

        DiscountRulesDTO dto = mapper.toDTO(entity);

        assertNotNull(dto);
        assertEquals(DiscountType.PERCENTAGE, dto.getDiscountType());
        assertEquals(15.0, dto.getDiscountValue());
        assertEquals("MIN_2_TICKETS", dto.getRuleCondition());
        assertEquals(Set.of(1L, 2L), dto.getCityIds());
        assertEquals(Set.of(10L, 20L), dto.getTheatreIds());
    }

    @Test
    void toEntity_shouldMapDTOToEntity_ignoreCitiesAndTheatres() {
        DiscountRulesDTO dto = new DiscountRulesDTO();
        dto.setDiscountType(DiscountType.FIXED_AMOUNT);
        dto.setDiscountValue(100.0);
        dto.setRuleCondition("MIN_3_TICKETS");
        dto.setCityIds(Set.of(1L, 2L));
        dto.setTheatreIds(Set.of(10L, 20L));

        DiscountRules entity = mapper.toEntity(dto);

        assertNotNull(entity);
        assertEquals(DiscountType.FIXED_AMOUNT, entity.getDiscount_type());
        assertEquals(100.0, entity.getDiscount_value());
        assertEquals("MIN_3_TICKETS", entity.getRule_condition());
        // cities and theatres are ignored
        assertNull(entity.getCities());
        assertNull(entity.getTheatres());
    }

    @Test
    void toDTOList_shouldMapListOfEntitiesToDTOs() {
        DiscountRules entity1 = new DiscountRules();
        entity1.setDiscount_type(DiscountType.PERCENTAGE);
        entity1.setDiscount_value(10.0);

        DiscountRules entity2 = new DiscountRules();
        entity2.setDiscount_type(DiscountType.FIXED_AMOUNT);
        entity2.setDiscount_value(50.0);

        List<DiscountRulesDTO> dtoList = mapper.toDTOList(List.of(entity1, entity2));

        assertNotNull(dtoList);
        assertEquals(2, dtoList.size());
        assertEquals(DiscountType.PERCENTAGE, dtoList.get(0).getDiscountType());
        assertEquals(DiscountType.FIXED_AMOUNT, dtoList.get(1).getDiscountType());
    }

    @Test
    void mapCityIds_shouldReturnNullForNullInput() {
        assertNull(mapper.mapCityIds(null));
    }

    @Test
    void mapTheatreIds_shouldReturnNullForNullInput() {
        assertNull(mapper.mapTheatreIds(null));
    }

    @Test
    void mapCityIds_shouldMapCitySetToIdSet() {
        City city1 = new City();
        city1.setId(1L);
        City city2 = new City();
        city2.setId(2L);
        Set<Long> ids = mapper.mapCityIds(Set.of(city1, city2));
        assertEquals(Set.of(1L, 2L), ids);
    }

    @Test
    void mapTheatreIds_shouldMapTheatreSetToIdSet() {
        Theatre theatre1 = new Theatre();
        theatre1.setId(10L);
        Theatre theatre2 = new Theatre();
        theatre2.setId(20L);
        Set<Long> ids = mapper.mapTheatreIds(Set.of(theatre1, theatre2));
        assertEquals(Set.of(10L, 20L), ids);
    }
}
