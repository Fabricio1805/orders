package tech.buildrun.btgpactual.orders.service;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import tech.buildrun.btgpactual.orders.controller.dto.OrderResponseDTO;
import tech.buildrun.btgpactual.orders.entity.Order;
import tech.buildrun.btgpactual.orders.entity.OrderItem;
import tech.buildrun.btgpactual.orders.listener.dto.OrderCreatedEventDTO;
import tech.buildrun.btgpactual.orders.repository.OrderRepository;

@Service
public class OrderService {

    private final OrderRepository orderRepository;

    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public void save(OrderCreatedEventDTO eventDTO) {
        Order order = new Order();
        
        order.setId(eventDTO.orderCode());
        order.setCustomerId(eventDTO.customerCode());
        order.setTotal(getTotal(eventDTO));
        order.setItems(getOrderItems(eventDTO));

        orderRepository.save(order);

    }


    public Page<OrderResponseDTO> findAllOrdersByCustomerId(final Long customerId, PageRequest pageRequest) {
        Page<Order> orders = orderRepository.findAllByCustomerId(customerId,pageRequest);

        return orders.map(OrderResponseDTO::fromEntity);

    }

    private BigDecimal getTotal(OrderCreatedEventDTO eventDTO) {
        return eventDTO.items().stream().map(i -> i.price().multiply(BigDecimal.valueOf(i.quantity())))
                .reduce(BigDecimal::add).orElse(BigDecimal.ZERO);
    }

    private List<OrderItem> getOrderItems(OrderCreatedEventDTO eventDTO) {
        return eventDTO.items().stream().map(i -> new OrderItem(i.product(), i.quantity(), i.price())).toList();
    }
}
