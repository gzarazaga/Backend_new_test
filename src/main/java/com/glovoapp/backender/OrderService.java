package com.glovoapp.backender;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderService {
    private CourierRepository courierRepository;
    private OrderRepository orderRepository;
    private OrderValidator orderValidator;

    @Autowired
    public OrderService (CourierRepository courierRepository, OrderRepository orderRespository, OrderValidator orderValidator){
        this.courierRepository = courierRepository;
        this.orderRepository = orderRespository;
        this.orderValidator = orderValidator;
    }
    public List<Order> getOrdersByCourier(String courierId) {
        List<Order> courierOrders = new ArrayList<>();

        Courier courier = courierRepository.findById(courierId);
        courierOrders = orderRepository.findAll();

        List<Order> validatedOrder = getValidatedOrders(courierOrders, courier);

        return validatedOrder;
    }

    private List<Order> getValidatedOrders(List<Order> orders, Courier courier) {
        List<Order> validatedOrders = new ArrayList<>();

        validatedOrders = orders.stream().filter(o -> orderValidator.validateBox(o, courier.getBox())).collect(Collectors.toList());
        validatedOrders = validatedOrders.stream().filter(o -> orderValidator.validateDistance(o, courier.getLocation(), courier.getVehicle())).collect(Collectors.toList());

        return validatedOrders;
    }



}
