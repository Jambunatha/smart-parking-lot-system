package com.parkinglotsystem.model;

import com.parkinglotsystem.enums.VehicleType;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents the entire parking lot with multiple floors
 */
public class ParkingLot {
    private List<ParkingFloor> floors;
    private int totalFloors;
    private int spotsPerFloor;

    /**
     * Constructor for ParkingLot
     *
     * @param totalFloors   Number of floors in the parking lot
     * @param spotsPerFloor Number of spots per floor
     */
    public ParkingLot(int totalFloors, int spotsPerFloor) {
        this.totalFloors = totalFloors;
        this.spotsPerFloor = spotsPerFloor;
        this.floors = new ArrayList<>();
        initializeFloors();
    }

    /**
     * Initialize all floors in the parking lot
     */
    private void initializeFloors() {
        for (int i = 1; i <= totalFloors; i++) {
            floors.add(new ParkingFloor(i, spotsPerFloor));
        }
    }

    /**
     * Finds an available parking spot for the given vehicle type
     *
     * @param vehicleType The type of vehicle
     * @return An available ParkingSpot or null if none available
     */
    public synchronized ParkingSpot getAvailableSpot(VehicleType vehicleType) {
        for (ParkingFloor floor : floors) {
            ParkingSpot spot = floor.getAvailableSpot(vehicleType);
            if (spot != null) {
                return spot;
            }
        }
        return null;
    }

    /**
     * Counts the total number of available spots
     *
     * @return The count of available spots
     */
    public synchronized int getAvailableSpotsCount() {
        return floors.stream()
                .mapToInt(ParkingFloor::getAvailableSpotsCount)
                .sum();
    }

    /**
     * Gets all information about spot availability by type
     *
     * @return String with availability details
     */
    public String getAvailabilityDetails() {
        StringBuilder sb = new StringBuilder();
        sb.append("=== Parking Lot Availability ===\n");
        sb.append("Total Floors: ").append(totalFloors).append("\n");
        sb.append("Spots Per Floor: ").append(spotsPerFloor).append("\n");
        sb.append("Total Available Spots: ").append(getAvailableSpotsCount())
                .append("/").append(totalFloors * spotsPerFloor).append("\n");
        sb.append("\nFloor Details:\n");
        for (ParkingFloor floor : floors) {
            sb.append("  ").append(floor.toString()).append("\n");
        }
        return sb.toString();
    }

    public List<ParkingFloor> getFloors() {
        return floors;
    }

    public int getTotalFloors() {
        return totalFloors;
    }

    public int getSpotsPerFloor() {
        return spotsPerFloor;
    }

    @Override
    public String toString() {
        return "ParkingLot{" +
                "totalFloors=" + totalFloors +
                ", spotsPerFloor=" + spotsPerFloor +
                ", totalSpots=" + (totalFloors * spotsPerFloor) +
                ", availableSpots=" + getAvailableSpotsCount() +
                '}';
    }
}
