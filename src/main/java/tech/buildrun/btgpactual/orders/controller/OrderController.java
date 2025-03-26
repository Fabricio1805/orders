package tech.buildrun.btgpactual.orders.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import tech.buildrun.btgpactual.orders.controller.dto.ApiResponse;
import tech.buildrun.btgpactual.orders.controller.dto.OrderResponseDTO;
import tech.buildrun.btgpactual.orders.controller.dto.PaginationResponse;
import tech.buildrun.btgpactual.orders.service.OrderService;

@RestController
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping("/customer/{customerId}/orders")
    public ResponseEntity<ApiResponse<OrderResponseDTO>> listOrdersByCustomer(
            @PathVariable() Long customerId,
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "10") Integer pageSize) {
                System.out.println(customerId);
        Page<OrderResponseDTO> pageResponse = orderService.findAllOrdersByCustomerId(customerId, PageRequest.of(page, pageSize));

        return ResponseEntity.ok().body(new ApiResponse<>(pageResponse.getContent(), PaginationResponse.fromPage(pageResponse)));
    }
}
