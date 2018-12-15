package com.glovoapp.backender;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

public class OrderServiceTest {
    private OrderService orderService;
    private CourierRepository courierRepository;
    private  OrderRepository orderRepository;

    @BeforeEach
    public void init() {
        this.courierRepository = Mockito.mock(CourierRepository.class);
        this.orderRepository = Mockito.mock(OrderRepository.class);

        this.orderService = new OrderService(courierRepository, orderRepository);
    }

    @Test
    public void findOrdersByCourierTest() {
        String courierId = "1";
        Courier courier = BackenderTestUtils.createCourier(courierId);
        when(courierRepository.findById(eq(courierId))).thenReturn(courier);

        List<Order> courierOrders = orderService.getOrdersBuCourier(courierId);

        assertNotNull(courierOrders);
        courierOrders.stream().forEach(System.out::println);
    }
}
