/**
 * Use Case 6: Reservation Confirmation & Room Allocation
 * Book My Stay App
 * Version: 6.1
 */

import java.util.*;

// Reservation request
class Reservation {
    String guestName;
    String roomType;

    public Reservation(String guestName, String roomType) {
        this.guestName = guestName;
        this.roomType = roomType;
    }
}

// Inventory Service
class InventoryService {

    private HashMap<String, Integer> inventory;

    public InventoryService() {
        inventory = new HashMap<>();

        inventory.put("Single Room", 2);
        inventory.put("Double Room", 2);
        inventory.put("Suite Room", 1);
    }

    public int getAvailability(String roomType) {
        return inventory.getOrDefault(roomType, 0);
    }

    public void decrementRoom(String roomType) {
        inventory.put(roomType, inventory.get(roomType) - 1);
    }

    public void displayInventory() {
        System.out.println("\nCurrent Inventory:");
        for (String room : inventory.keySet()) {
            System.out.println(room + " : " + inventory.get(room));
        }
    }
}

// Booking Service
class BookingService {

    private InventoryService inventory;
    private HashMap<String, Set<String>> allocatedRooms;

    public BookingService(InventoryService inventory) {
        this.inventory = inventory;
        allocatedRooms = new HashMap<>();
    }

    public void allocateRoom(Reservation reservation) {

        String roomType = reservation.roomType;

        if (inventory.getAvailability(roomType) <= 0) {
            System.out.println("No rooms available for " + roomType);
            return;
        }

        // generate unique room ID
        String roomId = roomType.replace(" ", "") + "_" + UUID.randomUUID().toString().substring(0, 5);

        // ensure set exists
        allocatedRooms.putIfAbsent(roomType, new HashSet<>());

        Set<String> roomSet = allocatedRooms.get(roomType);

        if (!roomSet.contains(roomId)) {

            roomSet.add(roomId);

            // update inventory
            inventory.decrementRoom(roomType);

            System.out.println("Reservation Confirmed");
            System.out.println("Guest: " + reservation.guestName);
            System.out.println("Room Type: " + roomType);
            System.out.println("Allocated Room ID: " + roomId);
            System.out.println("----------------------------------");
        }
    }
}

public class BookMyStayApp {

    public static void main(String[] args) {

        System.out.println("=================================");
        System.out.println("Book My Stay - Hotel Booking System v6.1");
        System.out.println("Room Allocation Service");
        System.out.println("=================================");

        // Request Queue (FIFO)
        Queue<Reservation> bookingQueue = new LinkedList<>();

        bookingQueue.add(new Reservation("Alice", "Single Room"));
        bookingQueue.add(new Reservation("Bob", "Double Room"));
        bookingQueue.add(new Reservation("Charlie", "Single Room"));

        InventoryService inventory = new InventoryService();
        BookingService bookingService = new BookingService(inventory);

        // Process queue
        while (!bookingQueue.isEmpty()) {

            Reservation request = bookingQueue.poll();

            bookingService.allocateRoom(request);
        }

        inventory.displayInventory();
    }
}