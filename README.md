# Smart Parking Lot System

A comprehensive backend system for managing smart parking lots with vehicle entry/exit management, parking space allocation, and fee calculation.

## Overview

This project implements a low-level architecture for a smart parking lot system that handles:
- **Parking Spot Allocation**: Automatically assigns available parking spots based on vehicle size
- **Check-In/Check-Out**: Records vehicle entry and exit times
- **Parking Fee Calculation**: Calculates fees based on duration and vehicle type
- **Real-Time Availability Updates**: Manages parking spot availability in real-time
- **Concurrency Handling**: Supports multiple simultaneous vehicle operations

## Project Structure

```
smart-parking-lot-system/
├── docs/
│   ├── ARCHITECTURE.md      # Detailed architecture documentation
│   └── QUICK_START.md       # Quick start guide
├── src/
│   └── com/parkinglotsystem/
│       ├── Main.java        # Entry point
│       ├── model/           # Data models (Vehicle, ParkingSpot, ParkingTicket)
│       ├── service/         # Business logic services
│       ├── manager/         # System managers (ParkingLotManager)
│       ├── enums/           # Enumerations (VehicleType, SpotStatus)
│       └── exception/       # Custom exceptions
└── README.md                # This file
```

## Key Components

### 1. **Models**
- `Vehicle`: Represents a vehicle with size and license plate
- `ParkingSpot`: Represents an individual parking spot with size and status
- `ParkingFloor`: Represents a floor in the parking lot
- `ParkingTicket`: Represents a parking transaction

### 2. **Enumerations**
- `VehicleType`: MOTORCYCLE, CAR, BUS
- `SpotStatus`: AVAILABLE, OCCUPIED, RESERVED, MAINTENANCE
- `ParkingTicketStatus`: ACTIVE, COMPLETED, CANCELLED

### 3. **Services**
- `FeeCalculationService`: Handles fee calculations based on vehicle type and duration
- `ParkingSpotFinder`: Finds available parking spots efficiently

### 4. **Managers**
- `ParkingLotManager`: Main orchestrator managing vehicle check-in/out and spot allocation

### 5. **Exception Handling**
- Custom exceptions for various error scenarios

## Functional Requirements Implementation

### Parking Spot Allocation
- Spots are automatically assigned based on vehicle size
- Motorcycle spots can only fit motorcycles
- Car spots can fit motorcycles or cars
- Bus spots can fit all vehicle types
- Efficient search algorithm prioritizes appropriate-sized spots

### Check-In and Check-Out
- Records precise entry time
- Records exit time upon check-out
- Maintains parking transaction history

### Parking Fee Calculation
- Base hourly rates vary by vehicle type
- Partial hours are rounded up
- Configurable rates per vehicle type
- Current default rates:
    - Motorcycle: $2/hour
    - Car: $3/hour
    - Bus: $5/hour

### Real-Time Availability Update
- Spot status updated immediately upon check-in/out
- Thread-safe operations for concurrent access
- Status tracking: AVAILABLE, OCCUPIED, RESERVED, MAINTENANCE

### Concurrency Handling
- Uses synchronization mechanisms for thread safety
- Supports multiple simultaneous check-in/check-out operations
- Prevents race conditions on spot allocation

## Architecture Highlights

### Design Patterns Used
- **Singleton Pattern**: ParkingLotManager
- **Factory Pattern**: Vehicle and ParkingSpot creation
- **Observer Pattern**: Real-time availability updates
- **Strategy Pattern**: Fee calculation strategies

### Key Algorithms
1. **Spot Allocation Algorithm**:
    - Searches for available spots in optimal size order
    - Prevents fragmentation by using appropriate-sized spots
    - O(n) time complexity per search

2. **Fee Calculation Algorithm**:
    - Calculates duration between check-in and check-out
    - Applies vehicle-type-specific hourly rates
    - Rounds up to nearest hour

## Testing Strategy

The project includes comprehensive unit and integration tests covering:
- Vehicle creation and validation
- Parking spot allocation scenarios
- Check-in/check-out flows
- Fee calculation accuracy
- Concurrent operations
- Edge cases and error scenarios

## Configuration

### Parking Lot Setup
```java
// Create a parking lot with 5 floors, 20 spots per floor
ParkingLotManager manager = new ParkingLotManager(5, 20);
```

### Fee Rates Configuration
```java
// Customize fees in FeeCalculationService
FeeCalculationService feeService = new FeeCalculationService();
// Default rates are predefined; can be extended for custom rates
```

## Performance Considerations

- **Space Complexity**: O(floors × spots)
- **Spot Search Time**: O(spots)
- **Check-in/Check-out Time**: O(spots) due to spot search
- **Thread Safety**: Synchronized methods ensure safe concurrent access

## Future Enhancements

1. Database integration (MySQL/PostgreSQL)
2. REST API endpoints
3. Real-time monitoring dashboard
4. Advanced analytics and reporting
5. Mobile app integration
6. Dynamic pricing based on occupancy
7. Reservation system
8. Payment gateway integration

## Support

For questions or issues, please refer to:
- `ARCHITECTURE.md` for detailed design information
- `QUICK_START.md` for setup and usage instructions
---
