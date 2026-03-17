import java.util.*;

/**
 * ==========================================================
 * Use Case 9: Error Handling & Validation
 * ==========================================================
 */

// Custom Exception
class InvalidBookingException extends Exception {

    public InvalidBookingException(String message) {
        super(message);
    }
}

// Reservation
class Reservation {

    private String guestName;
    private String roomType;

    public Reservation(String guestName, String roomType) {
        this.guestName = guestName;
        this.roomType = roomType;
    }

    public String getGuestName() {
        return guestName;
    }

    public String getRoomType() {
        return roomType;
    }
}

// Inventory Service
class InventoryService {

    private Map<String, Integer> inventory = new HashMap<>();

    public void addRoomType(String type, int count) {
        inventory.put(type, count);
    }

    public boolean hasRoomType(String type) {
        return inventory.containsKey(type);
    }

    public int getAvailability(String type) {
        return inventory.getOrDefault(type, 0);
    }

    public void decrement(String type) throws InvalidBookingException {

        int count = inventory.getOrDefault(type, 0);

        if (count <= 0) {
            throw new InvalidBookingException(
                    "Cannot decrement. No rooms available for " + type
            );
        }

        inventory.put(type, count - 1);
    }
}

// Validator
class BookingValidator {

    public static void validate(Reservation r, InventoryService inventory)
            throws InvalidBookingException {

        if (r.getGuestName() == null || r.getGuestName().isEmpty()) {
            throw new InvalidBookingException("Guest name cannot be empty");
        }

        if (r.getRoomType() == null || r.getRoomType().isEmpty()) {
            throw new InvalidBookingException("Room type cannot be empty");
        }

        if (!inventory.hasRoomType(r.getRoomType())) {
            throw new InvalidBookingException(
                    "Invalid room type: " + r.getRoomType()
            );
        }

        if (inventory.getAvailability(r.getRoomType()) <= 0) {
            throw new InvalidBookingException(
                    "No rooms available for " + r.getRoomType()
            );
        }
    }
}

// Booking Service
class BookingService {

    private InventoryService inventory;

    public BookingService(InventoryService inventory) {
        this.inventory = inventory;
    }

    public void processBooking(Reservation r) {

        try {
            // VALIDATION (fail-fast)
            BookingValidator.validate(r, inventory);

            // SAFE TO PROCEED
            inventory.decrement(r.getRoomType());

            System.out.println("Booking successful for "
                    + r.getGuestName() + " (" + r.getRoomType() + ")");

        } catch (InvalidBookingException e) {

            // GRACEFUL FAILURE
            System.out.println("Booking failed: " + e.getMessage());
        }
    }
}

// Main
public class BookMyStayApp {

    public static void main(String[] args) {

        System.out.println("=====================================");
        System.out.println("UC9 - Error Handling & Validation");
        System.out.println("=====================================");

        InventoryService inventory = new InventoryService();
        inventory.addRoomType("Single", 1);
        inventory.addRoomType("Double", 0);

        BookingService bookingService = new BookingService(inventory);

        // Test cases
        Reservation r1 = new Reservation("Alice", "Single");   // valid
        Reservation r2 = new Reservation("", "Single");        // invalid name
        Reservation r3 = new Reservation("Bob", "Triple");     // invalid type
        Reservation r4 = new Reservation("Charlie", "Double"); // no availability

        bookingService.processBooking(r1);
        bookingService.processBooking(r2);
        bookingService.processBooking(r3);
        bookingService.processBooking(r4);
    }
}