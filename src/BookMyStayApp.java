import java.util.HashMap;

/**
 * ==========================================================
 * Use Case 4: Room Search & Availability Check
 * BookMyStayApp v4.1
 * ==========================================================
 */

/* ---------- ROOM DOMAIN MODEL ---------- */

abstract class Room {

    protected String type;
    protected double price;

    public Room(String type, double price) {
        this.type = type;
        this.price = price;
    }

    public String getType() {
        return type;
    }

    public double getPrice() {
        return price;
    }

    public abstract void displayDetails();
}

class SingleRoom extends Room {

    public SingleRoom() {
        super("Single Room", 100);
    }

    public void displayDetails() {
        System.out.println("Single Room | Price: $" + price + " | 1 Bed");
    }
}

class DoubleRoom extends Room {

    public DoubleRoom() {
        super("Double Room", 150);
    }

    public void displayDetails() {
        System.out.println("Double Room | Price: $" + price + " | 2 Beds");
    }
}

class SuiteRoom extends Room {

    public SuiteRoom() {
        super("Suite Room", 300);
    }

    public void displayDetails() {
        System.out.println("Suite Room | Price: $" + price + " | Luxury Suite");
    }
}

/* ---------- INVENTORY ---------- */

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

/* ---------- SEARCH SERVICE ---------- */

class SearchService {

    private RoomInventory inventory;

    public SearchService(RoomInventory inventory) {
        this.inventory = inventory;
    }

    public void searchRooms(Room[] rooms) {

        System.out.println("\nAvailable Rooms:\n");

        for (Room room : rooms) {

            int available = inventory.getAvailability(room.getType());

            if (available > 0) {
                room.displayDetails();
                System.out.println("Available: " + available);
                System.out.println("-------------------------");
            }
        }
    }
}

/* ---------- APPLICATION ENTRY ---------- */

public class BookMyStayApp {

    public static void main(String[] args) {

        System.out.println("=====================================");
        System.out.println("Welcome to BookMyStayApp");
        System.out.println("Version 4.1 - Room Search");
        System.out.println("=====================================");

        RoomInventory inventory = new RoomInventory();

        Room[] rooms = {
                new SingleRoom(),
                new DoubleRoom(),
                new SuiteRoom()
        };

        SearchService searchService = new SearchService(inventory);

        searchService.searchRooms(rooms);
    }
}