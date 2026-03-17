import java.util.*;

/**
 * ==========================================================
 * Use Case 6: Room Allocation & Reservation Confirmation
 * ==========================================================
 */

// Reservation (same as UC5)
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

    private HashMap<String, Integer> inventory = new HashMap<>();

    public void addRoomType(String type, int count) {
        inventory.put(type, count);
    }

    public boolean isAvailable(String type) {
        return inventory.getOrDefault(type, 0) > 0;
    }

    public void decrement(String type) {
        inventory.put(type, inventory.get(type) - 1);
    }

    public void displayInventory() {
        System.out.println("Current Inventory: " + inventory);
    }
}

// Booking Service
class BookingService {

    private Queue<Reservation> queue;
    private InventoryService inventoryService;

    // Track all allocated rooms
    private Set<String> allocatedRoomIds = new HashSet<>();

    // Track rooms per type
    private HashMap<String, Set<String>> roomTypeMap = new HashMap<>();

    // Room ID counters
    private HashMap<String, Integer> counters = new HashMap<>();

    public BookingService(Queue<Reservation> queue, InventoryService inventoryService) {
        this.queue = queue;
        this.inventoryService = inventoryService;
    }

    // Generate unique room ID
    private String generateRoomId(String type) {

        int count = counters.getOrDefault(type, 0) + 1;
        counters.put(type, count);

        return type.charAt(0) + String.valueOf(count);
    }

    public void processBookings() {

        while (!queue.isEmpty()) {

            Reservation r = queue.poll();

            String type = r.getRoomType();

            System.out.println("\nProcessing: " + r.getGuestName() + " → " + type);

            if (inventoryService.isAvailable(type)) {

                String roomId = generateRoomId(type);

                // Ensure uniqueness (extra safety)
                if (allocatedRoomIds.contains(roomId)) {
                    System.out.println("Duplicate room ID detected! Skipping...");
                    continue;
                }

                // ATOMIC LOGIC START
                allocatedRoomIds.add(roomId);

                roomTypeMap.putIfAbsent(type, new HashSet<>());
                roomTypeMap.get(type).add(roomId);

                inventoryService.decrement(type);
                // ATOMIC LOGIC END

                System.out.println("Booking Confirmed!");
                System.out.println("Assigned Room ID: " + roomId);

            } else {
                System.out.println("No rooms available for " + type);
            }
        }
    }

    public void displayAllocations() {

        System.out.println("\nRoom Allocations:");

        for (String type : roomTypeMap.keySet()) {
            System.out.println(type + " → " + roomTypeMap.get(type));
        }
    }
}

// Main Class
public class BookMyStayApp {

    public static void main(String[] args) {

        System.out.println("=====================================");
        System.out.println("UC6 - Room Allocation Service");
        System.out.println("=====================================");

        // Step 1: Create Queue (from UC5)
        Queue<Reservation> queue = new LinkedList<>();

        queue.add(new Reservation("Alice", "Single"));
        queue.add(new Reservation("Bob", "Single"));
        queue.add(new Reservation("Charlie", "Double"));
        queue.add(new Reservation("David", "Single"));

        // Step 2: Inventory Setup
        InventoryService inventory = new InventoryService();
        inventory.addRoomType("Single", 2);
        inventory.addRoomType("Double", 1);

        // Step 3: Booking Service
        BookingService bookingService =
                new BookingService(queue, inventory);

        // Step 4: Process bookings
        bookingService.processBookings();

        // Step 5: Show results
        bookingService.displayAllocations();
        inventory.displayInventory();
    }
}