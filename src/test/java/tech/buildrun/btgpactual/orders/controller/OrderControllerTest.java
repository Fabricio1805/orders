package tech.buildrun.btgpactual.orders.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doReturn;

import java.math.BigDecimal;

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
import org.springframework.http.HttpStatusCode;

import tech.buildrun.btgpactual.orders.factory.OrderResponseFactory;
import tech.buildrun.btgpactual.orders.service.OrderService;

@ExtendWith(MockitoExtension.class)
public class OrderControllerTest {
    
    @Mock
    OrderService orderService;


    @InjectMocks
    OrderController orderController;

    @Captor
    ArgumentCaptor<Long> customerIdCaptor;

    @Captor
    ArgumentCaptor<PageRequest> pageRequestCaptor;


    @Nested
    class ListOrders {

        @Test
        void shouldReturnHttpOk() {
            // ARRANGE - prepara todos os mocks para a execucao
            var customerId = 1L;
            var page = 0;
            var pageSize = 10;

            doReturn(OrderResponseFactory.buildWithOneItem()).when(orderService).findAllOrdersByCustomerId(anyLong(), any());
            doReturn(BigDecimal.valueOf(24.50)).when(orderService).findTotalOnOrdersByCustomerId(anyLong());

            // ACT - executar o metodo a ser testado
           var response = orderController.listOrdersByCustomer(customerId, page, pageSize);


            // ASSERT - verifica se a execucao está correta
            assertEquals(HttpStatusCode.valueOf(200), response.getStatusCode());
        }
        

        @Test
        @DisplayName("Deve conseguir retornar dados passando os parametros corretos")
        void shouldPassCorrectParametersToService() {
            // ARRANGE - prepara todos os mocks para a execucao
            var customerId = 1L;
            var page = 0;
            var pageSize = 10;

            doReturn(OrderResponseFactory.buildWithOneItem()).when(orderService).findAllOrdersByCustomerId(customerIdCaptor.capture(), pageRequestCaptor.capture());
            doReturn(BigDecimal.valueOf(24.50)).when(orderService).findTotalOnOrdersByCustomerId(customerIdCaptor.capture());

            // ACT - executar o metodo a ser testado
            orderController.listOrdersByCustomer(customerId, page, pageSize);


            // ASSERT - verifica se a execucao está correta
            assertEquals(2, customerIdCaptor.getAllValues().size());
            assertEquals(customerId, customerIdCaptor.getAllValues().get(0));
            assertEquals(customerId, customerIdCaptor.getAllValues().get(1));

            assertEquals(page, pageRequestCaptor.getValue().getPageNumber());
            assertEquals(pageSize, pageRequestCaptor.getValue().getPageSize());
        }

        @Test
        void shouldReturnResposeBodyCorrectly() {
           // ARRANGE - prepara todos os mocks para a execucao
           var customerId = 1L;
           var page = 0;
           var pageSize = 10;
           var totalOnOrders = BigDecimal.valueOf(24.50);
           var pagination = OrderResponseFactory.buildWithOneItem();
           
           doReturn(pagination).when(orderService).findAllOrdersByCustomerId(anyLong(), any());
           doReturn(totalOnOrders).when(orderService).findTotalOnOrdersByCustomerId(anyLong());


           // ACT - executar o metodo a ser testado
           var response = orderController.listOrdersByCustomer(customerId, page, pageSize);


           // ASSERT - verifica se a execucao está correta
           assertNotNull(response);
           assertNotNull(response.getBody());
           assertNotNull(response.getBody().data());
           assertNotNull(response.getBody().pagination());
           assertNotNull(response.getBody().summary());
           
           assertEquals(totalOnOrders, response.getBody().summary().get("totalOnOrders"));
           assertEquals(pagination.getTotalPages(), response.getBody().pagination().totalPages());
           assertEquals(pagination.getTotalElements(), response.getBody().pagination().totalElements());
           assertEquals(pagination.getSize(), response.getBody().pagination().pageSize());
           assertEquals(pagination.getNumber(), response.getBody().pagination().page());

           assertEquals(pagination.getContent(), response.getBody().data());
        }
    }
}
