package tech.buildrun.btgpactual.orders.factory;

import java.math.BigDecimal;
import java.util.List;

import tech.buildrun.btgpactual.orders.listener.dto.OrderCreatedEventDTO;
import tech.buildrun.btgpactual.orders.listener.dto.OrderItemEventDTO;

public class OrderCreatedEventFactory {
    public static OrderCreatedEventDTO buildWithOneItem() {
        var items = new OrderItemEventDTO("notebook", 1, BigDecimal.valueOf(1000.00));
        var event = new OrderCreatedEventDTO(1L, 1L, List.of(items));

        return event;

    }

    public static OrderCreatedEventDTO buildWithTwoItems() {
        var item1 = new OrderItemEventDTO("notebook", 1, BigDecimal.valueOf(1000.00));
        var item2 = new OrderItemEventDTO("teclado", 1, BigDecimal.valueOf(500.00));

        var event = new OrderCreatedEventDTO(1L, 1L, List.of(item1, item2));

        return event;

    }
}
