package com.online.restaurant.dao;

import com.online.restaurant.model.OrderDetails;
import com.online.restaurant.model.Restaurant;
import org.springframework.data.repository.CrudRepository;

public interface OrderDetailsRepository extends CrudRepository<OrderDetails, Long> {

}
