package tech.buildrun.btgpactual.orders.listener.dto;

import java.math.BigDecimal;

public record OrderItemEventDTO(String product, Integer quantity, BigDecimal price) {
    
}
