package tech.buildrun.btgpactual.orders.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

import tech.buildrun.btgpactual.orders.listener.dto.OrderCreatedEventDTO;
import tech.buildrun.btgpactual.orders.service.OrderService;

import static tech.buildrun.btgpactual.orders.config.RabbitMqConfig.ORDER_CREATED_QUEUE;


@Component
public class OrderCreatedListener {
    
    private final Logger logger = LoggerFactory.getLogger(OrderCreatedListener.class);

    private final OrderService orderService;

    
    public OrderCreatedListener(OrderService orderService) {
        this.orderService = orderService;
    }

    @RabbitListener(queues = ORDER_CREATED_QUEUE)
    public void listener(Message<OrderCreatedEventDTO> message) {
        logger.info("Message consumed: {}", message);

        orderService.save(message.getPayload());
    }
}
