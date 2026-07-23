package com.parkinglotsystem.manager;

import com.parkinglotsystem.enums.VehicleType;
import com.parkinglotsystem.exception.InvalidTicketException;
import com.parkinglotsystem.exception.NoSpotsAvailableException;
import com.parkinglotsystem.model.*;
import com.parkinglotsystem.service.FeeCalculationService;
import com.parkinglotsystem.service.ParkingSpotFinder;

/**
 * Main manager for the parking lot system
 * Orchestrates all parking lot operations including check-in, check-out, and fee calculations
 */
public class ParkingLotManager {
    private ParkingLot parkingLot;
    private FeeCalculationService feeCalculationService;
    private ParkingSpotFinder parkingSpotFinder;

    /**
     * Constructor for ParkingLotManager
     *
     * @param floors        Number of floors in the parking lot
     * @param spotsPerFloor Number of parking spots per floor
     */
    public ParkingLotManager(int floors, int spotsPerFloor) {
        this.parkingLot = new ParkingLot(floors, spotsPerFloor);
        this.feeCalculationService = new FeeCalculationService();
        this.parkingSpotFinder = new ParkingSpotFinder();
    }

    /**
     * Checks in a vehicle to the parking lot
     *
     * @param vehicle The vehicle to check in
     * @return ParkingTicket with parking details
     * @throws NoSpotsAvailableException if no parking spots are available
     */
    public synchronized ParkingTicket checkIn(Vehicle vehicle) throws NoSpotsAvailableException {
        if (vehicle == null) {
            throw new InvalidTicketException("Vehicle cannot be null");
        }

        // Find available spot for the vehicle
        ParkingSpot availableSpot = parkingSpotFinder.findAvailableSpot(
                parkingLot,
                vehicle.getVehicleType()
        );

        // Occupy the spot with the vehicle
        availableSpot.occupy(vehicle);

        // Create and return the parking ticket
        ParkingTicket ticket = new ParkingTicket(vehicle, availableSpot);

        System.out.println("[CHECK-IN] Vehicle: " + vehicle.getLicensePlate() +
                " | Type: " + vehicle.getVehicleType() +
                " | Spot: " + availableSpot.getSpotNumber() +
                " | Floor: " + findFloorForSpot(availableSpot));

        return ticket;
    }

    /**
     * Checks out a vehicle from the parking lot
     *
     * @param ticket The parking ticket for the vehicle
     * @return The calculated parking fee
     * @throws InvalidTicketException if the ticket is invalid or null
     */
    public synchronized double checkOut(ParkingTicket ticket) throws InvalidTicketException {
        if (ticket == null) {
            throw new InvalidTicketException("Ticket cannot be null");
        }

        if (ticket.getStatus().toString().equals("COMPLETED")) {
            throw new InvalidTicketException("Ticket has already been checked out");
        }

        // Get the parking spot and vehicle info
        ParkingSpot spot = ticket.getParkingSpot();
        Vehicle vehicle = ticket.getVehicle();

        // Calculate the parking fee
        double fee = feeCalculationService.calculateFee(ticket);

        // Mark ticket as completed
        ticket.markAsCompleted(fee);

        // Vacate the parking spot
        spot.vacate();

        System.out.println("[CHECK-OUT] Vehicle: " + vehicle.getLicensePlate() +
                " | Type: " + vehicle.getVehicleType() +
                " | Duration: " + ticket.getDurationInHours() + " hours" +
                " | Fee: $" + String.format("%.2f", fee));

        return fee;
    }

    /**
     * Gets the parking spot availability details
     *
     * @return String with availability information
     */
    public String getParkingSpotAvailability() {
        return parkingLot.getAvailabilityDetails();
    }

    /**
     * Gets the total number of available parking spots
     *
     * @return Number of available spots
     */
    public int getAvailableSpotsCount() {
        return parkingLot.getAvailableSpotsCount();
    }

    /**
     * Gets the total number of parking spots in the lot
     *
     * @return Total number of spots
     */
    public int getTotalSpotsCount() {
        return parkingLot.getTotalFloors() * parkingLot.getSpotsPerFloor();
    }

    /**
     * Gets the occupancy rate of the parking lot
     *
     * @return Occupancy percentage (0-100)
     */
    public double getOccupancyRate() {
        int totalSpots = getTotalSpotsCount();
        int occupiedSpots = totalSpots - getAvailableSpotsCount();
        return (occupiedSpots * 100.0) / totalSpots;
    }

    /**
     * Checks if spots are available for a specific vehicle type
     *
     * @param vehicleType The type of vehicle
     * @return true if at least one spot is available, false otherwise
     */
    public boolean hasSpotsAvailable(VehicleType vehicleType) {
        return parkingSpotFinder.hasAvailableSpots(parkingLot, vehicleType);
    }

    /**
     * Helper method to find which floor a parking spot is on
     *
     * @param spot The parking spot
     * @return The floor number
     */
    private int findFloorForSpot(ParkingSpot spot) {
        for (ParkingFloor floor : parkingLot.getFloors()) {
            if (floor.getSpots().contains(spot)) {
                return floor.getFloorNumber();
            }
        }
        return -1;
    }

    /**
     * Gets the parking lot information
     *
     * @return ParkingLot object
     */
    public ParkingLot getParkingLot() {
        return parkingLot;
    }

    /**
     * Gets summary of parking lot status
     *
     * @return String with status summary
     */
    public String getSystemStatus() {
        StringBuilder sb = new StringBuilder();
        sb.append("\n===== PARKING LOT SYSTEM STATUS =====\n");
        sb.append("Total Floors: ").append(parkingLot.getTotalFloors()).append("\n");
        sb.append("Spots Per Floor: ").append(parkingLot.getSpotsPerFloor()).append("\n");
        sb.append("Total Spots: ").append(getTotalSpotsCount()).append("\n");
        sb.append("Available Spots: ").append(getAvailableSpotsCount()).append("\n");
        sb.append("Occupied Spots: ").append(getTotalSpotsCount() - getAvailableSpotsCount()).append("\n");
        sb.append("Occupancy Rate: ").append(String.format("%.2f%%", getOccupancyRate())).append("\n");
        sb.append("=====================================\n");
        return sb.toString();
    }

    @Override
    public String toString() {
        return "ParkingLotManager{" +
                "parkingLot=" + parkingLot +
                ", availableSpots=" + getAvailableSpotsCount() +
                ", occupancyRate=" + String.format("%.2f%%", getOccupancyRate()) +
                '}';
    }
}
