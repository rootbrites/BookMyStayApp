import java.io.*;
import java.util.*;

/**
 * ==========================================================
 * Use Case 12: Data Persistence & System Recovery
 * ==========================================================
 */

// Reservation (Serializable)
class Reservation implements Serializable {
    private static final long serialVersionUID = 1L;

    private String reservationId;
    private String guestName;
    private String roomType;

    public Reservation(String id, String guest, String roomType) {
        this.reservationId = id;
        this.guestName = guest;
        this.roomType = roomType;
    }

    public String getReservationId() {
        return reservationId;
    }

    @Override
    public String toString() {
        return reservationId + " - " + guestName + " (" + roomType + ")";
    }
}

// System State Wrapper
class SystemState implements Serializable {
    private static final long serialVersionUID = 1L;

    Map<String, Integer> inventory;
    List<Reservation> bookings;

    public SystemState(Map<String, Integer> inventory, List<Reservation> bookings) {
        this.inventory = inventory;
        this.bookings = bookings;
    }
}

// Persistence Service
class PersistenceService {

    private static final String FILE_NAME = "system_state.ser";

    // Save state
    public static void save(SystemState state) {
        try (ObjectOutputStream oos =
                     new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {

            oos.writeObject(state);
            System.out.println("System state saved successfully.");

        } catch (IOException e) {
            System.out.println("Error saving state: " + e.getMessage());
        }
    }

    // Load state
    public static SystemState load() {
        try (ObjectInputStream ois =
                     new ObjectInputStream(new FileInputStream(FILE_NAME))) {

            SystemState state = (SystemState) ois.readObject();
            System.out.println("System state loaded successfully.");
            return state;

        } catch (FileNotFoundException e) {
            System.out.println("No previous state found. Starting fresh.");
        } catch (Exception e) {
            System.out.println("Error loading state: " + e.getMessage());
        }

        return null;
    }
}

// Main
public class BookMyStayApp {

    public static void main(String[] args) {

        // Step 1: Try loading existing state
        SystemState state = PersistenceService.load();

        Map<String, Integer> inventory;
        List<Reservation> bookings;

        if (state != null) {
            inventory = state.inventory;
            bookings = state.bookings;
        } else {
            // Fresh start
            inventory = new HashMap<>();
            bookings = new ArrayList<>();

            inventory.put("Single", 2);
            inventory.put("Double", 1);

            bookings.add(new Reservation("R101", "Alice", "Single"));
        }

        // Display current state
        System.out.println("\n--- CURRENT STATE ---");
        System.out.println("Inventory: " + inventory);
        System.out.println("Bookings: " + bookings);

        // Step 2: Simulate change
        bookings.add(new Reservation("R102", "Bob", "Double"));
        inventory.put("Double", inventory.get("Double") - 1);

        // Step 3: Save before shutdown
        PersistenceService.save(new SystemState(inventory, bookings));
    }
}