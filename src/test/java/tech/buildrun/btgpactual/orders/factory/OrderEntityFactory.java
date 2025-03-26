package tech.buildrun.btgpactual.orders.factory;

import java.math.BigDecimal;
import java.util.List;

import tech.buildrun.btgpactual.orders.entity.Order;
import tech.buildrun.btgpactual.orders.entity.OrderItem;

public class OrderEntityFactory {
    public static Order build() {
        var items = new OrderItem("notebook", 2, BigDecimal.valueOf(2000));

        var order = new Order();

        order.setId(1L);
        order.setCustomerId(1L);
        order.setTotal(BigDecimal.valueOf(4000.00));
        order.setItems(List.of(items));

        return order;
    }   
}
