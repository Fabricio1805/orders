package tech.buildrun.btgpactual.orders.listener;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.messaging.support.MessageBuilder;

import tech.buildrun.btgpactual.orders.factory.OrderCreatedEventFactory;
import tech.buildrun.btgpactual.orders.service.OrderService;

@ExtendWith(MockitoExtension.class)
public class OrderCreatedListenerTest {

    @Mock
    OrderService orderService;

    @InjectMocks
    OrderCreatedListener orderCreatedListener;


    @Nested
    class Listener {

        @Test
        void shouldCallServiceWithCorrectParameters() {
            // Arrange
            var event = OrderCreatedEventFactory.buildWithOneItem();
            var message = MessageBuilder.withPayload(event).build();

            // ACT
            orderCreatedListener.listener(message);

            // ASSERT
            verify(orderService, times(1)).save(eq(message.getPayload()));
        }

    }
}
