package com.parkinglotsystem.service;

import com.parkinglotsystem.enums.VehicleType;
import com.parkinglotsystem.model.ParkingTicket;

import java.util.HashMap;
import java.util.Map;

/**
 * Service for calculating parking fees based on vehicle type and duration
 */
public class FeeCalculationService {

    // Hourly rates for different vehicle types
    private static final Map<VehicleType, Double> HOURLY_RATES = new HashMap<>();

    static {
        HOURLY_RATES.put(VehicleType.MOTORCYCLE, 2.0);
        HOURLY_RATES.put(VehicleType.CAR, 3.0);
        HOURLY_RATES.put(VehicleType.BUS, 5.0);
    }

    /**
     * Calculates the parking fee for a given ticket
     *
     * @param ticket The parking ticket to calculate fee for
     * @return The calculated fee
     */
    public double calculateFee(ParkingTicket ticket) {
        long durationMinutes = ticket.getTotalDuration();

        // Round up to the nearest hour
        long chargedHours = (long) Math.ceil(durationMinutes / 60.0);

        // Ensure minimum charge of 1 hour
        if (chargedHours < 1) {
            chargedHours = 1;
        }

        VehicleType vehicleType = ticket.getVehicle().getVehicleType();
        double hourlyRate = HOURLY_RATES.get(vehicleType);

        return chargedHours * hourlyRate;
    }

    /**
     * Gets the hourly rate for a specific vehicle type
     *
     * @param vehicleType The type of vehicle
     * @return The hourly rate
     */
    public double getHourlyRate(VehicleType vehicleType) {
        return HOURLY_RATES.get(vehicleType);
    }

    /**
     * Gets all available rates
     *
     * @return Map of vehicle types to hourly rates
     */
    public Map<VehicleType, Double> getAllRates() {
        return new HashMap<>(HOURLY_RATES);
    }

    /**
     * Calculates fee with explicit parameters
     *
     * @param durationMinutes Duration of parking in minutes
     * @param vehicleType     Type of vehicle
     * @return The calculated fee
     */
    public double calculateFeeByDuration(long durationMinutes, VehicleType vehicleType) {
        long chargedHours = (long) Math.ceil(durationMinutes / 60.0);
        if (chargedHours < 1) {
            chargedHours = 1;
        }
        return chargedHours * HOURLY_RATES.get(vehicleType);
    }
}
