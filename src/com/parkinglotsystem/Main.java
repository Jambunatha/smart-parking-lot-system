package com.parkinglotsystem;

import com.parkinglotsystem.enums.VehicleType;
import com.parkinglotsystem.manager.ParkingLotManager;
import com.parkinglotsystem.model.ParkingTicket;
import com.parkinglotsystem.model.Vehicle;

/**
 * Main application to showcase the Smart Parking Lot System
 */
public class Main {

    public static void main(String[] args) throws InterruptedException {

        System.out.println("\n================================================");
        System.out.println("WELCOME TO SMART PARKING LOT SYSTEM");
        System.out.println("================================================\n");

        // Create a parking lot with 3 floors and 12 spots per floor
        ParkingLotManager parkingLot = new ParkingLotManager(3, 12);

        System.out.println(parkingLot.getSystemStatus());

        // 1) Check-in and check-out a car
        System.out.println("\n--- 1. Single Vehicle Parking ---");
        Vehicle car1 = new Vehicle("ABC123", VehicleType.CAR);
        ParkingTicket ticket1 = parkingLot.checkIn(car1);
        System.out.println("-> Vehicle parked. Ticket ID: " + ticket1.getTicketId().substring(0, 8) + "...");
        System.out.println("  Available spots remaining: " + parkingLot.getAvailableSpotsCount());

        Thread.sleep(2000); // Simulate parking for 2 seconds

        double fee1 = parkingLot.checkOut(ticket1);
        System.out.println("-> Vehicle checked out. Parking fee: $" + String.format("%.2f", fee1));
        System.out.println("  Available spots: " + parkingLot.getAvailableSpotsCount());

        // 2) Multiple vehicles parking
        System.out.println("\n--- 2. Multiple Vehicles ---");
        Vehicle car2 = new Vehicle("DEF456", VehicleType.CAR);
        Vehicle motorcycle = new Vehicle("XYZ789", VehicleType.MOTORCYCLE);
        Vehicle bus = new Vehicle("BUS001", VehicleType.BUS);

        ParkingTicket ticket2 = parkingLot.checkIn(car2);
        System.out.println("-> Car (DEF456) parked");

        ParkingTicket ticket3 = parkingLot.checkIn(motorcycle);
        System.out.println("-> Motorcycle (XYZ789) parked");

        ParkingTicket ticket4 = parkingLot.checkIn(bus);
        System.out.println("-> Bus (BUS001) parked");

        System.out.println("  Total vehicles parked: 3");
        System.out.println("  Available spots: " + parkingLot.getAvailableSpotsCount() + " / " +
                parkingLot.getTotalSpotsCount());
        System.out.println("  Occupancy rate: " + String.format("%.2f%%", parkingLot.getOccupancyRate()));

        // 3) Vehicle check-out with fee calculation
        System.out.println("\n--- 3. Fee Calculation ---");

        Thread.sleep(3000); // Simulate longer parking

        double fee2 = parkingLot.checkOut(ticket2);
        System.out.println("-> Car checked out. Fee: $" + String.format("%.2f", fee2));

        double fee3 = parkingLot.checkOut(ticket3);
        System.out.println("-> Motorcycle checked out. Fee: $" + String.format("%.2f", fee3));

        double fee4 = parkingLot.checkOut(ticket4);
        System.out.println("-> Bus checked out. Fee: $" + String.format("%.2f", fee4));

        System.out.println("  Total revenue: $" + String.format("%.2f", fee1 + fee2 + fee3 + fee4));

        // 4) Parking lot at capacity
        System.out.println("\n--- 4. Parking Lot Capacity ---");
        System.out.println("Filling parking lot to capacity...");

        int vehicleCount = 0;
        try {
            while (vehicleCount < parkingLot.getTotalSpotsCount()) {
                Vehicle v = new Vehicle("VEHI-" + vehicleCount, VehicleType.CAR);
                parkingLot.checkIn(v);
                vehicleCount++;
            }
        } catch (Exception e) {
            System.out.println("  Exception: " + e.getMessage());
        }

        System.out.println("  Vehicles parked: " + (parkingLot.getTotalSpotsCount() - parkingLot.getAvailableSpotsCount()));
        System.out.println("  Available spots: " + parkingLot.getAvailableSpotsCount());
        System.out.println("  Occupancy rate: " + String.format("%.2f%%", parkingLot.getOccupancyRate()));

        // 5) Show final status
        System.out.println("\n--- FINAL STATUS ---");
        System.out.println(parkingLot.getParkingSpotAvailability());

        System.out.println("\n================================================");
    }
}
