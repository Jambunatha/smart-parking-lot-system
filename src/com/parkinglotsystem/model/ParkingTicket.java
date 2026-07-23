package com.parkinglotsystem.model;

import com.parkinglotsystem.enums.ParkingTicketStatus;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

/**
 * Represents a parking ticket for a vehicle's parking session
 */
public class ParkingTicket {
    private String ticketId;
    private Vehicle vehicle;
    private ParkingSpot parkingSpot;
    private LocalDateTime entryTime;
    private LocalDateTime exitTime;
    private ParkingTicketStatus status;
    private double fee;

    /**
     * Constructor for ParkingTicket
     *
     * @param vehicle     The vehicle being parked
     * @param parkingSpot The parking spot assigned to the vehicle
     */
    public ParkingTicket(Vehicle vehicle, ParkingSpot parkingSpot) {
        this.ticketId = UUID.randomUUID().toString();
        this.vehicle = vehicle;
        this.parkingSpot = parkingSpot;
        this.entryTime = LocalDateTime.now();
        this.exitTime = null;
        this.status = ParkingTicketStatus.ACTIVE;
        this.fee = 0.0;
    }

    /**
     * Marks the ticket as completed (check-out) and sets the fee
     *
     * @param fee The calculated parking fee
     */
    public void markAsCompleted(double fee) {
        this.exitTime = LocalDateTime.now();
        this.status = ParkingTicketStatus.COMPLETED;
        this.fee = fee;
    }

    /**
     * Calculates the total duration of parking in minutes
     *
     * @return Duration in minutes, or 0 if vehicle hasn't checked out yet
     */
    public long getTotalDuration() {
        if (exitTime == null) {
            return 0;
        }
        return ChronoUnit.MINUTES.between(entryTime, exitTime);
    }

    /**
     * Calculates the total duration in hours (rounded up)
     *
     * @return Duration in hours, rounded up
     */
    public long getDurationInHours() {
        long minutes = getTotalDuration();
        return (long) Math.ceil(minutes / 60.0);
    }

    public String getTicketId() {
        return ticketId;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    public ParkingSpot getParkingSpot() {
        return parkingSpot;
    }

    public LocalDateTime getEntryTime() {
        return entryTime;
    }

    public LocalDateTime getExitTime() {
        return exitTime;
    }

    public ParkingTicketStatus getStatus() {
        return status;
    }

    public double getFee() {
        return fee;
    }

    @Override
    public String toString() {
        return "ParkingTicket{" +
                "ticketId='" + ticketId + '\'' +
                ", vehicle=" + vehicle +
                ", parkingSpot=" + parkingSpot.getSpotNumber() +
                ", entryTime=" + entryTime +
                ", exitTime=" + exitTime +
                ", status=" + status +
                ", fee=" + fee +
                '}';
    }
}
