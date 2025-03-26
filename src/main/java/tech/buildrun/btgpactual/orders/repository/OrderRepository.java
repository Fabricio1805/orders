package tech.buildrun.btgpactual.orders.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.repository.MongoRepository;

import tech.buildrun.btgpactual.orders.entity.Order;

public interface OrderRepository extends MongoRepository <Order, Long>{
    Page<Order> findAllByCustomerId(final Long customerId, final PageRequest pageRequest);
}
