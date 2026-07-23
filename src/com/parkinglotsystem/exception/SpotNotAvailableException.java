package com.parkinglotsystem.exception;

/**
 * Exception thrown when a parking spot is not available
 */
public class SpotNotAvailableException extends ParkingLotException {
    public SpotNotAvailableException(String message) {
        super(message);
    }
}
