package tech.buildrun.btgpactual.orders.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.math.BigDecimal;

import org.bson.Document;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;

import tech.buildrun.btgpactual.orders.entity.Order;
import tech.buildrun.btgpactual.orders.factory.OrderCreatedEventFactory;
import tech.buildrun.btgpactual.orders.factory.OrderEntityFactory;
import tech.buildrun.btgpactual.orders.repository.OrderRepository;

@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {

    @Mock
    OrderRepository orderRepository;

    @Mock
    MongoTemplate mongoTemplate;

    @InjectMocks
    OrderService orderService;

    @Captor
    ArgumentCaptor<Order> orderCaptor;

    @Captor
    ArgumentCaptor<Aggregation> aggArgumentCaptor;

    @Nested
    class Save {

        @Test
        @DisplayName("deve salvar um pedido com sucesso!")
        void shouldCallRepositorySave() {
            // Arrange
            var event = OrderCreatedEventFactory.buildWithOneItem();

            // ACT
            orderService.save(event);

            // ASSERT
            verify(orderRepository, times(1)).save(any());
        }

        @Test
        @DisplayName("")
        void shouldMapEventToEventWithSuccess() {
            // Arrange
            var event = OrderCreatedEventFactory.buildWithOneItem();

            // ACT
            orderService.save(event);

            // ASSERT
            verify(orderRepository, times(1)).save(orderCaptor.capture());

            var entity = orderCaptor.getValue();
            assertEquals(event.customerCode(), entity.getCustomerId());
            assertEquals(event.orderCode(), entity.getId());
            assertNotNull(entity.getTotal());
            assertEquals(event.items().getFirst().product(), entity.getItems().getFirst().getProduct());
            assertEquals(event.items().getFirst().quantity(), entity.getItems().getFirst().getQuantity());
            assertEquals(event.items().getFirst().price(), entity.getItems().getFirst().getPrice());
        }

        @Test
        @DisplayName("")
        void shouldCalculateOrderTotalWithSuccess() {
            // Arrange
            var event = OrderCreatedEventFactory.buildWithTwoItems();

            var orderTotal = event.items().stream()
                    .map(i -> i.price().multiply(BigDecimal.valueOf(i.quantity())))
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            // ACT
            orderService.save(event);

            // ASSERT
            verify(orderRepository, times(1)).save(orderCaptor.capture());

            var entity = orderCaptor.getValue();

            assertNotNull(entity.getTotal());
            assertEquals(orderTotal, entity.getTotal());

        }
    }

    @Nested
    class FindAllOrdersByCustomerId {

        @Test
        @DisplayName("")
        void shouldCallRepository() {
            // Arrange
            var customerId = 1L;
            var pageRequest = PageRequest.of(0, 10);

            doReturn(OrderEntityFactory.buildWithPage()).when(orderRepository).findAllByCustomerId(eq(customerId),
                    eq(pageRequest));

            // ACT
            orderService.findAllOrdersByCustomerId(customerId, pageRequest);

            // ASSERT
            verify(orderRepository, times(1)).findAllByCustomerId(eq(customerId), eq(pageRequest));
        }

        @Test
        @DisplayName("")
        void shouldMapResponseSuccess() {
            // Arrange
            var customerId = 1L;
            var pageRequest = PageRequest.of(0, 10);
            var page = OrderEntityFactory.buildWithPage();

            doReturn(page).when(orderRepository).findAllByCustomerId(anyLong(), any());

            // ACT
            var response = orderService.findAllOrdersByCustomerId(customerId, pageRequest);

            // ASSERT
            assertEquals(page.getTotalPages(), response.getTotalPages());
            assertEquals(page.getTotalElements(), response.getTotalElements());
            assertEquals(page.getSize(), response.getSize());
            assertEquals(page.getNumber(), response.getNumber());

            assertEquals(page.getContent().getFirst().getId(), response.getContent().getFirst().orderId());
            assertEquals(page.getContent().getFirst().getCustomerId(), response.getContent().getFirst().customerId());
            assertEquals(page.getContent().getFirst().getTotal(), response.getContent().getFirst().total());
        }
    }

    @Nested
    class FindTotalOnOrdersByCustomerId {
        @Test
        void shouldCallMongoTemplate() {
            // Arrange
            var customerId = 1L;
            var totalExpected = BigDecimal.valueOf(1);

            var aggregationResult = mock(AggregationResults.class);
            doReturn(aggregationResult).when(mongoTemplate).aggregate(any(Aggregation.class), anyString(),
                    eq(Document.class));
            doReturn(new Document("total", 1)).when(aggregationResult).getUniqueMappedResult();

            // ACT
            var totalOrders = orderService.findTotalOnOrdersByCustomerId(customerId);

            // ASSERT
            verify(mongoTemplate, times(1)).aggregate(any(Aggregation.class), anyString(), eq(Document.class));
            assertEquals(totalExpected, totalOrders);
        }

        @Test
        void shouldUseCorrectAggregation() {
            // Arrange
            var customerId = 1L;

            var aggregationResult = mock(AggregationResults.class);
            doReturn(aggregationResult).when(mongoTemplate).aggregate(aggArgumentCaptor.capture(), anyString(),
                    eq(Document.class));
            doReturn(new Document("total", 1)).when(aggregationResult).getUniqueMappedResult();

            // ACT
            orderService.findTotalOnOrdersByCustomerId(customerId);

            // ASSERT
            var aggregation = aggArgumentCaptor.getValue();
            var aggregationExpected = Aggregation.newAggregation(
                    Aggregation.match(Criteria.where("customerId").is(customerId)),
                    Aggregation.group().sum("total").as("total"));

            assertEquals(aggregationExpected.toString(), aggregation.toString());
        }

        @Test
        void shouldQueryCorrectTable() {
            // Arrange
            var customerId = 1L;

            var aggregationResult = mock(AggregationResults.class);
            doReturn(aggregationResult).when(mongoTemplate).aggregate(any(Aggregation.class), eq("tb_orders"),
                    eq(Document.class));
            doReturn(new Document("total", 1)).when(aggregationResult).getUniqueMappedResult();

            // ACT
            orderService.findTotalOnOrdersByCustomerId(customerId);

            // ASSERT
            verify(mongoTemplate, times(1)).aggregate(any(Aggregation.class), eq("tb_orders"), eq(Document.class));
        }
    }
}
