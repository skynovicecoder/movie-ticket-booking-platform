package com.company.mtbp.inventory.service;

import com.company.mtbp.inventory.dto.DiscountRulesDTO;
import com.company.mtbp.inventory.entity.City;
import com.company.mtbp.inventory.entity.DiscountRules;
import com.company.mtbp.inventory.entity.Theatre;
import com.company.mtbp.inventory.enums.ConditionType;
import com.company.mtbp.inventory.enums.DiscountType;
import com.company.mtbp.inventory.exception.BadRequestException;
import com.company.mtbp.inventory.exception.ResourceNotFoundException;
import com.company.mtbp.inventory.mapper.DiscountRulesMapper;
import com.company.mtbp.inventory.pagedto.PageResponse;
import com.company.mtbp.inventory.repository.CityRepository;
import com.company.mtbp.inventory.repository.DiscountRulesRepository;
import com.company.mtbp.inventory.repository.TheatreRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class DiscountRulesServiceTest {

    @Mock
    private DiscountRulesRepository discountRulesRepository;

    @Mock
    private CityRepository cityRepository;

    @Mock
    private TheatreRepository theatreRepository;

    @Mock
    private DiscountRulesMapper discountRulesMapper;

    @InjectMocks
    private DiscountRulesService discountRulesService;

    private DiscountRules sampleDiscount;
    private DiscountRulesDTO sampleDiscountDTO;
    private City sampleCity;
    private Theatre sampleTheatre;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        sampleCity = City.builder().id(1L).name("Mumbai").build();
        sampleTheatre = Theatre.builder().id(1L).name("PVR").build();

        sampleDiscount = DiscountRules.builder()
                .id(1L)
                .active(true)
                .name("Early Bird")
                .cities(Set.of(sampleCity))
                .theatres(Set.of(sampleTheatre))
                .build();

        sampleDiscountDTO = DiscountRulesDTO.builder()
                .id(1L)
                .active(true)
                .name("Early Bird")
                .cityIds(Set.of(1L))
                .theatreIds(Set.of(1L))
                .build();
    }

    @Test
    void saveDiscount_success() {
        when(discountRulesMapper.toEntity(sampleDiscountDTO)).thenReturn(sampleDiscount);
        when(cityRepository.findAllById(Set.of(1L))).thenReturn(List.of(sampleCity));
        when(theatreRepository.findAllById(Set.of(1L))).thenReturn(List.of(sampleTheatre));
        when(discountRulesRepository.save(sampleDiscount)).thenReturn(sampleDiscount);
        when(discountRulesMapper.toDTO(sampleDiscount)).thenReturn(sampleDiscountDTO);

        DiscountRulesDTO result = discountRulesService.saveDiscount(sampleDiscountDTO);

        assertNotNull(result);
        assertEquals("Early Bird", result.getName());
        verify(discountRulesRepository).save(sampleDiscount);
    }

    @Test
    void saveDiscount_cityNotFound_throwsException() {
        when(discountRulesMapper.toEntity(sampleDiscountDTO)).thenReturn(sampleDiscount);
        when(cityRepository.findAllById(Set.of(1L))).thenReturn(Collections.emptyList());

        assertThrows(ResourceNotFoundException.class, () -> discountRulesService.saveDiscount(sampleDiscountDTO));
    }

    @Test
    void saveDiscount_theatreNotFound_throwsException() {
        when(discountRulesMapper.toEntity(sampleDiscountDTO)).thenReturn(sampleDiscount);
        when(cityRepository.findAllById(Set.of(1L))).thenReturn(List.of(sampleCity));
        when(theatreRepository.findAllById(Set.of(1L))).thenReturn(Collections.emptyList());

        assertThrows(ResourceNotFoundException.class, () -> discountRulesService.saveDiscount(sampleDiscountDTO));
    }

    @Test
    void getDiscountById_found() {
        when(discountRulesRepository.findById(1L)).thenReturn(Optional.of(sampleDiscount));
        when(discountRulesMapper.toDTO(sampleDiscount)).thenReturn(sampleDiscountDTO);

        Optional<DiscountRulesDTO> result = discountRulesService.getDiscountById(1L);

        assertTrue(result.isPresent());
        assertEquals("Early Bird", result.get().getName());
    }

    @Test
    void getDiscountById_notFound() {
        when(discountRulesRepository.findById(2L)).thenReturn(Optional.empty());

        Optional<DiscountRulesDTO> result = discountRulesService.getDiscountById(2L);

        assertTrue(result.isEmpty());
    }

    @Test
    void getAllDiscounts_success() {
        Page<DiscountRules> discountPage = new PageImpl<>(List.of(sampleDiscount));
        when(discountRulesRepository.findAll(PageRequest.of(0, 10))).thenReturn(discountPage);
        when(discountRulesMapper.toDTOList(discountPage.getContent())).thenReturn(List.of(sampleDiscountDTO));

        PageResponse<DiscountRulesDTO> result = discountRulesService.getAllDiscounts(0, 10);

        assertNotNull(result);
        assertEquals(1, result.getContent().size());
        assertEquals(sampleDiscountDTO.getName(), result.getContent().getFirst().getName());
        assertEquals(1, result.getTotalElements());
        assertEquals(1, result.getTotalPages());
        assertEquals(0, result.getPageNumber());
        assertTrue(result.isLast());
    }

    @Test
    void patchDiscount_updatesFieldSuccessfully() {
        when(discountRulesMapper.toEntity(sampleDiscountDTO)).thenReturn(sampleDiscount);
        when(cityRepository.findAllById(any())).thenReturn(List.of(sampleCity));
        when(theatreRepository.findAllById(any())).thenReturn(List.of(sampleTheatre));
        when(discountRulesRepository.save(sampleDiscount)).thenReturn(sampleDiscount);
        when(discountRulesMapper.toDTO(sampleDiscount)).thenReturn(sampleDiscountDTO);

        Map<String, Object> updates = Map.of("name", "Super Saver");

        DiscountRulesDTO updated = discountRulesService.patchDiscount(sampleDiscountDTO, updates);

        assertNotNull(updated);
        verify(discountRulesRepository).save(sampleDiscount);
    }

    @Test
    void patchDiscount_invalidField_throwsBadRequest() {
        assertThrows(BadRequestException.class, () -> discountRulesService.patchDiscount(sampleDiscountDTO, Map.of("invalidField", "value")));
    }

    @Test
    void getOffers_byCity_returnsApplicableDiscounts() {
        when(cityRepository.findById(1L)).thenReturn(Optional.of(sampleCity));
        when(discountRulesRepository.findAll()).thenReturn(List.of(sampleDiscount));
        when(discountRulesMapper.toDTOList(anyList())).thenReturn(List.of(sampleDiscountDTO));

        List<DiscountRulesDTO> result = discountRulesService.getOffers(1L, null);

        assertEquals(1, result.size());
    }

    @Test
    void getOffers_byTheatre_returnsApplicableDiscounts() {
        when(theatreRepository.findById(1L)).thenReturn(Optional.of(sampleTheatre));
        when(discountRulesRepository.findAll()).thenReturn(List.of(sampleDiscount));
        when(discountRulesMapper.toDTOList(anyList())).thenReturn(List.of(sampleDiscountDTO));

        List<DiscountRulesDTO> result = discountRulesService.getOffers(null, 1L);

        assertEquals(1, result.size());
    }

    @Test
    void getOffers_allActiveDiscounts_whenNoCityOrTheatre() {
        when(discountRulesRepository.findAll()).thenReturn(List.of(sampleDiscount));
        when(discountRulesMapper.toDTOList(anyList())).thenReturn(List.of(sampleDiscountDTO));

        List<DiscountRulesDTO> result = discountRulesService.getOffers(null, null);

        assertEquals(1, result.size());
    }

    @Test
    void patchDiscount_updatesAllFieldTypesSuccessfully() {
        when(discountRulesMapper.toEntity(sampleDiscountDTO)).thenReturn(sampleDiscount);
        when(cityRepository.findAllById(any())).thenReturn(List.of(sampleCity));
        when(theatreRepository.findAllById(any())).thenReturn(List.of(sampleTheatre));
        when(discountRulesRepository.save(sampleDiscount)).thenReturn(sampleDiscount);
        when(discountRulesMapper.toDTO(sampleDiscount)).thenReturn(sampleDiscountDTO);

        Map<String, Object> updates = Map.of(
                "name", "Super Saver",
                "discountValue", 75.0,
                "active", false,
                "discountType", DiscountType.FIXED_AMOUNT,
                "conditionType", ConditionType.CUSTOMER_TYPE,
                "cityIds", List.of(1L, 2L),
                "theatreIds", List.of(10L, 20L)
        );

        DiscountRulesDTO updated = discountRulesService.patchDiscount(sampleDiscountDTO, updates);

        assertNotNull(updated);

        assertEquals("Super Saver", updated.getName());
        assertEquals(75.0, updated.getDiscountValue());
        assertFalse(updated.getActive());
        assertEquals(Set.of(1L, 2L), updated.getCityIds());
        assertEquals(Set.of(10L, 20L), updated.getTheatreIds());

        verify(discountRulesRepository).save(sampleDiscount);
    }
}
