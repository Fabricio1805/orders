package tech.buildrun.btgpactual.orders.config;

import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMqConfig {
    public static final String ORDER_CREATED_QUEUE = "bgt-pactual-order-created";
}
