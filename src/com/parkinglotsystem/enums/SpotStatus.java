package com.parkinglotsystem.enums;

/**
 * Enumeration representing the status of a parking spot
 */
public enum SpotStatus {
    AVAILABLE("Available for parking"),
    OCCUPIED("Currently occupied"),
    RESERVED("Reserved for operations"),
    MAINTENANCE("Under maintenance");

    private final String description;

    SpotStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
