package tech.buildrun.btgpactual.orders.controller.dto;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import tech.buildrun.btgpactual.orders.factory.OrderEntityFactory;

public class OrderResponseDTOTest {

    @Nested
    class FromEntity {
        @Test
        void souldMapCorrectly() {
            // ARRANGE - prepara todos os mocks para a execucao
            var input = OrderEntityFactory.build();

           // ACT - executar o metodo a ser testado
            var output = OrderResponseDTO.fromEntity(input);


           // ASSERT - verifica se a execucao está correta
           assertEquals(input.getId(), output.orderId());
           assertEquals(input.getCustomerId(), output.customerId());
           assertEquals(input.getTotal(), output.total());
   
        }
    }
}
