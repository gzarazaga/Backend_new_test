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

        for (int i=1; i <= prioritySlots.size(); i++) {
            List<Order> slot = prioritySlots.get(Integer.valueOf(i));
            prioritizedOrders.addAll(slot);
        }

        return prioritizedOrders;
    }

    private Map<Integer,List<Order>> getPrioritySlots(List<Order> noPrioritizedOrders,Location courierLocation) {
        Map<Integer,List<Order>> prioritySlots = new HashMap<>();
        Map<Double,List<Order>> orderDistances = new HashMap<>();

        for (Order order: noPrioritizedOrders) {
            double distanceFromCourier = DistanceCalculator.calculateDistance(order.getPickup(), courierLocation);
            List<Order> ordersForDistance = Optional.ofNullable(orderDistances.get(distanceFromCourier)).orElse(new ArrayList<>());
            ordersForDistance.add(order);
            orderDistances.put(distanceFromCourier, ordersForDistance);
        }

        Map<Double,List<Order>> sortedByDistanceOrders = new LinkedHashMap<>();
        orderDistances.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .forEachOrdered(x -> sortedByDistanceOrders.put(x.getKey(), x.getValue()));

        sortedByDistanceOrders.entrySet().stream().forEach(e -> assignSlot(e, prioritySlots));

        return prioritySlots;
    }

    private void assignSlot(Map.Entry<Double, List<Order>> e, Map<Integer, List<Order>> prioritySlots) {
        List<Order> firstOrders = Optional.ofNullable(prioritySlots.get(1)).orElse(new ArrayList<>());
        List<Order> secondOrders = Optional.ofNullable(prioritySlots.get(2)).orElse(new ArrayList<>());
        List<Order> lastOrders = Optional.ofNullable(prioritySlots.get(3)).orElse(new ArrayList<>());

        Set<Order> firstOrdersSet = new HashSet<>(firstOrders);
        Set<Order> secondOrdersSet = new HashSet<>(secondOrders);
        Set<Order> lastOrdersSet = new HashSet<>(lastOrders);

        if (e.getKey() < 0.5) {
            firstOrdersSet.addAll(e.getValue());
        } else if (e.getKey() < 1 && e.getKey() > 0.5) {
            secondOrdersSet.addAll(e.getValue());
        } else {
            lastOrdersSet.addAll(e.getValue());
        }

        List<Order> finalFirstOrders = sortOrders(firstOrdersSet);
        List<Order> finalSecondOrders = sortOrders(secondOrdersSet);
        List<Order> finalLastOrders = sortOrders(lastOrdersSet);

        prioritySlots.put(1, finalFirstOrders);
        prioritySlots.put(2, finalSecondOrders);
        prioritySlots.put(3, finalLastOrders);
    }

    private List<Order> sortOrders(Set<Order> orders) {
        List<Order> sortedOrders = new ArrayList<>();

        List<Order> vipOrders = getVipOrders(orders);
        List<Order> foodOrders = getFoodOrders(orders);

        sortedOrders.addAll(vipOrders);
        sortedOrders.addAll(foodOrders);

        orders.removeAll(sortedOrders);
        sortedOrders.addAll(orders);

        return  sortedOrders;
    }

    private List<Order> getVipOrders(Set<Order> orders) {
        return  orders.stream().filter(order -> order.getVip()).collect(Collectors.toList());
    }

    private List<Order> getFoodOrders(Set<Order> orders) {
        return  orders.stream().filter(order -> order.getFood() && !order.getVip()).collect(Collectors.toList());
    }
}

