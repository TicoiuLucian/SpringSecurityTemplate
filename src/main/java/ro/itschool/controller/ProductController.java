package ro.itschool.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ro.itschool.entity.MyUser;
import ro.itschool.entity.Product;
import ro.itschool.entity.ShoppingCartProductQuantity;
import ro.itschool.repository.ProductRepository;
import ro.itschool.repository.ShoppingCartProductQuantityRepository;
import ro.itschool.service.ShoppingCartService;
import ro.itschool.service.UserService;
import ro.itschool.util.Constants;

import java.util.Optional;

@Controller
@RequiredArgsConstructor
@RequestMapping(value = "/product")
public class ProductController {

    private final ProductRepository productRepository;

    private final ShoppingCartService shoppingCartService;

    private final UserService userService;

    private final ShoppingCartProductQuantityRepository quantityRepository;

    @RequestMapping(value = {"/all"})
    public String index(Model model) {
        model.addAttribute("products", productRepository.findByQuantityGreaterThan(0L));
        return "products";
    }

    @RequestMapping(value = "/delete/{id}")
    public String deleteProduct(@PathVariable Integer id) {
        shoppingCartService.deleteProductByIdFromShoppingCart(id);
        productRepository.deleteById(id);
        return Constants.REDIRECT_TO_PRODUCTS;
    }

    @RequestMapping(value = "/add/{id}")
    public String addProductToShoppingCart(@PathVariable Integer id, @ModelAttribute("product") @RequestBody Product frontendProduct) {
        Optional<Product> desiredProductOptional = productRepository.findById(id);
        if (frontendProduct == null) {
            throw new RuntimeException("Quantity can't be null");
        }
        Integer quantityToBeOrdered = frontendProduct.getQuantity();

        MyUser loggedUser = getLoggedUser();

        desiredProductOptional.ifPresent(desiredProduct -> {
            Product productToBeAddedToShoppingCart = new Product();
            productToBeAddedToShoppingCart.setId(desiredProduct.getId());
            productToBeAddedToShoppingCart.setPrice(desiredProduct.getPrice());
            productToBeAddedToShoppingCart.setName(desiredProduct.getName());
            productToBeAddedToShoppingCart.setQuantity(quantityToBeOrdered);
            loggedUser.getShoppingCart().addProductToShoppingCart(productToBeAddedToShoppingCart);

            desiredProduct.setQuantity(desiredProduct.getQuantity() - quantityToBeOrdered);

            Optional<ShoppingCartProductQuantity> cartProductQuantityOptional = quantityRepository.findByShoppingCartIdAndProductId(loggedUser.getId().intValue(), desiredProduct.getId());
            if (cartProductQuantityOptional.isEmpty()) {
                quantityRepository.save(new ShoppingCartProductQuantity(loggedUser.getId().intValue(), desiredProduct.getId(), quantityToBeOrdered));
            } else {
                cartProductQuantityOptional.ifPresent(cartProductQuantity -> {
                    cartProductQuantity.setQuantity(cartProductQuantity.getQuantity() + quantityToBeOrdered);
                    quantityRepository.save(cartProductQuantity);
                });
            }
            productRepository.save(desiredProduct);
            userService.updateUser(loggedUser);
        });

        return Constants.REDIRECT_TO_PRODUCTS;
    }

    @RequestMapping(value = "/remove/{productId}")
    public String removeProductFromShoppingCart(@PathVariable Integer productId) {
        MyUser loggedUser = getLoggedUser();

        quantityRepository.getProductsByShoppingCartId(loggedUser.getId()).stream()
                .filter(product -> product.getId().equals(productId))
                .forEach(product -> {
                    Optional<Product> productOptional = productRepository.findById(product.getId());
                    productOptional.ifPresent(pr -> {
                        pr.setQuantity(pr.getQuantity() + product.getQuantity());
                        productRepository.save(pr);
                    });
                });
        quantityRepository.deleteByShoppingCartIdAndProductId(loggedUser.getId().intValue(), productId);

        return Constants.REDIRECT_TO_SHOPPING_CART;
    }

    @GetMapping(value = "/add-new")
    public String addProduct(Model model) {
        model.addAttribute("product", new Product());
        return Constants.ADD_PRODUCT;
    }

    @PostMapping(value = "/add-new")
    public String addProduct(@ModelAttribute("product") @RequestBody Product product) {
        productRepository.save(product);
        return Constants.REDIRECT_TO_PRODUCTS;
    }

    private MyUser getLoggedUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return userService.findUserByUserName(auth.getName());
    }
}
