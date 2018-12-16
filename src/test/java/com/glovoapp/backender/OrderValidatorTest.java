package com.glovoapp.backender;

import org.junit.jupiter.api.Test;

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
        orderValidator.validateBox(order, true);
    }

    @Test
    public void testValidateBoxForOrderWithFlamingo() {
        Order order = BackenderTestUtils.createOrderWithWord("Tapas flamingo");
        orderValidator.validateBox(order, true);
    }
}
