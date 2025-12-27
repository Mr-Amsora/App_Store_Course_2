package com.codewithmosh.store.entities;

import com.codewithmosh.store.exceptions.OutOfStockException;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "carts")
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    private UUID id;

    @Column(name = "date_created",insertable = false,updatable = false)
    private LocalDate dateCreated;

    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL ,fetch =  FetchType.EAGER, orphanRemoval = true)
    private Set<CartItem> items = new LinkedHashSet<>();


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public BigDecimal getTotalPrice() {
        return items.stream()
                .map(CartItem::getTotalPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public CartItem getItemByProductId(long id) {
        return items.stream().filter(item -> item.getProduct().getId().equals(id)).findFirst().orElse(null);
    }

    public CartItem addItem(Product product) {
        var cartItem = getItemByProductId(product.getId());

        if (cartItem != null) {
            if (cartItem.getQuantity() + 1 > product.getQuantity()) {
                throw new OutOfStockException("Cannot add more than available stock");
            }
            cartItem.setQuantity(cartItem.getQuantity() + 1);
        } else {
            if (product.getQuantity() <= 0) {
                throw new OutOfStockException("Product is out of stock");
            }

            cartItem = new CartItem();
            cartItem.setProduct(product);
            cartItem.setQuantity(1);
            cartItem.setCart(this);
            items.add(cartItem);
        }
        return cartItem;
    }


    public  void removeItem(long productId) {
        var cartItem = getItemByProductId(productId);
        if (cartItem != null) {
            items.remove(cartItem);
            cartItem.setCart(null);
        }
    }

    public void clear() {
        items.clear();
    }
}