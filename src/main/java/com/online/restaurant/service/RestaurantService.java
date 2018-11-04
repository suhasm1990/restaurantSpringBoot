package com.online.restaurant.service;

import com.online.restaurant.model.*;

import java.util.Optional;

public interface RestaurantService {

    Iterable<Restaurant> getAllRestaurants();

    Iterable<Menu> getMenuFromRestaurant(Long id);

    void saveReservation(Reservation reservation);

    Long saveOrder(Orders order);

    void saveOrderDetails(OrderDetails orderDetails);

    void sendEmail(String toValue, String subject, String body);

    Optional<Restaurant> getRestaurantById(Long Id);

    Optional<Orders> getOrderById(Long Id);

}
