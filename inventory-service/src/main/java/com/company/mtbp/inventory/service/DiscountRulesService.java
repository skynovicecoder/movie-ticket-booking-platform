package com.company.mtbp.inventory.service;

import com.company.mtbp.inventory.dto.DiscountRulesDTO;
import com.company.mtbp.inventory.entity.City;
import com.company.mtbp.inventory.entity.DiscountRules;
import com.company.mtbp.inventory.entity.Theatre;
import com.company.mtbp.inventory.exception.BadRequestException;
import com.company.mtbp.inventory.exception.ResourceNotFoundException;
import com.company.mtbp.inventory.mapper.DiscountRulesMapper;
import com.company.mtbp.inventory.repository.CityRepository;
import com.company.mtbp.inventory.repository.DiscountRulesRepository;
import com.company.mtbp.inventory.repository.TheatreRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.util.*;

@Service
@Slf4j
public class DiscountRulesService {

    private final DiscountRulesRepository discountRulesRepository;
    private final CityRepository cityRepository;
    private final TheatreRepository theatreRepository;
    private final DiscountRulesMapper discountRulesMapper;

    public DiscountRulesService(DiscountRulesRepository discountRulesRepository,
                                CityRepository cityRepository,
                                TheatreRepository theatreRepository,
                                DiscountRulesMapper discountRulesMapper) {
        this.discountRulesRepository = discountRulesRepository;
        this.cityRepository = cityRepository;
        this.theatreRepository = theatreRepository;
        this.discountRulesMapper = discountRulesMapper;
    }

    public DiscountRulesDTO saveDiscount(DiscountRulesDTO dto) {
        DiscountRules entity = discountRulesMapper.toEntity(dto);

        if (dto.getCityIds() != null && !dto.getCityIds().isEmpty()) {
            Set<City> cities = new HashSet<>(cityRepository.findAllById(dto.getCityIds()));
            if (cities.isEmpty())
                throw new ResourceNotFoundException("No cities found for given IDs");
            entity.setCities(cities);
        }

        if (dto.getTheatreIds() != null && !dto.getTheatreIds().isEmpty()) {
            Set<Theatre> theatres = new HashSet<>(theatreRepository.findAllById(dto.getTheatreIds()));
            if (theatres.isEmpty())
                throw new ResourceNotFoundException("No theatres found for given IDs");
            entity.setTheatres(theatres);
        }

        DiscountRules saved = discountRulesRepository.save(entity);
        return discountRulesMapper.toDTO(saved);
    }

    public Optional<DiscountRulesDTO> getDiscountById(Long id) {
        return discountRulesRepository.findById(id)
                .map(discountRulesMapper::toDTO);
    }

    public List<DiscountRulesDTO> getAllDiscounts() {
        List<DiscountRules> list = discountRulesRepository.findAll();
        return discountRulesMapper.toDTOList(list);
    }

    public DiscountRulesDTO patchDiscount(DiscountRulesDTO dto, Map<String, Object> updates) {
        updates.forEach((key, value) -> {
            try {
                Field field = DiscountRulesDTO.class.getDeclaredField(key);
                field.setAccessible(true);

                if (field.getType().equals(Double.class) && value instanceof Number) {
                    field.set(dto, ((Number) value).doubleValue());
                } else if (field.getType().equals(Boolean.class) && value instanceof Boolean) {
                    field.set(dto, value);
                } else if (field.getType().equals(String.class)) {
                    field.set(dto, value.toString());
                } else if (field.getType().equals(Long.class) && value instanceof Number) {
                    field.set(dto, ((Number) value).longValue());
                } else if (field.getType().equals(Set.class) && value instanceof List) {
                    field.set(dto, new HashSet<>((List<?>) value));
                }
            } catch (NoSuchFieldException | IllegalAccessException e) {
                log.warn("Invalid DiscountRulesDTO field: {}", key);
                throw new BadRequestException("Invalid Discount field: " + key);
            }
        });

        return saveDiscount(dto);
    }
}
