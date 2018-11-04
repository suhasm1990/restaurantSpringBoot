package com.online.restaurant.controller;

import com.online.restaurant.model.Orders;
import com.online.restaurant.model.OrderDetails;
import com.online.restaurant.model.Reservation;
import com.online.restaurant.model.Restaurant;
import com.online.restaurant.service.RestaurantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.mail.internet.MimeMessage;
import javax.persistence.criteria.Order;
import java.util.List;
import java.util.Optional;

@Controller
public class RestaurantController {

    @Autowired
    RestaurantService restaurantService;

    @RequestMapping("/login")
    public String loginPage(){
        return "login";
    }

    @RequestMapping("/restaurants/{option}")
    public String getRestaurants(Model model, @PathVariable("option") String option){
        model.addAttribute("option", option);
        model.addAttribute("restaurants", restaurantService.getAllRestaurants());
        return "restaurants";
    }

    @RequestMapping("/restaurant/{id}")
    public String getRestaurants(Model model, @PathVariable("id") Long id){
        model.addAttribute("menuList", restaurantService.getMenuFromRestaurant(id));
        model.addAttribute("restaurantId", id);
        return "menu";
    }

    @RequestMapping("/makeReservation/{id}")
    public String makeReservation(Model model, @PathVariable("id") Long id){
        Reservation reservation = new Reservation();
        reservation.setRestaurantId(id);
        model.addAttribute("reservation", reservation);
        return "reservation";
    }

    @RequestMapping(value = "/submitReservation", method = RequestMethod.POST)
    public String submitReservation(@ModelAttribute(value = "reservation") Reservation reservation, BindingResult errors){
        restaurantService.saveReservation(reservation);
        Restaurant restaurant = restaurantService.getRestaurantById(reservation.getRestaurantId()).orElse(null);
        String subject = "Reservation Details from Waze Eats";
        String body = String.format("<html>\n" +
                "<body>\n" +
                "<p>Hello,</p>\n" +
                "<p>Your Reservation is confirmed at <b>%s</b> on <b>%s %s %s</b> at <b>%02d:%02d</b> for <b>%d</b> people.</p>\n" +
                "<p>See you soon and have a nice day</p>\n" +
                "</body>\n" +
                "</html>", restaurant.getName(), reservation.getReservationDateTime().getDayOfMonth(),
                reservation.getReservationDateTime().getMonth(),reservation.getReservationDateTime().getYear(),
                reservation.getReservationDateTime().getHour(),reservation.getReservationDateTime().getMinute(),
                reservation.getNoOfPersons());
        restaurantService.sendEmail(reservation.getEmail(), subject, body);
        return "index";
    }

    @RequestMapping(value = "/getOrderId", method = RequestMethod.POST)
    public ResponseEntity getOrderId(@RequestParam("email") String email){
        Orders orderValue = new Orders();
        orderValue.setEmail(email);
        Long orderId = restaurantService.saveOrder(orderValue);
        return new ResponseEntity(orderId, HttpStatus.OK);
    }

    @RequestMapping(value = "/placeOrder", method = RequestMethod.POST)
    public ResponseEntity placeOrder(@RequestBody List<OrderDetails> orderList){
        String tableContents = "";
        Long orderId = Long.parseLong("0");
        double totalPrice = 0;
        for(OrderDetails individualOrder : orderList){
            restaurantService.saveOrderDetails(individualOrder);
            tableContents+="<tr><td>"+individualOrder.getName()+"</td>" +
                    "<td>"+individualOrder.getQuantity()+"</td>" +
                    "<td>"+individualOrder.getPrice()+"<td></tr>";
            totalPrice = individualOrder.getTotalPrice();
            orderId = individualOrder.getOrderId();
        }
        tableContents+="<tr><td></td><td><b>Total</b></td><td><b>"+totalPrice+"</b></td></tr>";
        Orders orderValue = restaurantService.getOrderById(orderId).orElse(null);
        String subject = "Waze Eats Order Details";
        String body = String.format("<html lang=\"en\">\n" +
                "<head>\n" +
                "  <title>Waze Eats Order Details</title>\n" +
                "</head>\n" +
                "<body>\n" +
                "\n" +
                "<div class=\"container\">\n" +
                "  <h2>Waze Eats</h2>\n" +
                "  <p>Please find below order details and the order will be delivered to you shortly</p>            \n" +
                "  <table>\n" +
                "    <thead>\n" +
                "      <tr>\n" +
                "        <th>Name</th>\n" +
                "        <th>Quantity</th>\n" +
                "        <th>Price</th>\n" +
                "      </tr>\n" +
                "    </thead>\n" +
                "    <tbody>\n" +
                "      %s\n" +
                "    </tbody>\n" +
                "  </table>\n" +
                "  <p>Thank you for choosing Waze Eats. Have a nice day!</p>  \n" +
                "</div>\n" +
                "\n" +
                "</body>\n" +
                "</html>\n", tableContents);
        restaurantService.sendEmail(orderValue.getEmail(), subject, body);
        return new ResponseEntity(HttpStatus.OK);
    }

}
