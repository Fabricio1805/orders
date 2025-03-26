package tech.buildrun.btgpactual.orders.controller.dto;

import java.math.BigDecimal;

import tech.buildrun.btgpactual.orders.entity.Order;

public record OrderResponseDTO(Long orderId, Long customerId, BigDecimal total) {

    public static OrderResponseDTO fromEntity(Order order) {
        return new OrderResponseDTO(order.getId(), order.getCustomerId(), order.getTotal());
    }
}
