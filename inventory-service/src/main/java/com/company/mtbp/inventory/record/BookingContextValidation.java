package com.company.mtbp.inventory.record;

import com.company.mtbp.inventory.dto.CustomerDTO;
import com.company.mtbp.inventory.dto.ShowDTO;

public record BookingContextValidation(CustomerDTO customer, ShowDTO show) {
}
