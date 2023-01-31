package com.example.ecommerce.dto.order;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class CreateOrderRequest {

    @NotNull
    @NotEmpty
    private List<Long> productIds;

}
