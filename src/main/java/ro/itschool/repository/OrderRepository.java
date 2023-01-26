package ro.itschool.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ro.itschool.entity.Order;

public interface OrderRepository extends JpaRepository<Order, Integer> {

}
