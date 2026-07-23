package com.parkinglotsystem.enums;

/**
 * Enumeration representing the status of a parking ticket
 */
public enum ParkingTicketStatus {
    ACTIVE("Vehicle is currently parked"),
    COMPLETED("Vehicle has checked out"),
    CANCELLED("Ticket has been cancelled");

    private final String description;

    ParkingTicketStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
