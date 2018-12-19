package com.glovoapp.backender;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

public class OrderServiceTest {
    private OrderService orderService;
    private CourierRepository courierRepository;
    private OrderRepository orderRepository;

    @BeforeEach
    public void init() {
        this.courierRepository = Mockito.mock(CourierRepository.class);
        this.orderRepository = Mockito.mock(OrderRepository.class);

        this.orderService = new OrderService(courierRepository, orderRepository, new OrderValidator(), new OrderPrioritizer());
    }

    @Test
    public void findPizzaOrdersByCourierWithBoxTest() {
        orderService = new OrderService(courierRepository, new OrderRepository(), new OrderValidator(), new OrderPrioritizer());
        String courierId = "1";
        Courier courier = BackenderTestUtils.createCourier(courierId, true);

        when(courierRepository.findById(eq(courierId))).thenReturn(courier);

        List<Order> courierOrders = orderService.getOrdersByCourier(courierId);

        assertNotNull(courierOrders);

        Order firstOrder = courierOrders.get(0);

        Order expected = new Order().withId("order-1")
                .withDescription("I want a pizza cut into very small slices")
                .withFood(true)
                .withVip(false)
                .withPickup(new Location(41.3965463, 2.1963997))
                .withDelivery(new Location(41.407834, 2.1675979));

        assertEquals(expected, firstOrder);
    }

    @Test
    public void findPizzaOrdersMoreThanFiveKmsTest() {
        orderService = new OrderService(courierRepository, new OrderRepository(), new OrderValidator(), new OrderPrioritizer());
        String courierId = "1";
        Courier courier = BackenderTestUtils.createCourier(courierId, true);
        courier.withVehicle(Vehicle.BICYCLE);

        when(courierRepository.findById(eq(courierId))).thenReturn(courier);

        List<Order> courierOrders = orderService.getOrdersByCourier(courierId);

        assertNotNull(courierOrders);

        Order firstOrder = courierOrders.get(0);

        Order firstExpected = new Order().withId("order-1")
                .withDescription("I want a pizza cut into very small slices")
                .withFood(true)
                .withVip(false)
                .withPickup(new Location(41.3965463, 2.1963997))
                .withDelivery(new Location(41.407834, 2.1675979));

        Order secondOrder = courierOrders.get(1);

        assertEquals(firstExpected, firstOrder);

        Order expected = new Order().withId("order-2")
                .withDescription("Bring me a huuuudge hamburguer")
                .withFood(true)
                .withVip(false)
                .withPickup(new Location(41.3965463, 2.1963997))
                .withDelivery(new Location(41.407834, 2.1675979));

        assertEquals(expected, secondOrder);
        assertEquals(2, courierOrders.size());
    }

    @Test
    public void findPizzaOrdersMoreThanFiveKmsWithMotorcycleTest() {
        orderService = new OrderService(courierRepository, new OrderRepository(), new OrderValidator(), new OrderPrioritizer());
        String courierId = "1";
        Courier courier = BackenderTestUtils.createCourier(courierId, true);
        courier.withVehicle(Vehicle.MOTORCYCLE);

        when(courierRepository.findById(eq(courierId))).thenReturn(courier);

        List<Order> courierOrders = orderService.getOrdersByCourier(courierId);

        assertNotNull(courierOrders);

        Order thirthOrder = courierOrders.get(2);

        Order expected = new Order().withId("order-3")
                .withDescription("Could you please by me a sandwich?")
                .withFood(true)
                .withVip(false)
                .withPickup(new Location(41.451980, 2.2069379))
                .withDelivery(new Location(41.407834, 2.1675979));

        assertEquals(expected, thirthOrder);
        assertEquals(3, courierOrders.size());
    }
}
