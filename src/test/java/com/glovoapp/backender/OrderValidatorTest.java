package com.glovoapp.backender;

import org.junit.jupiter.api.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class OrderValidatorTest {
    OrderValidator orderValidator = new OrderValidator();

    @Test
    public void testValidateBoxForOrderWithPizza() {
        Order order = BackenderTestUtils.createOrderWithWord("La buena pizza");
        boolean result = orderValidator.validateBox(order, true);

        assertTrue(result);
    }

    @Test
    public void testValidateBoxForOrderWithCake() {
        Order order = BackenderTestUtils.createOrderWithWord("The good cake");
        boolean result = orderValidator.validateBox(order, true);

        assertTrue(result);
    }

    @Test
    public void testValidateBoxForOrderWithFlamingo() {
        Order order = BackenderTestUtils.createOrderWithWord("Tapas flamingo");
        boolean result = orderValidator.validateBox(order, true);

        assertTrue(result);
    }

    @Test
    public void testValidateBoxForOrderWithPizzaWithoutBox() {
        Order order = BackenderTestUtils.createOrderWithWord("La buena pizza");
        boolean result = orderValidator.validateBox(order, false);

        assertFalse(result);
    }

    @Test
    public void testValidateBoxForOrderWithCakeWithoutBox() {
        Order order = BackenderTestUtils.createOrderWithWord("The good cake");
        boolean result = orderValidator.validateBox(order, false);

        assertFalse(result);
    }

    @Test
    public void testValidateBoxForOrderWithFlamingoWithoutBox() {
        Order order = BackenderTestUtils.createOrderWithWord("Tapas flamingo");
        boolean result = orderValidator.validateBox(order, false);

        assertFalse(result);
    }

    @Test
    public void testValidateBoxForOrderDoesntRequireBoxWithBox() {
        Order order = BackenderTestUtils.createOrderWithWord("The best hamburguer");
        boolean result = orderValidator.validateBox(order, true);

        assertTrue(result);
    }

    @Test
    public void testValidateBoxForOrderDoesntRequireBoxWithoutBox() {
        Order order = BackenderTestUtils.createOrderWithWord("The best hamburguer");
        boolean result = orderValidator.validateBox(order, false);

        assertTrue(result);
    }

    @Test
    public void testValidateDistanceLessThanFiveKmWithBike() {
        Order order = BackenderTestUtils.createOrderWithWord("The best hamburguer");
        Location courierLocation = new Location(     41.3965463,  2.1963997);
        boolean result = orderValidator.validateDistance(order, courierLocation, Vehicle.BICYCLE);

        assertTrue(result);
    }

    @Test
    public void testValidateDistanceLessThanFiveKmWithMotorCyle() {
        Order order = BackenderTestUtils.createOrderWithWord("The best hamburguer");
        Location courierLocation = new Location(     41.3965463,  2.1963997);
        boolean result = orderValidator.validateDistance(order, courierLocation, Vehicle.MOTORCYCLE);

        assertTrue(result);
    }

    @Test
    public void testValidateDistanceMoreThanFiveKmWithBicycle() {
        Order order = BackenderTestUtils.createOrderWithWord("The best hamburguer");
        Location courierLocation = new Location(     41.451980,  2.206937);
        boolean result = orderValidator.validateDistance(order, courierLocation, Vehicle.BICYCLE);

        assertFalse(result);
    }

    @Test
    public void testValidateDistanceMoreThanFiveKmWithMotorCyle() {
        Order order = BackenderTestUtils.createOrderWithWord("The best hamburguer");
        Location courierLocation = new Location(     41.451980,  2.206937);
        boolean result = orderValidator.validateDistance(order, courierLocation, Vehicle.MOTORCYCLE);

        assertTrue(result);
    }
}
