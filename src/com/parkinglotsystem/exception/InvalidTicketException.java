package com.parkinglotsystem.exception;

/**
 * Exception thrown when an invalid parking ticket is provided
 */
public class InvalidTicketException extends ParkingLotException {
    public InvalidTicketException(String message) {
        super(message);
    }
}
