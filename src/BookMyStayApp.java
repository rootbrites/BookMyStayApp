import java.util.*;

/**
 * ==========================================================
 * Use Case 11: Concurrent Booking Simulation
 * ==========================================================
 */

// Booking Request
class BookingRequest {
    String guestName;
    String roomType;

    public BookingRequest(String guestName, String roomType) {
        this.guestName = guestName;
        this.roomType = roomType;
    }
}

// Thread-Safe Inventory
class InventoryService {

    private Map<String, Integer> inventory = new HashMap<>();

    public InventoryService() {
        inventory.put("Single", 1); // Only 1 room → forces race condition
    }

    // Critical Section
    public synchronized boolean bookRoom(String type, String guest) {

        int available = inventory.getOrDefault(type, 0);

        if (available > 0) {
            System.out.println(guest + " is booking...");

            // Simulate delay (forces thread collision)
            try { Thread.sleep(100); } catch (InterruptedException e) {}

            inventory.put(type, available - 1);

            System.out.println("Booking SUCCESS for " + guest);
            return true;
        } else {
            System.out.println("Booking FAILED for " + guest);
            return false;
        }
    }

    public void displayInventory() {
        System.out.println("Final Inventory: " + inventory);
    }
}

// Shared Queue
class BookingQueue {

    private Queue<BookingRequest> queue = new LinkedList<>();

    public synchronized void addRequest(BookingRequest req) {
        queue.add(req);
    }

    public synchronized BookingRequest getRequest() {
        return queue.poll();
    }
}

// Worker Thread
class BookingProcessor extends Thread {

    private BookingQueue queue;
    private InventoryService inventory;

    public BookingProcessor(BookingQueue queue, InventoryService inventory) {
        this.queue = queue;
        this.inventory = inventory;
    }

    public void run() {
        while (true) {

            BookingRequest req;

            // Get request safely
            synchronized (queue) {
                req = queue.getRequest();
            }

            if (req == null) break;

            // Process booking
            inventory.bookRoom(req.roomType, req.guestName);
        }
    }
}

// Main
public class BookMyStayApp {

    public static void main(String[] args) {

        InventoryService inventory = new InventoryService();
        BookingQueue queue = new BookingQueue();

        // Simulate multiple users
        queue.addRequest(new BookingRequest("Alice", "Single"));
        queue.addRequest(new BookingRequest("Bob", "Single"));

        // Two threads competing
        Thread t1 = new BookingProcessor(queue, inventory);
        Thread t2 = new BookingProcessor(queue, inventory);

        t1.start();
        t2.start();

        try {
            t1.join();
            t2.join();
        } catch (InterruptedException e) {}

        inventory.displayInventory();
    }
}
