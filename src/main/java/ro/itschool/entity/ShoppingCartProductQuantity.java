package ro.itschool.entity;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ShoppingCartProductQuantity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer shoppingCartId;
    private Integer productId;
    private Integer quantity;

    public ShoppingCartProductQuantity(Integer shoppingCartId, Integer productId, Integer quantity) {
        this.shoppingCartId = shoppingCartId;
        this.productId = productId;
        this.quantity = quantity;
    }
}
