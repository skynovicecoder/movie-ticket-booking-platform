package com.company.mtbp.inventory.mapper;

import com.company.mtbp.inventory.entity.Booking;
import com.company.mtbp.inventory.events.InventoryCreatedEvent;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface InventoryCreatedEventMapper {
    @Mapping(target = "id", source = "id")
    @Mapping(target = "customer", source = "customer")
    @Mapping(target = "show", source = "show")
    @Mapping(target = "bookingTime", source = "bookingTime")
    @Mapping(target = "totalAmount", source = "totalAmount")
    @Mapping(target = "status", source = "status")
    @Mapping(target = "bookingDetails", source = "bookingDetails")
    InventoryCreatedEvent toEvent(Booking booking);
}