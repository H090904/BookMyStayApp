import java.util.LinkedList;
import java.util.Queue;

/**
 * Reservation
 *
 * Represents a guest's booking request.
 *
 * @author YourName
 * @version 5.0
 */
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
        System.out.println("Guest: " + guestName + " | Requested Room: " + roomType);
    }
}


/**
 * BookingRequestQueue
 *
 * Manages booking requests using FIFO principle.
 *
 * @version 5.0
 */
class BookingRequestQueue {

    private Queue<Reservation> requestQueue;

    public BookingRequestQueue() {
        requestQueue = new LinkedList<>();
    }

    // Add booking request (enqueue)
    public void addRequest(Reservation reservation) {
        requestQueue.offer(reservation);
        System.out.println("Booking request added for " + reservation.getGuestName());
    }

    // View all requests (without removing)
    public void viewAllRequests() {
        System.out.println("\n---- Booking Request Queue ----");

        if (requestQueue.isEmpty()) {
            System.out.println("No booking requests in queue.");
            return;
        }

        for (Reservation r : requestQueue) {
            r.displayReservation();
        }
    }

    // Get next request (peek - no removal)
    public Reservation getNextRequest() {
        return requestQueue.peek();
    }
}


/**
 * UseCase5BookingRequestQueue
 *
 * Entry point demonstrating FIFO booking request handling.
 *
 * @version 5.1
 */
public class BookMyStayApp {

    public static void main(String[] args) {

        System.out.println("=======================================");
        System.out.println("     Welcome to Book My Stay App       ");
        System.out.println("     Hotel Booking System v5.1         ");
        System.out.println("=======================================\n");

        // Initialize booking queue
        BookingRequestQueue bookingQueue = new BookingRequestQueue();

        // Simulate guest booking requests
        Reservation r1 = new Reservation("Alice", "Single Room");
        Reservation r2 = new Reservation("Bob", "Double Room");
        Reservation r3 = new Reservation("Charlie", "Suite Room");

        // Add requests (FIFO order)
        bookingQueue.addRequest(r1);
        bookingQueue.addRequest(r2);
        bookingQueue.addRequest(r3);

        // View all queued requests
        bookingQueue.viewAllRequests();

        // Show next request (without removing)
        System.out.println("\nNext request to process:");
        Reservation next = bookingQueue.getNextRequest();
        if (next != null) {
            next.displayReservation();
        }

        System.out.println("\nAll requests are stored in arrival order (FIFO).");
        System.out.println("No inventory changes made at this stage.");
    }
}