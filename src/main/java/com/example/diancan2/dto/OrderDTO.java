package com.example.diancan2.dto;

import lombok.Data;

import java.util.List;

@Data
public class OrderDTO {
    private Long seatId;
    private List<OrderItemDTO> items;

    @Data
    public static class OrderItemDTO {
        private Long foodId;
        private Integer quantity;
    }
}
