/**
 * Use Case 4: Room Search & Availability Check
 * Book My Stay App
 * Version: 4.1
 */

import java.util.HashMap;

// Room domain class
class Room {

    private String roomType;
    private int beds;
    private double price;

    public Room(String roomType, int beds, double price) {
        this.roomType = roomType;
        this.beds = beds;
        this.price = price;
    }

    public String getRoomType() {
        return roomType;
    }

    public void displayRoomDetails() {
        System.out.println("Room Type : " + roomType);
        System.out.println("Beds      : " + beds);
        System.out.println("Price     : $" + price);
    }
}

// Inventory class (state holder)
class RoomInventory {

    private HashMap<String, Integer> inventory;

    public RoomInventory() {
        inventory = new HashMap<>();

        inventory.put("Single Room", 5);
        inventory.put("Double Room", 0);
        inventory.put("Suite Room", 2);
    }

    public int getAvailability(String roomType) {
        return inventory.getOrDefault(roomType, 0);
    }
}

public class BookMyStayApp {

    public static void main(String[] args) {

        System.out.println("=================================");
        System.out.println("Book My Stay - Hotel Booking System v4.1");
        System.out.println("Room Search & Availability Check");
        System.out.println("=================================");

        // Create room objects
        Room single = new Room("Single Room", 1, 100);
        Room doubleRoom = new Room("Double Room", 2, 180);
        Room suite = new Room("Suite Room", 3, 350);

        // Inventory
        RoomInventory inventory = new RoomInventory();

        // Search logic (read-only)
        Room[] rooms = {single, doubleRoom, suite};

        System.out.println("\nAvailable Rooms:");

        for (Room room : rooms) {

            int available = inventory.getAvailability(room.getRoomType());

            if (available > 0) {

                room.displayRoomDetails();
                System.out.println("Available Rooms: " + available);
                System.out.println("------------------------------");
            }
        }

        System.out.println("Search completed. Inventory unchanged.");
    }
}