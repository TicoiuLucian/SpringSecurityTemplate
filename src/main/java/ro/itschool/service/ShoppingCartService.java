package ro.itschool.service;

import ro.itschool.entity.Order;
import ro.itschool.entity.ShoppingCart;

import java.util.Optional;

public interface ShoppingCartService {


    Optional<ShoppingCart> findById(Integer id);

    Order convertShoppingCartToOrder(ShoppingCart shoppingCart);

    ShoppingCart update(ShoppingCart shoppingCart);

    ShoppingCart save(ShoppingCart sc);

    void deleteProductByIdFromShoppingCart(Integer productId);
}
