package com.glovoapp.backender;

public class BackenderTestUtils {

    public static Courier createCourier(String courierId, Boolean withBox) {
        Location location = new Location(      41.3965463,  2.1963997);
        Courier courier = new Courier();
        courier.withId("1");
        courier.withBox(withBox);
        courier.withLocation(location);
        return courier;
    }

    public static Order createOrderWithWord(String description) {
        Location location = new Location(41.3965463, 2.1963997);
        Order order = new Order();
        order.withDescription(description);
        order.withPickup(location);

        return order;
    }
}
