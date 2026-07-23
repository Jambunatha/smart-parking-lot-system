package com.parkinglotsystem.exception;

/**
 * Exception thrown when no parking spots are available
 */
public class NoSpotsAvailableException extends ParkingLotException {
    public NoSpotsAvailableException(String message) {
        super(message);
    }
}
