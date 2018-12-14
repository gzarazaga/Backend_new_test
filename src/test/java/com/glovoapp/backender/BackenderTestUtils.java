package com.glovoapp.backender;

public class BackenderTestUtils {

    public static Courier createCourier(String courierId) {
        Courier courier = new Courier();
        courier.withId("1");

        return courier;
    }
}
