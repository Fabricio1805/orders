package tech.buildrun.btgpactual.orders.controller.dto;

import java.math.BigDecimal;

public record OrderResponseDTO(Long orderId, Long customerId, BigDecimal total) {}
