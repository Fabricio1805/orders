package tech.buildrun.btgpactual.orders.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import tech.buildrun.btgpactual.orders.entity.Order;

public interface OrderRepository extends MongoRepository <Order, Long>{
    
}
