package com.glovoapp.backender;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import com.glovoapp.backender.CourierRepository;

@Service
public class OrderService {
    private CourierRepository courierRepository;
    private OrderRepository orderRepository;

    @Autowired
    public OrderService (CourierRepository courierRepository, OrderRepository orderRespository){
        this.courierRepository = courierRepository;
        this.orderRepository = orderRespository;
    }
    public List<Order> getOrdersBuCourier(String courierId) {
        List<Order> courierOrders = new ArrayList<>();

        Courier courier = courierRepository.findById(courierId);
        courierOrders = orderRepository.findAll();

        /*
        - If the description of the order contains the words pizza, cake or flamingo,
        we can only show the order to the courier if they are equipped with a Glovo box.

                - If the order is further than 5km to the courier, we will only show it to
        couriers that move in motorcycle or electric scooter.
        */

        return courierOrders;
    }


}
