import java.util.*;

/**
 * ==========================================================
 * Use Case 8: Booking History & Reporting
 * ==========================================================
 */

// Reservation class (simplified)
class Reservation {

    private String reservationId;
    private String guestName;
    private String roomType;

    public Reservation(String reservationId, String guestName, String roomType) {
        this.reservationId = reservationId;
        this.guestName = guestName;
        this.roomType = roomType;
    }

    public String getReservationId() {
        return reservationId;
    }

    public String getGuestName() {
        return guestName;
    }

    public String getRoomType() {
        return roomType;
    }

    public String toString() {
        return reservationId + " | " + guestName + " | " + roomType;
    }
}

// Booking History (data storage)
class BookingHistory {

    private List<Reservation> history = new ArrayList<>();

    // Add confirmed booking
    public void addReservation(Reservation reservation) {
        history.add(reservation);
    }

    // Read-only access
    public List<Reservation> getAllReservations() {
        return Collections.unmodifiableList(history);
    }
}

// Report Service
class BookingReportService {

    // Display all bookings
    public void displayAllBookings(List<Reservation> reservations) {

        System.out.println("\n--- Booking History ---");

        for (Reservation r : reservations) {
            System.out.println(r);
        }
    }

    // Summary report
    public void generateSummary(List<Reservation> reservations) {

        Map<String, Integer> roomCount = new HashMap<>();

        for (Reservation r : reservations) {
            roomCount.put(
                    r.getRoomType(),
                    roomCount.getOrDefault(r.getRoomType(), 0) + 1
            );
        }

        System.out.println("\n--- Booking Summary ---");

        for (String type : roomCount.keySet()) {
            System.out.println(type + " Rooms Booked: " + roomCount.get(type));
        }
    }
}

// Main class
public class BookMyStayApp {

    public static void main(String[] args) {

        System.out.println("=====================================");
        System.out.println("UC8 - Booking History & Reporting");
        System.out.println("=====================================");

        // Simulated confirmed bookings (from UC6)
        Reservation r1 = new Reservation("S1", "Alice", "Single");
        Reservation r2 = new Reservation("S2", "Bob", "Single");
        Reservation r3 = new Reservation("D1", "Charlie", "Double");

        // Step 1: Store history
        BookingHistory history = new BookingHistory();

        history.addReservation(r1);
        history.addReservation(r2);
        history.addReservation(r3);

        // Step 2: Reporting
        BookingReportService reportService = new BookingReportService();

        List<Reservation> allBookings = history.getAllReservations();

        reportService.displayAllBookings(allBookings);
        reportService.generateSummary(allBookings);
    }
}