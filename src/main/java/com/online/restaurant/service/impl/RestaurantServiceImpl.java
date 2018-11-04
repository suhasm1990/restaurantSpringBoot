package com.online.restaurant.service.impl;

import com.online.restaurant.model.*;
import com.online.restaurant.dao.*;
import com.online.restaurant.service.RestaurantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.internet.MimeMessage;
import java.util.Optional;

@Service
public class RestaurantServiceImpl implements RestaurantService {

    @Autowired
    private JavaMailSender sender;

    @Autowired
    RestaurantRepository restaurantRepo;

    @Autowired
    MenuRepository menuRepository;

    @Autowired
    ReservationRepository reservationRepository;

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    OrderDetailsRepository orderDetailsRepository;

    @Override
    public Iterable<Restaurant> getAllRestaurants(){

        return restaurantRepo.findAll();

    }

    @Override
    public Iterable<Menu> getMenuFromRestaurant(Long id){

        return menuRepository.findByRestaurantId(id);

    }

    @Override
    public void saveReservation(Reservation reservation){
        reservationRepository.save(reservation);
    }

    @Override
    public Long saveOrder(Orders order){
        return orderRepository.save(order).getOrderId();
    }

    @Override
    public void saveOrderDetails(OrderDetails orderDetails){
        orderDetailsRepository.save(orderDetails);
    }

    @Override
    public Optional<Restaurant> getRestaurantById(Long Id){
        return restaurantRepo.findById(Id);
    }

    @Override
    public Optional<Orders> getOrderById(Long Id){
        return orderRepository.findById(Id);
    }

    @Override
    public void sendEmail(String toValue, String subject, String body){
        MimeMessage message = sender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);
        try {
            helper.setTo(toValue);
            helper.setText(body, true);
            helper.setSubject(subject);

            sender.send(message);
        }
        catch (Exception ex){
            System.out.println("Error in sending email: "+ex);
        }
    }

}
