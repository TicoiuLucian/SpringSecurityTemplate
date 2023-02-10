package ro.itschool.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ro.itschool.entity.Product;
import ro.itschool.entity.ShoppingCartProductQuantity;

import java.util.List;

public interface ShoppingCartProductQuantityRepository extends JpaRepository<ShoppingCartProductQuantity, Long> {

    @Query(value = "SELECT new ro.itschool.entity.Product(p.id, p.name, p.price, s.quantity) from Product p inner join ShoppingCartProductQuantity s " +
            "on p.id = s.productId where shoppingCartId = :id")
    List<Product> getProductsByShoppingCartId(Long id);

}
