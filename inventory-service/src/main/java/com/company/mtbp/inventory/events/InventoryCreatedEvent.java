package com.company.mtbp.inventory.events;

import java.time.LocalDateTime;
import java.util.List;

public record InventoryCreatedEvent(Long id, Long customerId, Long showId, LocalDateTime bookingTime, Double totalAmount, String status, List<Long> bookingDetailIds) {};
