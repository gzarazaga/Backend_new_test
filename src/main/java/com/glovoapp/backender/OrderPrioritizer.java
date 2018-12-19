package com.glovoapp.backender;

import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class OrderPrioritizer {

/*
We want to prioritise some orders in order to improve our Customer Experience.

- We'll show the orders that are close to the courier first, in slots of 500
meters (e.g. orders closer than 500m have the same priority; same orders
between 500 and 1000m)

- Inside each slot, we'll show the orders that belong to a VIP customer first, sorted by distance

- Then, in each slot, we'll show the orders that are food, sorted by distance

- ...and the rest of orders in the slot, sorted by distance
 */
    public List<Order> prioritize(List<Order> noPrioritizedOrders, Location courierLocation) {
        List<Order> prioritizedOrders = new ArrayList<>();
        Map<Integer,List<Order>> prioritySlots = getPrioritySlots(noPrioritizedOrders, courierLocation);

        for (int i=0; i > prioritySlots.size(); i++) {
            prioritizedOrders.addAll(prioritySlots.get(i));
        }

        return noPrioritizedOrders;
    }

    private Map<Integer,List<Order>> getPrioritySlots(List<Order> noPrioritizedOrders,Location courierLocation) {
        Map<Integer,List<Order>> prioritySlots = new HashMap<>();
        Map<Double,Order> orderDistances = new HashMap<>();

        for (Order order: noPrioritizedOrders) {
            double distanceFromCourier = DistanceCalculator.calculateDistance(order.getPickup(), courierLocation);
            orderDistances.put(distanceFromCourier, order);
        }

        Map<Double,Order> sortedByDistanceOrders = new LinkedHashMap<>();
        orderDistances.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .forEachOrdered(x -> sortedByDistanceOrders.put(x.getKey(), x.getValue()));

        sortedByDistanceOrders.entrySet().stream().forEach(e -> assignSlot(e, prioritySlots));

        return prioritySlots;
    }

    private void assignSlot(Map.Entry<Double, Order> e, Map<Integer, List<Order>> prioritySlots) {
        List<Order> firstOrders = new ArrayList<>();
        List<Order> secondOrders = new ArrayList<>();
        List<Order> lastOrders = new ArrayList<>();

        if (e.getKey() < 0.5) {
            firstOrders.add(e.getValue());
        } else if (e.getKey() < 1 && e.getKey() > 0.5) {
            secondOrders.add(e.getValue());
        } else {
            lastOrders.add(e.getValue());
        }

        List<Order> finalFirstOrders = sortOrders(firstOrders);
        List<Order> finalSecondOrders = sortOrders(secondOrders);
        List<Order> finalLastOrders = sortOrders(lastOrders);

        prioritySlots.put(1, finalFirstOrders);
        prioritySlots.put(2, finalSecondOrders);
        prioritySlots.put(3, finalLastOrders);
    }

    private List<Order> sortOrders(List<Order> orders) {
        List<Order> sortedOrders = new ArrayList<>();

        List<Order> vipOrders = getVipOrders(orders);
        List<Order> foodOrers = getFoodOrders(orders);

        sortedOrders.addAll(vipOrders);
        sortedOrders.addAll(foodOrers);

        orders.removeAll(sortedOrders);
        sortedOrders.addAll(orders);

        return  sortedOrders;
    }

    private List<Order> getVipOrders(List<Order> orders) {
        return  orders.stream().filter(order -> order.getVip()).collect(Collectors.toList());
    }

    private List<Order> getFoodOrders(List<Order> orders) {
        return  orders.stream().filter(order -> order.getFood()).collect(Collectors.toList());
    }
}

