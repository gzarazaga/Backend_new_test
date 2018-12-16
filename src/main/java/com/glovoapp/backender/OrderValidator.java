package com.glovoapp.backender;

import org.springframework.stereotype.Component;

@Component
public class OrderValidator {

    public boolean validateBox(Order o, Boolean box) {
        if (o.getDescription().contains("pizza") || o.getDescription().contains("Pizza") || o.getDescription().contains("PIZZA")
                || o.getDescription().contains("cake") || o.getDescription().contains("Cake") || o.getDescription().contains("CAKE")
                || o.getDescription().contains("flamingo")|| o.getDescription().contains("Flamingo") || o.getDescription().contains("FLAMINGO")) {
            return box;
        }
        return true;
    }

    public boolean validateDistance(Order o, Location courierLocation, Vehicle courierVehicle) {
        double distance = DistanceCalculator.calculateDistance(o.getPickup(), courierLocation);

        if (distance > 5) {
            return (Vehicle.MOTORCYCLE.equals(courierVehicle) || Vehicle.ELECTRIC_SCOOTER.equals(courierVehicle));
        }
        return true;
    }
}
