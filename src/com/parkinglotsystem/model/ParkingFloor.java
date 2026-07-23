package com.parkinglotsystem.model;

import com.parkinglotsystem.enums.VehicleType;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a floor in the parking lot with multiple parking spots
 */
public class ParkingFloor {
    private int floorNumber;
    private List<ParkingSpot> spots;

    /**
     * Constructor for ParkingFloor
     *
     * @param floorNumber   The floor number
     * @param spotsPerFloor The number of spots on this floor
     */
    public ParkingFloor(int floorNumber, int spotsPerFloor) {
        this.floorNumber = floorNumber;
        this.spots = new ArrayList<>();
        initializeSpots(spotsPerFloor);
    }

    /**
     * Initialize parking spots on the floor
     * Distributes spots evenly among the three vehicle types
     *
     * @param spotsPerFloor Total number of spots on this floor
     */
    private void initializeSpots(int spotsPerFloor) {
        int spotsPerType = spotsPerFloor / 3;
        int spotNumber = 1;

        // Create motorcycle spots
        for (int i = 0; i < spotsPerType; i++) {
            spots.add(new ParkingSpot(spotNumber++, VehicleType.MOTORCYCLE));
        }

        // Create car spots
        for (int i = 0; i < spotsPerType; i++) {
            spots.add(new ParkingSpot(spotNumber++, VehicleType.CAR));
        }

        // Create bus spots (remaining)
        for (int i = spotsPerType * 2; i < spotsPerFloor; i++) {
            spots.add(new ParkingSpot(spotNumber++, VehicleType.BUS));
        }
    }

    /**
     * Finds an available parking spot for the given vehicle type
     *
     * @param vehicleType The type of vehicle
     * @return An available ParkingSpot or null if none available
     */
    public synchronized ParkingSpot getAvailableSpot(VehicleType vehicleType) {
        // First pass: Look for a spot of the same size
        for (ParkingSpot spot : spots) {
            if (spot.isAvailable() && spot.getSpotSize() == vehicleType) {
                return spot;
            }
        }

        // Second pass: Look for larger spots that can accommodate the vehicle
        for (ParkingSpot spot : spots) {
            if (spot.isAvailable() && spot.canFit(vehicleType)) {
                return spot;
            }
        }

        return null;
    }

    /**
     * Counts the number of available spots
     *
     * @return The count of available spots
     */
    public synchronized int getAvailableSpotsCount() {
        return (int) spots.stream().filter(ParkingSpot::isAvailable).count();
    }

    /**
     * Gets the total number of spots on this floor
     *
     * @return The total spot count
     */
    public int getTotalSpotsCount() {
        return spots.size();
    }

    public int getFloorNumber() {
        return floorNumber;
    }

    public List<ParkingSpot> getSpots() {
        return spots;
    }

    @Override
    public String toString() {
        return "ParkingFloor{" +
                "floorNumber=" + floorNumber +
                ", totalSpots=" + spots.size() +
                ", availableSpots=" + getAvailableSpotsCount() +
                '}';
    }
}
