package com.company.mtbp.inventory.integrationTests.read;

import com.company.mtbp.inventory.entity.City;
import com.company.mtbp.inventory.entity.DiscountRules;
import com.company.mtbp.inventory.entity.Theatre;
import com.company.mtbp.inventory.enums.ConditionType;
import com.company.mtbp.inventory.enums.DiscountType;
import com.company.mtbp.inventory.repository.CityRepository;
import com.company.mtbp.inventory.repository.DiscountRulesRepository;
import com.company.mtbp.inventory.repository.TheatreRepository;
import com.company.mtbp.inventory.service.DiscountRulesService;
import com.github.javafaker.Faker;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.IntStream;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Slf4j
@ActiveProfiles("test")
class DiscountOffersReadScenariosTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private DiscountRulesService discountRulesService;

    @MockitoBean
    private DiscountRulesRepository discountRulesRepository;

    @MockitoBean
    private CityRepository cityRepository;

    @MockitoBean
    private TheatreRepository theatreRepository;

    private Faker faker;

    @BeforeEach
    void setup() {
        faker = new Faker();
    }

    @Test
    void getOffers_returnsDiscounts() throws Exception {
        Long cityId = 1L;
        Long theatreId = 10L;

        City city = City.builder()
                .id(cityId)
                .name("Mumbai")
                .build();

        Theatre theatre = Theatre.builder()
                .id(theatreId)
                .name("PVR")
                .city(city)
                .build();

        List<DiscountRules> discountRulesList = IntStream.range(0, 3)
                .mapToObj(i -> DiscountRules.builder()
                        .id((long) i + 1)
                        .name("Offer " + i)
                        .discount_type(faker.options().option(DiscountType.class))
                        .discount_value(faker.number().randomDouble(2, 10, 50))
                        .conditionType(faker.options().option(ConditionType.class))
                        .active(true)
                        .cities(Set.of(city))
                        .theatres(Set.of(theatre))
                        .build())
                .toList();

        Mockito.when(cityRepository.findById(cityId)).thenReturn(Optional.of(city));
        Mockito.when(theatreRepository.findById(theatreId)).thenReturn(Optional.of(theatre));
        Mockito.when(discountRulesRepository.findAll()).thenReturn(discountRulesList);

        mockMvc.perform(get("/api/v1/discounts/offers")
                        .param("cityId", cityId.toString())
                        .param("theatreId", theatreId.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(discountRulesList.size()));
    }

    @Test
    void getOffers_returnsNoContentWhenEmpty() throws Exception {
        Long cityId = 2L;
        Long theatreId = 20L;

        Mockito.when(cityRepository.findById(cityId)).thenReturn(Optional.empty());
        Mockito.when(theatreRepository.findById(theatreId)).thenReturn(Optional.empty());
        Mockito.when(discountRulesRepository.findAll()).thenReturn(List.of());

        mockMvc.perform(get("/api/v1/discounts/offers")
                        .param("cityId", cityId.toString())
                        .param("theatreId", theatreId.toString()))
                .andExpect(status().isNoContent());
    }

    @Test
    void getOffers_returnsAllWhenNoParams() throws Exception {
        List<DiscountRules> discountRulesList = IntStream.range(0, 2)
                .mapToObj(i -> DiscountRules.builder()
                        .id((long) i + 1)
                        .name("Global Offer " + i)
                        .discount_type(faker.options().option(DiscountType.class))
                        .discount_value(faker.number().randomDouble(2, 5, 30))
                        .conditionType(faker.options().option(ConditionType.class))
                        .active(true)
                        .build())
                .toList();

        Mockito.when(discountRulesRepository.findAll()).thenReturn(discountRulesList);

        mockMvc.perform(get("/api/v1/discounts/offers"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(discountRulesList.size()));
    }


}
