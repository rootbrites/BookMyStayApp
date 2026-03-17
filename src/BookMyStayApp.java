import java.util.*;

/**
 * ==========================================================
 * Use Case 7: Add-On Service Selection
 * ==========================================================
 */

// Service class (Add-On)
class Service {

    private String name;
    private double cost;

    public Service(String name, double cost) {
        this.name = name;
        this.cost = cost;
    }

    public double getCost() {
        return cost;
    }

    public String getName() {
        return name;
    }

    public String toString() {
        return name + " (₹" + cost + ")";
    }
}

// Manager for Add-On Services
class AddOnServiceManager {

    // reservationId → list of services
    private Map<String, List<Service>> serviceMap = new HashMap<>();

    // Add service to a reservation
    public void addService(String reservationId, Service service) {

        serviceMap.putIfAbsent(reservationId, new ArrayList<>());
        serviceMap.get(reservationId).add(service);

        System.out.println("Added " + service.getName() +
                " to reservation " + reservationId);
    }

    // Calculate total add-on cost
    public double calculateTotalCost(String reservationId) {

        double total = 0;

        List<Service> services = serviceMap.get(reservationId);

        if (services != null) {
            for (Service s : services) {
                total += s.getCost();
            }
        }

        return total;
    }

    // Display services for reservation
    public void displayServices(String reservationId) {

        List<Service> services = serviceMap.get(reservationId);

        System.out.println("\nServices for Reservation " + reservationId + ":");

        if (services == null || services.isEmpty()) {
            System.out.println("No services selected.");
            return;
        }

        for (Service s : services) {
            System.out.println("- " + s);
        }

        System.out.println("Total Add-On Cost: ₹" +
                calculateTotalCost(reservationId));
    }
}

// Main class
public class BookMyStayApp {

    public static void main(String[] args) {

        System.out.println("=====================================");
        System.out.println("UC7 - Add-On Service Selection");
        System.out.println("=====================================");

        // Assume reservation IDs from UC6
        String res1 = "S1";
        String res2 = "D1";

        // Create services
        Service breakfast = new Service("Breakfast", 500);
        Service wifi = new Service("WiFi", 200);
        Service spa = new Service("Spa", 1500);

        // Manager
        AddOnServiceManager manager = new AddOnServiceManager();

        // Add services
        manager.addService(res1, breakfast);
        manager.addService(res1, wifi);

        manager.addService(res2, spa);

        // Display
        manager.displayServices(res1);
        manager.displayServices(res2);
    }
}