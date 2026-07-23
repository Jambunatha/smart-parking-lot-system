package com.parkinglotsystem.enums;

/**
 * Enumeration representing different types of vehicles
 */
public enum VehicleType {
    MOTORCYCLE("Motorcycle", 1),
    CAR("Car", 2),
    BUS("Bus", 3);

    private final String displayName;
    private final int sizeRanking;

    VehicleType(String displayName, int sizeRanking) {
        this.displayName = displayName;
        this.sizeRanking = sizeRanking;
    }

    public String getDisplayName() {
        return displayName;
    }

    public int getSizeRanking() {
        return sizeRanking;
    }
}
