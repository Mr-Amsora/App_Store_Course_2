package com.codewithmosh.store.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "customer_id")
    private User customer;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private PaymentStatus status;

    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "created_at",insertable = false,updatable = false)
    private LocalDateTime createdAt;

    @ColumnDefault("0.00")
    @Column(name = "total_price", precision = 10, scale = 2)
    private BigDecimal totalPrice;

    @OneToMany(mappedBy = "order" , cascade = {CascadeType.PERSIST , CascadeType.REMOVE} )
    private Set<OrderItem> items = new LinkedHashSet<>();

    public Order saveOrder(Cart cart,User user){
        var order = new Order();
        order.setTotalPrice(cart.getTotalPrice());
        order.setStatus(PaymentStatus.PENDING);
        order.setCustomer(user);
        cart.getItems().forEach(item -> {
            var orderItem = new OrderItem();
            orderItem.setProduct(item.getProduct());
            orderItem.setQuantity(item.getQuantity());
            orderItem.setUnitPrice(item.getProduct().getPrice());
            orderItem.setTotalPrice(item.getTotalPrice());
            orderItem.setOrder(order);
            order.getItems().add(orderItem);
        });
        return order;
    }
}