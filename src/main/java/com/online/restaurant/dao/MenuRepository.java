package com.online.restaurant.dao;

import com.online.restaurant.model.Menu;

import org.springframework.data.repository.CrudRepository;

public interface MenuRepository extends CrudRepository<Menu, Long> {

    Iterable<Menu> findByRestaurantId(Long id);

}
