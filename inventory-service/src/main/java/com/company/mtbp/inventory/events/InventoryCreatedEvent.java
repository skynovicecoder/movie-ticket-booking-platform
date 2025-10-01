package com.company.mtbp.inventory.events;

import com.company.mtbp.inventory.entity.BookingDetail;
import com.company.mtbp.inventory.entity.Customer;
import com.company.mtbp.inventory.entity.Show;

import java.time.LocalDateTime;
import java.util.List;

public record InventoryCreatedEvent(Long id, Customer customer, Show show, LocalDateTime bookingTime, Double totalAmount, String status, List<BookingDetail> bookingDetails) {};
