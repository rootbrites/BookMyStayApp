import java.util.*;

/**
 * ==========================================================
 * Use Case 10: Booking Cancellation & Inventory Rollback
 * ==========================================================
 */

// Custom Exception
class CancellationException extends Exception {
    public CancellationException(String message) {
        super(message);
    }
}

// Reservation
class Reservation {
    private String reservationId;
    private String guestName;
    private String roomType;
    private boolean isActive;

    public Reservation(String reservationId, String guestName, String roomType) {
        this.reservationId = reservationId;
        this.guestName = guestName;
        this.roomType = roomType;
        this.isActive = true;
    }

    public String getReservationId() {
        return reservationId;
    }

    public String getRoomType() {
        return roomType;
    }

    public boolean isActive() {
        return isActive;
    }

    public void cancel() {
        this.isActive = false;
    }

    @Override
    public String toString() {
        return reservationId + " - " + guestName + " (" + roomType + ")";
    }
}

// Inventory Service
class InventoryService {
    private Map<String, Integer> inventory = new HashMap<>();

    public void addRoom(String type, int count) {
        inventory.put(type, count);
    }

    public void bookRoom(String type) {
        inventory.put(type, inventory.get(type) - 1);
    }

    public void releaseRoom(String type) {
        inventory.put(type, inventory.get(type) + 1);
    }

    public void displayInventory() {
        System.out.println("Current Inventory: " + inventory);
    }
}

// Booking History
class BookingHistory {
    private Map<String, Reservation> reservations = new HashMap<>();

    public void addReservation(Reservation r) {
        reservations.put(r.getReservationId(), r);
    }

    public Reservation getReservation(String id) {
        return reservations.get(id);
    }
}

// Cancellation Service
class CancellationService {

    private InventoryService inventory;
    private BookingHistory history;

    // Stack for rollback tracking
    private Stack<String> rollbackStack = new Stack<>();

    public CancellationService(InventoryService inventory, BookingHistory history) {
        this.inventory = inventory;
        this.history = history;
    }

    public void cancelBooking(String reservationId) {

        try {
            Reservation r = history.getReservation(reservationId);

            // Step 1: Validate
            if (r == null) {
                throw new CancellationException("Reservation does not exist");
            }

            if (!r.isActive()) {
                throw new CancellationException("Reservation already cancelled");
            }

            // Step 2: Push to rollback stack
            rollbackStack.push(reservationId);

            // Step 3: Restore inventory
            inventory.releaseRoom(r.getRoomType());

            // Step 4: Mark as cancelled
            r.cancel();

            System.out.println("Cancellation successful: " + reservationId);

        } catch (CancellationException e) {
            System.out.println("Cancellation failed: " + e.getMessage());
        }
    }

    public void showRollbackStack() {
        System.out.println("Rollback Stack: " + rollbackStack);
    }
}

// Main
public class BookMyStayApp {

    public static void main(String[] args) {

        InventoryService inventory = new InventoryService();
        inventory.addRoom("Single", 2);

        BookingHistory history = new BookingHistory();

        // Create bookings
        Reservation r1 = new Reservation("R101", "Alice", "Single");
        Reservation r2 = new Reservation("R102", "Bob", "Single");

        history.addReservation(r1);
        history.addReservation(r2);

        // Simulate booking (reduce inventory)
        inventory.bookRoom("Single");
        inventory.bookRoom("Single");

        inventory.displayInventory();

        CancellationService cancelService =
                new CancellationService(inventory, history);

        // Test cases
        cancelService.cancelBooking("R101"); // valid
        cancelService.cancelBooking("R101"); // duplicate cancel
        cancelService.cancelBooking("R999"); // invalid

        inventory.displayInventory();
        cancelService.showRollbackStack();
    }
}