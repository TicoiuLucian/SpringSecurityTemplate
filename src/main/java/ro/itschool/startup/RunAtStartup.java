package ro.itschool.startup;

import com.github.javafaker.Faker;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import ro.itschool.entity.*;
import ro.itschool.repository.ProductRepository;
import ro.itschool.repository.RoleRepository;
import ro.itschool.repository.ShoppingCartProductQuantityRepository;
import ro.itschool.repository.UserRepository;
import ro.itschool.service.ShoppingCartService;
import ro.itschool.service.UserService;
import ro.itschool.util.Constants;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

@Component
@RequiredArgsConstructor
public class RunAtStartup {

    private final UserService userService;

    private final RoleRepository roleRepository;

    private final ProductRepository productRepository;

    private final ShoppingCartService shoppingCartService;

    private final UserRepository userRepository;

    private final ShoppingCartProductQuantityRepository quantityRepository;


    @EventListener(ContextRefreshedEvent.class)
    public void contextRefreshedEvent() {

        roleRepository.save(new Role(Constants.ROLE_USER));
        roleRepository.save(new Role(Constants.ROLE_ADMIN));

        saveUser();
//        saveUserToDelete();
//        saveAdminUser();
        save50Products();
    }

    private void saveAdminUser() {
        MyUser myUser = new MyUser();
        myUser.setUsername("admin");
        myUser.setPassword("admin");
        myUser.setRandomToken("randomToken");
        final Set<Role> roles = new HashSet<>();
        roles.add(roleRepository.findByName(Constants.ROLE_ADMIN));
        myUser.setRoles(roles);
        myUser.setEnabled(true);
        myUser.setAccountNonExpired(true);
        myUser.setAccountNonLocked(true);
        myUser.setCredentialsNonExpired(true);
        myUser.setEmail("admin1@gmail.com");
        myUser.setFullName("Ion Admin");
        myUser.setPasswordConfirm("admin");
        myUser.setRandomTokenEmail("randomToken");

        userService.saveUser(myUser);
    }

    public void saveUser() {
        Faker faker = new Faker();
        MyUser myUser = new MyUser();
        myUser.setUsername("user");
        myUser.setPassword("user");
        myUser.setRandomToken("randomToken");
        final Set<Role> roles = new HashSet<>();
        roles.add(roleRepository.findByName(Constants.ROLE_USER));
        myUser.setRoles(roles);
        myUser.setEnabled(true);
        myUser.setAccountNonExpired(true);
        myUser.setAccountNonLocked(true);
        myUser.setCredentialsNonExpired(true);
        myUser.setEmail("user@gmail.com");
        myUser.setFullName("Ion User");
        myUser.setPasswordConfirm("user");
        myUser.setRandomTokenEmail("randomToken");

        MyUser myUser1 = userService.saveUser(myUser);

        List<Product> products = Stream
                .generate(() -> {
                            Product product = productRepository.save(
                                    new Product(
                                            faker.pokemon().name(),
                                            faker.number().numberBetween(100, 10000),
                                            faker.number().numberBetween(0, 199)));
                            quantityRepository.save(new ShoppingCartProductQuantity(myUser.getShoppingCart().getId(), product.getId(), 5));
                            return product;
                        }
                )
                .limit(3)
                .toList();

        ShoppingCart cart = myUser1.getShoppingCart();
        cart.setUser(myUser1);
        cart.setProducts(products);

//        ShoppingCart myCart = shoppingCartService.save(cart);

//        myUser1.setShoppingCart(cart);
        userService.updateUser(myUser1);

    }

    public void save50Products() {
        Faker faker = new Faker();
        Stream
                .generate(() -> productRepository.save(
                        new Product(
                                faker.pokemon().name(),
                                faker.number().numberBetween(100, 10000),
                                faker.number().numberBetween(0, 199)))
                )
                .limit(5)
                .toList();
    }

    public void saveUserToDelete() {
        MyUser myUser = new MyUser();
        myUser.setUsername("userToDelete");
        myUser.setPassword("user");
        myUser.setRandomToken("randomToken");
        final Set<Role> roles = new HashSet<>();
        roles.add(roleRepository.findByName(Constants.ROLE_USER));
        myUser.setRoles(roles);
        myUser.setEnabled(true);
        myUser.setAccountNonExpired(true);
        myUser.setAccountNonLocked(true);
        myUser.setCredentialsNonExpired(true);
        myUser.setEmail("userDeleteMe@gmail.com");
        myUser.setFullName("Ion DeleteMe User");
        myUser.setPasswordConfirm("user");
        myUser.setRandomTokenEmail("randomToken");

        userService.saveUser(myUser);

    }

}
