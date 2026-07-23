package com.parkinglotsystem.exception;

/**
 * Exception thrown when an invalid vehicle ticket is provided
 */
public class InvalidVehicleException extends ParkingLotException {
    public InvalidVehicleException(String message) {
        super(message);
    }
}
