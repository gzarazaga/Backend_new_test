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

        Order secondOrder = courierOrders.get(1);

        Order expected = new Order().withId("order-1")
                .withDescription("I want a pizza cut into very small slices")
                .withFood(true)
                .withVip(false)
                .withPickup(new Location(41.3965463, 2.1963997))
                .withDelivery(new Location(41.407834, 2.1675979));

        assertEquals(expected, secondOrder);


    }

    @Test
    public void shoudNotFindPizzaOrdersByCourierWithBoxTest() {
        orderService = new OrderService(courierRepository, new OrderRepository(), new OrderValidator(), new OrderPrioritizer());
        String courierId = "1";
        Courier courier = BackenderTestUtils.createCourier(courierId, false);

        when(courierRepository.findById(eq(courierId))).thenReturn(courier);

        List<Order> courierOrders = orderService.getOrdersByCourier(courierId);

        assertNotNull(courierOrders);

        Order secondOrder = courierOrders.get(1);

        assertEquals(3, courierOrders.size());

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

        Order firstExpected = new Order().withId("order-1")
                .withDescription("I want a pizza cut into very small slices")
                .withFood(true)
                .withVip(false)
                .withPickup(new Location(41.3965463, 2.1963997))
                .withDelivery(new Location(41.407834, 2.1675979));

        Order secondOrder = courierOrders.get(1);

        assertEquals(firstExpected, secondOrder);
    }

    @Test
    public void findCakeOrdersMoreThanFiveKmsWithMotorcycleTest() {
        orderService = new OrderService(courierRepository, new OrderRepository(), new OrderValidator(), new OrderPrioritizer());
        String courierId = "1";
        Courier courier = BackenderTestUtils.createCourier(courierId, true);
        courier.withVehicle(Vehicle.MOTORCYCLE);

        when(courierRepository.findById(eq(courierId))).thenReturn(courier);

        List<Order> courierOrders = orderService.getOrdersByCourier(courierId);

        assertNotNull(courierOrders);

        Order fifthOrder = courierOrders.get(5);

        Order expected = new Order().withId("order-3")
                .withDescription("Could you please by me a cake of sandwich?")
                .withFood(true)
                .withVip(false)
                .withPickup(new Location(41.451980, 2.2069379))
                .withDelivery(new Location(41.407834, 2.1675979));

        assertEquals(expected, fifthOrder);

        for (Order order: courierOrders) {
            System.out.println(order);
        }
    }

    /*
    We want to prioritise some orders in order to improve our Customer Experience.

- We'll show the orders that are close to the courier first, in slots of 500
meters (e.g. orders closer than 500m have the same priority; same orders
between 500 and 1000m)

- Inside each slot, we'll show the orders that belong to a VIP customer first, sorted by distance

- Then, in each slot, we'll show the orders that are food, sorted by distance

- ...and the rest of orders in the slot, sorted by distance
     */
    @Test
    public void priotizedByCloserOrdersTest() {
        orderService = new OrderService(courierRepository, new OrderRepository(), new OrderValidator(), new OrderPrioritizer());
        String courierId = "1";
        Courier courier = BackenderTestUtils.createCourier(courierId, true);
        courier.withVehicle(Vehicle.MOTORCYCLE);

        when(courierRepository.findById(eq(courierId))).thenReturn(courier);

        List<Order> courierOrders = orderService.getOrdersByCourier(courierId);

        assertNotNull(courierOrders);

        Order firstOrder = courierOrders.get(0);

        Order expected = new Order().withId("order-4")
                .withDescription("I need 1k of icecream")
                .withFood(true)
                .withVip(true)
                .withPickup(new Location(41.3965363, 2.1963697))
                .withDelivery(new Location(41.407834, 2.1964997));

        assertEquals(expected, firstOrder);

        for (Order order: courierOrders) {
            System.out.println(order);
        }

    }

    @Test
    public void priotizedByVipOrdersInEachSlotTest() {
        orderService = new OrderService(courierRepository, new OrderRepository(), new OrderValidator(), new OrderPrioritizer());
        String courierId = "1";
        Courier courier = BackenderTestUtils.createCourier(courierId, true);
        courier.withVehicle(Vehicle.MOTORCYCLE);

        when(courierRepository.findById(eq(courierId))).thenReturn(courier);

        List<Order> courierOrders = orderService.getOrdersByCourier(courierId);

        assertNotNull(courierOrders);

        Order thirthOrder = courierOrders.get(4);

        Order expected = new Order().withId("order-5")
                .withDescription("Like 3 but it's vip")
                .withFood(true)
                .withVip(true)
                .withPickup(new Location(41.451982, 2.2069380))
                .withDelivery(new Location(41.407834, 2.1675979));

        assertEquals(expected, thirthOrder);



    }


    @Test
    public void priotizedByFoodOrdersInEachSlotTest() {
        orderService = new OrderService(courierRepository, new OrderRepository(), new OrderValidator(), new OrderPrioritizer());
        String courierId = "1";
        Courier courier = BackenderTestUtils.createCourier(courierId, true);
        courier.withVehicle(Vehicle.MOTORCYCLE);

        when(courierRepository.findById(eq(courierId))).thenReturn(courier);

        List<Order> courierOrders = orderService.getOrdersByCourier(courierId);

        assertNotNull(courierOrders);

        Order fourthOrder = courierOrders.get(3);

        Order expected = new Order().withId("order-6")
                .withDescription("Like 1 but itsn't food")
                .withFood(false)
                .withVip(false)
                .withPickup(new Location(41.3965463, 2.1963997))
                .withDelivery(new Location(41.407834, 2.1675979));

        assertEquals(expected, fourthOrder);

        for (Order order: courierOrders) {
            System.out.println(order);
        }

    }
}
