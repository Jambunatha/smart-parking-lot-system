package com.parkinglotsystem.model;

import com.parkinglotsystem.enums.VehicleType;
import com.parkinglotsystem.exception.InvalidVehicleException;

/**
 * Represents a vehicle in the parking lot system
 */
public class Vehicle {
    private String licensePlate;
    private VehicleType vehicleType;

    /**
     * Constructor for Vehicle
     *
     * @param licensePlate The license plate of the vehicle
     * @param vehicleType  The type of vehicle
     * @throws InvalidVehicleException if license plate is null or empty
     */
    public Vehicle(String licensePlate, VehicleType vehicleType) {
        if (licensePlate == null || licensePlate.trim().isEmpty()) {
            throw new InvalidVehicleException("License plate cannot be null or empty");
        }
        this.licensePlate = licensePlate.trim();
        this.vehicleType = vehicleType;
    }

    public String getLicensePlate() {
        return licensePlate;
    }

    public VehicleType getVehicleType() {
        return vehicleType;
    }

    @Override
    public String toString() {
        return "Vehicle{" +
                "licensePlate='" + licensePlate + '\'' +
                ", vehicleType=" + vehicleType +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Vehicle vehicle = (Vehicle) o;

        return licensePlate.equals(vehicle.licensePlate);
    }

    @Override
    public int hashCode() {
        return licensePlate.hashCode();
    }
}
