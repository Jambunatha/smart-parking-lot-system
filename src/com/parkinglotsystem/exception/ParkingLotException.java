package com.parkinglotsystem.exception;

/**
 * Base exception for parking lot system
 */
public class ParkingLotException extends RuntimeException {
    public ParkingLotException(String message) {
        super(message);
    }

    public ParkingLotException(String message, Throwable cause) {
        super(message, cause);
    }
}
