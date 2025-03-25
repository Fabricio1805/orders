package tech.buildrun.btgpactual.orders.controller.dto;

public record PaginationResponse(Integer page,
        Integer pageSize,
        Integer totalElements,
        Integer totalPages    
        ) {
}
