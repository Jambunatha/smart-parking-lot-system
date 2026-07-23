# Smart Parking Lot System - Architecture Documentation

## Table of Contents
1. [System Overview](#system-overview)
2. [Component Architecture](#component-architecture)
3. [Data Model](#data-model)
4. [Algorithms](#algorithms)
5. [Concurrency Management](#concurrency-management)
6. [Design Patterns](#design-patterns)

## System Overview

The Smart Parking Lot System is designed as a multi-layered architecture:

```
┌─────────────────────────────────────┐
│    ParkingLotManager                │
├─────────────────────────────────────┤
│  Service Layer                      │
│  ├─ FeeCalculationService           │
│  └─ ParkingSpotFinder               │
├─────────────────────────────────────┤
│  Data Model Layer                   │
│  ├─ ParkingLot                      │
│  ├─ ParkingFloor                    │
│  ├─ ParkingSpot                     │
│  ├─ Vehicle                         │
│  └─ ParkingTicket                   │
├─────────────────────────────────────┤
│  Enum & Exception Layer             │
│  ├─ VehicleType                     │
│  ├─ SpotStatus                      │
│  ├─ ParkingTicketStatus             │
│  └─ Custom Exceptions               │
└─────────────────────────────────────┘
```

## Component Architecture

### 1. ParkingLotManager
**Responsibility**: Central management point for all parking lot operations

**Key Methods**:
- `checkIn(Vehicle vehicle)`: Allocates a parking spot and creates a ticket
- `checkOut(ParkingTicket ticket)`: Completes parking transaction and calculates fee
- `getParkingSpotAvailability()`: Returns current availability statistics
- `getAvailableSpotsCount()`: Returns total available spots

**Thread Safety**: All public methods are synchronized

### 2. Data Models

#### Vehicle
```
Vehicle
├─ licensePlate: String (Unique identifier)
├─ vehicleType: VehicleType (MOTORCYCLE, CAR, BUS)
└─ Methods:
   ├─ getLicensePlate()
   └─ getVehicleType()
```

#### ParkingSpot
```
ParkingSpot
├─ spotNumber: int
├─ spotSize: VehicleType (Motorcycle, Car, Bus)
├─ status: SpotStatus
├─ occupiedBy: Vehicle (nullable)
└─ Methods:
   ├─ isAvailable()
   ├─ occupy(Vehicle)
   ├─ vacate()
   └─ getSpotSize()
```

#### ParkingFloor
```
ParkingFloor
├─ floorNumber: int
├─ spots: List<ParkingSpot>
└─ Methods:
   ├─ getAvailableSpot(VehicleType)
   └─ getAvailableSpotsCount()
```

#### ParkingTicket
```
ParkingTicket
├─ ticketId: String (UUID)
├─ vehicle: Vehicle
├─ parkingSpot: ParkingSpot
├─ entryTime: LocalDateTime
├─ exitTime: LocalDateTime (nullable)
├─ status: ParkingTicketStatus
├─ fee: double (nullable)
└─ Methods:
   ├─ getTotalDuration()
   └─ markAsCompleted()
```

#### ParkingLot
```
ParkingLot
├─ floors: List<ParkingFloor>
├─ totalFloors: int
├─ spotsPerFloor: int
└─ Methods:
   ├─ getAvailableSpot(VehicleType)
   └─ getAvailableSpotsCount()
```

### 3. Service Layer

#### FeeCalculationService
Implements fee calculation logic based on:
- **Vehicle Type**: Different hourly rates
- **Duration**: Time spent in parking lot
- **Rounding**: Rounds up to nearest hour

**Rates** (Configurable):
```
MOTORCYCLE: $2.00 per hour
CAR:        $3.00 per hour
BUS:        $5.00 per hour
```

**Example**:
- Vehicle: Car, Duration: 2.5 hours
- Calculation: 3 hours (rounded up) × $3/hour = $9.00

#### ParkingSpotFinder
Implements the parking spot allocation algorithm

**Algorithm Priority** (Spot Size by Vehicle Type):
```
MOTORCYCLE → Motorcycle spots (preferred) → Car spots → Bus spots
CAR        → Car spots (preferred) → Bus spots
BUS        → Bus spots (only option)
```

### 4. Enumerations

#### VehicleType
```java
MOTORCYCLE  // Smallest vehicle
CAR         // Medium vehicle
BUS         // Largest vehicle
```

#### SpotStatus
```java
AVAILABLE   // Ready for occupation
OCCUPIED    // Currently occupied
RESERVED    // Reserved for maintenance/operations
MAINTENANCE // Under maintenance
```

#### ParkingTicketStatus
```java
ACTIVE      // Vehicle currently parked
COMPLETED   // Check-out completed
CANCELLED   // Transaction cancelled
```

## Data Model

### Database Schema (For Future SQL Implementation)

```sql
-- Parking Lot Schema
CREATE TABLE parking_lot (
    lot_id INT PRIMARY KEY,
    name VARCHAR(255),
    total_floors INT,
    spots_per_floor INT
);

CREATE TABLE parking_floor (
    floor_id INT PRIMARY KEY,
    lot_id INT FOREIGN KEY,
    floor_number INT,
    FOREIGN KEY (lot_id) REFERENCES parking_lot(lot_id)
);

CREATE TABLE parking_spot (
    spot_id INT PRIMARY KEY,
    floor_id INT FOREIGN KEY,
    spot_number INT,
    spot_size ENUM('MOTORCYCLE', 'CAR', 'BUS'),
    status ENUM('AVAILABLE', 'OCCUPIED', 'RESERVED', 'MAINTENANCE'),
    FOREIGN KEY (floor_id) REFERENCES parking_floor(floor_id)
);

CREATE TABLE vehicle (
    vehicle_id INT PRIMARY KEY AUTO_INCREMENT,
    license_plate VARCHAR(50) UNIQUE,
    vehicle_type ENUM('MOTORCYCLE', 'CAR', 'BUS')
);

CREATE TABLE parking_ticket (
    ticket_id VARCHAR(36) PRIMARY KEY,
    vehicle_id INT FOREIGN KEY,
    spot_id INT FOREIGN KEY,
    entry_time DATETIME,
    exit_time DATETIME,
    status ENUM('ACTIVE', 'COMPLETED', 'CANCELLED'),
    fee DECIMAL(10, 2),
    FOREIGN KEY (vehicle_id) REFERENCES vehicle(vehicle_id),
    FOREIGN KEY (spot_id) REFERENCES parking_spot(spot_id)
);
```
## Algorithms

### 1. Parking Spot Allocation Algorithm

**Objective**: Find optimal parking spot for incoming vehicle

**Algorithm**: Best-Fit Strategy
```
1. Determine vehicle size (MOTORCYCLE, CAR, or BUS)
2. Search for available spots in priority order:
   a. First, look for spots matching vehicle size
   b. If found, return that spot
   c. If not found, search larger spot types (if applicable)
3. Return first available spot found
4. If no spot available, throw ParkingLotException
```

**Time Complexity**: O(n) where n = number of spots
**Space Complexity**: O(1)

**Pseudo-code**:
```
function findAvailableSpot(vehicleType):
    for each floor in parking_lot.floors:
        // First pass: Perfect fit
        spot = floor.getAvailableSpot(vehicleType)
        if spot exists:
            return spot
    
    // Second pass: Larger sizes (if applicable)
    if vehicleType == MOTORCYCLE:
        return findAvailableSpot(CAR)
    else if vehicleType == CAR:
        return findAvailableSpot(BUS)
    
    throw ParkingLotException("No spots available")
```

### 2. Fee Calculation Algorithm

**Objective**: Calculate parking fee based on duration and vehicle type

**Algorithm**: Duration-Based Pricing
```
1. Calculate duration = exitTime - entryTime
2. Round up duration to nearest hour
3. Fetch hourly rate for vehicle type
4. Calculate fee = rounding_up(duration) × hourly_rate
5. Return fee
```

**Formula**:
```
Duration (hours) = ceiling((exitTime - entryTime) / 3600 seconds)
Fee = Duration × Rate[vehicleType]
```

**Example**:
```
Entry: 10:00 AM
Exit:  12:30 PM
Duration: 2.5 hours → Round up to 3 hours

Vehicle Type: CAR (Rate = $3/hour)
Fee = 3 × $3 = $9.00
```

## Concurrency Management

### Thread Safety Strategy

**Problem**: Multiple vehicles entering/exiting simultaneously

**Solution**: Synchronization at Critical Sections

```java
synchronized public ParkingTicket checkIn(Vehicle vehicle) {
    // Find available spot (critical section)
    ParkingSpot spot = parkingLot.getAvailableSpot(vehicle.getVehicleType());
    
    // Occupy spot (state modification)
    spot.occupy(vehicle);
    
    // Create ticket
    return createTicket(vehicle, spot);
}

synchronized public double checkOut(ParkingTicket ticket) {
    // Vacate spot (critical section)
    ticket.getParkingSpot().vacate();
    
    // Calculate fee
    double fee = calculateFee(ticket);
    
    // Update ticket status
    ticket.markAsCompleted(fee);
    
    return fee;
}
```

### Lock Strategy

**Type**: Re-entrant Lock (via synchronized keyword)
**Scope**: Method-level for simplicity
**Alternative**: Could use `java.util.concurrent.locks.ReentrantReadWriteLock` for read-heavy workloads

### Race Condition Prevention

1. **Spot Allocation Race**:
    - Problem: Two threads finding same spot simultaneously
    - Solution: Atomic spot allocation within synchronized method

2. **Status Update Race**:
    - Problem: Spot status updated by multiple threads
    - Solution: Spot status changes only within synchronized methods

3. **Ticket State Race**:
    - Problem: Ticket modified by concurrent operations
    - Solution: Ticket state changes within synchronized blocks

## Design Patterns

### 1. Singleton Pattern
```java
public class ParkingLotManager {
    private static ParkingLotManager instance;
    
    public static synchronized ParkingLotManager getInstance() {
        if (instance == null) {
            instance = new ParkingLotManager();
        }
        return instance;
    }
}
```

**Purpose**: Ensure single instance of parking lot manager

### 2. Factory Pattern
```java
public class VehicleFactory {
    public static Vehicle createVehicle(String licensePlate, VehicleType type) {
        return new Vehicle(licensePlate, type);
    }
}
```

**Purpose**: Centralized vehicle creation

### 3. Strategy Pattern (Fee Calculation)
```java
public interface FeeStrategy {
    double calculateFee(long durationMinutes, VehicleType vehicleType);
}
```

**Purpose**: Allow different fee calculation strategies

### 4. Observer Pattern (for Future Enhancement)
```java
public interface ParkingLotObserver {
    void spotAvailable(ParkingSpot spot);
    void spotOccupied(ParkingSpot spot);
}
```

**Purpose**: Real-time notifications on availability changes

## Performance Analysis

### Check-In Operation
```
Time Complexity: O(f × s)
  f = number of floors
  s = spots per floor
Space Complexity: O(1)

Bottleneck: Linear search through spots
```

### Check-Out Operation
```
Time Complexity: O(1)
Space Complexity: O(1)

Note: O(1) for direct spot vacating
```

### Fee Calculation
```
Time Complexity: O(1)
Space Complexity: O(1)
```

### Overall System
```
Capacity: Can handle thousands of spots
Concurrent Operations: Safe up to available system threads
Latency: Sub-millisecond for typical operations
```

## Scalability Considerations

### Current Limitations
1. In-memory storage only
2. Single-threaded bottleneck at manager level
3. Linear search for spot allocation

### Future Improvements
1. **Database Backend**: Persistent storage
2. **Caching**: Redis for availability cache
3. **Indexing**: B-tree indexes for faster spot search
4. **Read-Write Locks**: Separate read-heavy operations
5. **Message Queue**: Async operations via Kafka/RabbitMQ
6. **Microservices**: Separate fee calculation service

## Security Considerations

1. **Input Validation**: Vehicle plate format validation
2. **Access Control**: Permission checks for operations
3. **Audit Logging**: Track all transactions
4. **Data Encryption**: Sensitive data protection
5. **Rate Limiting**: Prevent DoS attacks

---