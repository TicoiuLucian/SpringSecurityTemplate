package ro.itschool.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import ro.itschool.entity.MyUser;
import ro.itschool.entity.Product;
import ro.itschool.entity.ShoppingCart;
import ro.itschool.repository.OrderRepository;
import ro.itschool.repository.ProductRepository;
import ro.itschool.repository.ShoppingCartProductQuantityRepository;
import ro.itschool.service.ShoppingCartService;
import ro.itschool.service.UserService;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping(value = "/shopping-cart")
public class ShoppingCartController {

    @Autowired
    private ShoppingCartService shoppingCartService;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private ShoppingCartProductQuantityRepository quantityRepository;

    @RequestMapping(value = "/to-order")
    public String convertToOrder() {

        //stabilim care e username-ul user-ului autentificat
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = auth.getName();

        //aducem userul din db pe baza username-ului
        MyUser user = userService.findUserByUserName(currentPrincipalName);

        List<Product> productsByShoppingCartId = quantityRepository.getProductsByShoppingCartId(user.getId());
        Optional<ShoppingCart> byId = shoppingCartService.findById(user.getId().intValue());
        byId.get().setProducts(productsByShoppingCartId);

        orderRepository.save(shoppingCartService.convertShoppingCartToOrder(byId.get()));
        user.getShoppingCart().getProducts().clear();
        userService.updateUser(user);

        return "order-successful";
    }

    @RequestMapping
    public String getShoppingCartForPrincipal(Model model) {
        //stabilim care e username-ul user-ului autentificat
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = auth.getName();

        //aducem userul din db pe baza username-ului
        MyUser userByUserName = userService.findUserByUserName(currentPrincipalName);

        List<Product> productsByShoppingCartId = quantityRepository.getProductsByShoppingCartId(userByUserName.getId());


        model.addAttribute("products", productsByShoppingCartId);

        return "shopping-cart";
    }

//    @RequestMapping(value = "/product/remove/{productId}")
//    public String removeProductFromShoppingCart(@PathVariable Integer productId) {
//        //stabilim care e username-ul user-ului autentificat
//        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//        String currentPrincipalName = auth.getName();
//
//        //aducem userul din db pe baza username-ului
//        MyUser userByUserName = userService.findUserByUserName(currentPrincipalName);
//
//        Optional<Product> optionalProduct = productRepository.findById(productId);
//
//        userByUserName.getShoppingCart().getProducts().removeIf(product -> product.getId().equals(productId));
//        userService.updateUser(userByUserName);
//
//        return "redirect:/shopping-cart";
//    }

}
