package com.online.restaurant.model;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class OrderDetails {

    @Id
    @GeneratedValue
    private Long orderDetailsId;

    private Long orderId;

    private Long menuId;

    private Long restaurantId;

    private String name;

    private double price;

    private int quantity;

    private double totalPrice;

}
