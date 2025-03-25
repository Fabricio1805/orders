package tech.buildrun.btgpactual.orders.listener.dto;

import java.util.List;

public record OrderCreatedEventDTO(Long orderCode, Long customerCode, List<OrderItemEventDTO> items ) {
    
}
