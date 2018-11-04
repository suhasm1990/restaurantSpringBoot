package com.online.restaurant.dao;

import com.online.restaurant.model.Restaurant;

import org.springframework.data.repository.CrudRepository;

public interface RestaurantRepository extends CrudRepository<Restaurant, Long> {

}
