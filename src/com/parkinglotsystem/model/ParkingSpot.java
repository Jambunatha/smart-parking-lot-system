package com.parkinglotsystem.model;

import com.parkinglotsystem.enums.SpotStatus;
import com.parkinglotsystem.enums.VehicleType;
import com.parkinglotsystem.exception.SpotNotAvailableException;

/**
 * Represents a parking spot in the parking lot
 */
public class ParkingSpot {
    private int spotNumber;
    private VehicleType spotSize;
    private SpotStatus status;
    private Vehicle occupiedBy;

    /**
     * Constructor for ParkingSpot
     *
     * @param spotNumber The spot number on the floor
     * @param spotSize   The size of the spot (determines which vehicle types can park here)
     */
    public ParkingSpot(int spotNumber, VehicleType spotSize) {
        this.spotNumber = spotNumber;
        this.spotSize = spotSize;
        this.status = SpotStatus.AVAILABLE;
        this.occupiedBy = null;
    }

    /**
     * Occupies the parking spot with a vehicle
     *
     * @param vehicle The vehicle to occupy the spot
     * @throws SpotNotAvailableException if spot is not available
     */
    public synchronized void occupy(Vehicle vehicle) {
        if (status != SpotStatus.AVAILABLE) {
            throw new SpotNotAvailableException("Spot " + spotNumber + " is not available");
        }
        this.occupiedBy = vehicle;
        this.status = SpotStatus.OCCUPIED;
    }

    /**
     * Vacates the parking spot (removes the vehicle)
     */
    public synchronized void vacate() {
        this.occupiedBy = null;
        this.status = SpotStatus.AVAILABLE;
    }

    /**
     * Checks if the spot can accommodate a vehicle of the given type
     *
     * @param vehicleType The type of vehicle
     * @return true if the vehicle can fit, false otherwise
     */
    public boolean canFit(VehicleType vehicleType) {
        // Spot can fit a vehicle if:
        // 1. Spot is for the same size: always fits
        // 2. Spot is larger: can fit smaller vehicles
        return vehicleType.getSizeRanking() <= spotSize.getSizeRanking();
    }

    public boolean isAvailable() {
        return status == SpotStatus.AVAILABLE;
    }

    public int getSpotNumber() {
        return spotNumber;
    }

    public VehicleType getSpotSize() {
        return spotSize;
    }

    public SpotStatus getStatus() {
        return status;
    }

    public Vehicle getOccupiedBy() {
        return occupiedBy;
    }

    @Override
    public String toString() {
        return "ParkingSpot{" +
                "spotNumber=" + spotNumber +
                ", spotSize=" + spotSize +
                ", status=" + status +
                ", occupiedBy=" + (occupiedBy != null ? occupiedBy.getLicensePlate() : "None") +
                '}';
    }
}
