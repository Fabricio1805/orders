package tech.buildrun.btgpactual.orders.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.websocket.server.PathParam;
import tech.buildrun.btgpactual.orders.controller.dto.ApiResponse;
import tech.buildrun.btgpactual.orders.controller.dto.OrderResponseDTO;
import tech.buildrun.btgpactual.orders.service.OrderService;

@RequestMapping
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping("/customer/{customerId}/orders")
    public ResponseEntity<ApiResponse<OrderResponseDTO>> listOrdersByCustomer(
            @RequestParam(name = "page", defaultValue = "0") Integer page,
            @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
            @PathParam("customerId") Long customerId) {
        return ResponseEntity.ok().body(null);
    }
}
