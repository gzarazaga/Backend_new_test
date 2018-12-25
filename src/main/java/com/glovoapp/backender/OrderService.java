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
    private OrderPrioritizer orderPrioritizer;

    @Autowired
    public OrderService (CourierRepository courierRepository, OrderRepository orderRespository, OrderValidator orderValidator, OrderPrioritizer orderPrioritizer){
        this.courierRepository = courierRepository;
        this.orderRepository = orderRespository;
        this.orderValidator = orderValidator;
        this.orderPrioritizer = orderPrioritizer;
    }
    public List<Order> getOrdersByCourier(String courierId) {
        List<Order> courierOrders = new ArrayList<>();

        Courier courier = courierRepository.findById(courierId);
        courierOrders = orderRepository.findAll();

        List<Order> validatedOrder = getCourierOrders(courierOrders, courier);

        return validatedOrder;
    }

    private List<Order> getCourierOrders(List<Order> orders, Courier courier) {
        List<Order> validatedOrders = new ArrayList<>();
        List<Order> prioritizedOrders = new ArrayList<>();

        validatedOrders = orders.stream().filter(o -> orderValidator.validateBox(o, courier.getBox())).collect(Collectors.toList());
        validatedOrders = validatedOrders.stream().filter(o -> orderValidator.validateDistance(o, courier.getLocation(), courier.getVehicle())).collect(Collectors.toList());

        prioritizedOrders = orderPrioritizer.prioritize(validatedOrders, courier.getLocation());

        return prioritizedOrders;
    }



}
