package com.company.mtbp.inventory.mapper;

import com.company.mtbp.inventory.dto.BookingDTO;
import com.company.mtbp.inventory.events.InventoryCreatedEvent;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface InventoryCreatedEventMapper {
    @Mapping(target = "id", source = "id")
    @Mapping(target = "customerId", source = "customerId")
    @Mapping(target = "showId", source = "showId")
    @Mapping(target = "bookingTime", source = "bookingTime")
    @Mapping(target = "totalAmount", source = "totalAmount")
    @Mapping(target = "status", source = "status")
    @Mapping(target = "bookingDetailIds", source = "bookingDetailIds")
    InventoryCreatedEvent toEvent(BookingDTO bookingDto);
}