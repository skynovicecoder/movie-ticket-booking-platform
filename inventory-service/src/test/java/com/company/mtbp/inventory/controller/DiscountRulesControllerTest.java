package com.company.mtbp.inventory.controller;

import com.company.mtbp.inventory.dto.DiscountRulesDTO;
import com.company.mtbp.inventory.enums.ConditionType;
import com.company.mtbp.inventory.enums.DiscountType;
import com.company.mtbp.inventory.service.DiscountRulesService;
import com.company.mtbp.shared.dto.PageResponse;
import com.github.javafaker.Faker;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.IntStream;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@Slf4j
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
class DiscountRulesControllerTest {

    @Mock
    private DiscountRulesService discountRulesService;

    @InjectMocks
    private DiscountRulesController discountRulesController;

    private MockMvc mockMvc;
    private Faker faker;
    private DiscountRulesDTO sampleDiscount;

    @BeforeEach
    void setup() {
        faker = new Faker();
        mockMvc = MockMvcBuilders.standaloneSetup(discountRulesController).build();

        sampleDiscount = new DiscountRulesDTO();
        sampleDiscount.setId(1L);
        sampleDiscount.setName("Third Ticket Discount");
        sampleDiscount.setDiscountType(DiscountType.PERCENTAGE);
        sampleDiscount.setDiscountValue(50.0);
        sampleDiscount.setConditionType(ConditionType.TICKET_INDEX);
        sampleDiscount.setRuleCondition("{\"index\":2}");
        sampleDiscount.setActive(true);
    }

    @Test
    void createDiscount_returnsCreatedDiscount() throws Exception {
        Mockito.when(discountRulesService.saveDiscount(any(DiscountRulesDTO.class)))
                .thenReturn(sampleDiscount);

        mockMvc.perform(post("/api/v1/discounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Third Ticket Discount\",\"discountType\":\"PERCENTAGE\",\"discountValue\":50.0,\"conditionType\":\"TICKET_INDEX\",\"ruleCondition\":\"{\\\"index\\\":2}\",\"active\":true}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Third Ticket Discount"))
                .andExpect(jsonPath("$.discountType").value("PERCENTAGE"));
    }

    @Test
    void patchDiscount_returnsUpdatedDiscount() throws Exception {
        Map<String, Object> updates = Map.of("discountValue", 60.0);

        Mockito.when(discountRulesService.getDiscountById(1L))
                .thenReturn(Optional.of(sampleDiscount));

        sampleDiscount.setDiscountValue(60.0);
        Mockito.when(discountRulesService.patchDiscount(eq(sampleDiscount), eq(updates)))
                .thenReturn(sampleDiscount);

        mockMvc.perform(patch("/api/v1/discounts/update/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"discountValue\":60.0}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.discountValue").value(60.0));
    }

    @Test
    void patchDiscount_returnsNotFound() throws Exception {
        Map<String, Object> updates = Map.of("discountValue", 60.0);

        Mockito.when(discountRulesService.getDiscountById(1L))
                .thenReturn(Optional.empty());

        mockMvc.perform(patch("/api/v1/discounts/update/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"discountValue\":60.0}"))
                .andExpect(status().isNotFound());
    }

    @Test
    void getAllDiscounts_returnsPaginatedResponse() throws Exception {
        List<DiscountRulesDTO> discounts = IntStream.range(0, 3)
                .mapToObj(i -> {
                    DiscountRulesDTO dto = new DiscountRulesDTO();
                    dto.setId(faker.number().randomNumber());
                    dto.setName("Discount " + i);
                    dto.setDiscountType(DiscountType.PERCENTAGE);
                    dto.setDiscountValue(10.0 + i);
                    dto.setConditionType(ConditionType.TICKET_INDEX);
                    dto.setRuleCondition("{\"index\":2}");
                    dto.setActive(true);
                    return dto;
                }).toList();

        PageResponse<DiscountRulesDTO> response = new PageResponse<>(
                discounts,
                0, 10,
                discounts.size(),
                1,
                true
        );

        Mockito.when(discountRulesService.getAllDiscounts(0, 10)).thenReturn(response);

        mockMvc.perform(get("/api/v1/discounts")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(discounts.size()))
                .andExpect(jsonPath("$.totalElements").value(discounts.size()))
                .andExpect(jsonPath("$.pageNumber").value(0))
                .andExpect(jsonPath("$.last").value(true));
    }

    @Test
    void getAllDiscounts_returnsEmptyPage() throws Exception {
        PageResponse<DiscountRulesDTO> emptyResponse = new PageResponse<>(
                List.of(),
                0, 10,
                0L, 0,
                true
        );

        Mockito.when(discountRulesService.getAllDiscounts(0, 10)).thenReturn(emptyResponse);

        mockMvc.perform(get("/api/v1/discounts")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(0))
                .andExpect(jsonPath("$.totalElements").value(0))
                .andExpect(jsonPath("$.totalPages").value(0))
                .andExpect(jsonPath("$.pageNumber").value(0))
                .andExpect(jsonPath("$.last").value(true));
    }

    @Test
    void getDiscountById_returnsDiscount() throws Exception {
        Mockito.when(discountRulesService.getDiscountById(1L))
                .thenReturn(Optional.of(sampleDiscount));

        mockMvc.perform(get("/api/v1/discounts/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));
    }

    @Test
    void getDiscountById_returnsNotFound() throws Exception {
        Mockito.when(discountRulesService.getDiscountById(1L))
                .thenReturn(Optional.empty());

        mockMvc.perform(get("/api/v1/discounts/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    void getOffers_returnsOffersForCityOrTheatre() throws Exception {
        List<DiscountRulesDTO> offers = List.of(sampleDiscount);

        Mockito.when(discountRulesService.getOffers(1L, null))
                .thenReturn(offers);

        mockMvc.perform(get("/api/v1/discounts/offers")
                        .param("cityId", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(offers.size()))
                .andExpect(jsonPath("$[0].id").value(1L));
    }

    @Test
    void getOffers_returnsNoContent() throws Exception {
        Mockito.when(discountRulesService.getOffers(null, null))
                .thenReturn(List.of());

        mockMvc.perform(get("/api/v1/discounts/offers"))
                .andExpect(status().isNoContent());
    }
}
