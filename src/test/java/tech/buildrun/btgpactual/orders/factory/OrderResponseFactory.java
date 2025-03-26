package tech.buildrun.btgpactual.orders.factory;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import tech.buildrun.btgpactual.orders.controller.dto.OrderResponseDTO;

public class OrderResponseFactory {

    public static Page<OrderResponseDTO> buildWithOneItem() {
        var orderResponse = new OrderResponseDTO(1L, 1L, BigDecimal.valueOf(24.50));

        return new PageImpl<>(List.of(orderResponse));
    }
    
}
