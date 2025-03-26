package tech.buildrun.btgpactual.orders.service;

import java.math.BigDecimal;
import java.util.List;

import org.bson.Document;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Service;

import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;

import tech.buildrun.btgpactual.orders.controller.dto.OrderResponseDTO;
import tech.buildrun.btgpactual.orders.entity.Order;
import tech.buildrun.btgpactual.orders.entity.OrderItem;
import tech.buildrun.btgpactual.orders.listener.dto.OrderCreatedEventDTO;
import tech.buildrun.btgpactual.orders.repository.OrderRepository;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final MongoTemplate mongoTemplate;

    public OrderService(OrderRepository orderRepository, MongoTemplate mongoTemplate) {
        this.orderRepository = orderRepository;
        this.mongoTemplate = mongoTemplate;
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

    public BigDecimal findTotalOnOrdersByCustomerId(final Long customerId) {
        var aggregations = Aggregation.newAggregation(
                Aggregation.match(Criteria.where("customerId").is(customerId)),
                Aggregation.group().sum("total").as("total")
        );

        AggregationResults<Document> response = mongoTemplate.aggregate(aggregations, "tb_orders", Document.class);
    
        return new BigDecimal(response.getUniqueMappedResult().get("total").toString());
    }

    private BigDecimal getTotal(OrderCreatedEventDTO eventDTO) {
        return eventDTO.items().stream().map(i -> i.price().multiply(BigDecimal.valueOf(i.quantity())))
                .reduce(BigDecimal::add).orElse(BigDecimal.ZERO);
    }

    private List<OrderItem> getOrderItems(OrderCreatedEventDTO eventDTO) {
        return eventDTO.items().stream().map(i -> new OrderItem(i.product(), i.quantity(), i.price())).toList();
    }
}
