package com.parkinglotsystem.service;

import com.parkinglotsystem.enums.VehicleType;
import com.parkinglotsystem.exception.NoSpotsAvailableException;
import com.parkinglotsystem.model.ParkingLot;
import com.parkinglotsystem.model.ParkingSpot;

/**
 * Service for finding available parking spots
 */
public class ParkingSpotFinder {

    /**
     * Finds an available parking spot for the given vehicle type
     *
     * @param parkingLot  The parking lot to search in
     * @param vehicleType The type of vehicle
     * @return An available parking spot
     * @throws NoSpotsAvailableException if no spot is available
     */
    public ParkingSpot findAvailableSpot(ParkingLot parkingLot, VehicleType vehicleType)
            throws NoSpotsAvailableException {
        ParkingSpot spot = parkingLot.getAvailableSpot(vehicleType);
        if (spot == null) {
            throw new NoSpotsAvailableException(
                    "No available parking spots for vehicle type: " + vehicleType.getDisplayName()
            );
        }
        return spot;
    }

    /**
     * Gets the total number of available spots in the parking lot
     *
     * @param parkingLot The parking lot to check
     * @return The number of available spots
     */
    public int getAvailableSpotsCount(ParkingLot parkingLot) {
        return parkingLot.getAvailableSpotsCount();
    }

    /**
     * Checks if any spots are available for the given vehicle type
     *
     * @param parkingLot  The parking lot to check
     * @param vehicleType The type of vehicle
     * @return true if at least one spot is available, false otherwise
     */
    public boolean hasAvailableSpots(ParkingLot parkingLot, VehicleType vehicleType) {
        return parkingLot.getAvailableSpot(vehicleType) != null;
    }
}
