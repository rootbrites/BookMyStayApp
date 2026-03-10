/**
 * Use Case 5: Booking Request Queue (First-Come-First-Served)
 * Book My Stay App
 * Version: 5.1
 */

import java.util.LinkedList;
import java.util.Queue;

// Reservation class representing a guest booking request
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

    public void displayReservation() {
        System.out.println("Guest Name : " + guestName);
        System.out.println("Room Type  : " + roomType);
        System.out.println("---------------------------");
    }
}

    public class BookMyStayApp {

    public static void main(String[] args) {

        System.out.println("=================================");
        System.out.println("Book My Stay - Hotel Booking System v5.1");
        System.out.println("Booking Request Queue (FIFO)");
        System.out.println("=================================");

        // Queue to store booking requests
        Queue<Reservation> bookingQueue = new LinkedList<>();

        // Guest booking requests
        Reservation r1 = new Reservation("Alice", "Single Room");
        Reservation r2 = new Reservation("Bob", "Double Room");
        Reservation r3 = new Reservation("Charlie", "Suite Room");

        // Add requests to queue
        bookingQueue.add(r1);
        bookingQueue.add(r2);
        bookingQueue.add(r3);

        System.out.println("\nBooking Requests Received:");

        for (Reservation r : bookingQueue) {
            r.displayReservation();
        }

        System.out.println("Requests stored in FIFO order.");
        System.out.println("Waiting for allocation processing...");
    }
}