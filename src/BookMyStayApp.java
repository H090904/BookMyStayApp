import java.util.*;

/**
 * Reservation
 * Represents a confirmed booking
 *
 * @version 8.0
 */
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

    public void display() {
        System.out.println("Reservation ID: " + reservationId +
                " | Guest: " + guestName +
                " | Room: " + roomType);
    }
}


/**
 * BookingHistory
 * Stores confirmed bookings in order
 *
 * @version 8.0
 */
class BookingHistory {

    private List<Reservation> history = new ArrayList<>();

    // Add confirmed booking
    public void addReservation(Reservation r) {
        history.add(r);
        System.out.println("Added to history: " + r.getReservationId());
    }

    // Get all bookings (read-only usage)
    public List<Reservation> getAllReservations() {
        return history;
    }

    // Display all bookings
    public void displayAll() {
        System.out.println("\n---- Booking History ----");

        if (history.isEmpty()) {
            System.out.println("No bookings found.");
            return;
        }

        for (Reservation r : history) {
            r.display();
        }
    }
}


/**
 * BookingReportService
 * Generates reports from booking history
 *
 * @version 8.0
 */
class BookingReportService {

    private BookingHistory history;

    public BookingReportService(BookingHistory history) {
        this.history = history;
    }

    // Generate summary report
    public void generateSummaryReport() {

        List<Reservation> reservations = history.getAllReservations();

        System.out.println("\n---- Booking Summary Report ----");

        if (reservations.isEmpty()) {
            System.out.println("No data available.");
            return;
        }

        System.out.println("Total Bookings: " + reservations.size());

        // Count bookings per room type
        Map<String, Integer> roomCount = new HashMap<>();

        for (Reservation r : reservations) {
            roomCount.put(r.getRoomType(),
                    roomCount.getOrDefault(r.getRoomType(), 0) + 1);
        }

        System.out.println("\nBookings by Room Type:");
        for (String type : roomCount.keySet()) {
            System.out.println(type + " : " + roomCount.get(type));
        }
    }
}


/**
 * UseCase8BookingHistoryReport
 * Entry point
 *
 * @version 8.1
 */
public class BookMyStayApp {

    public static void main(String[] args) {

        System.out.println("=======================================");
        System.out.println("     Welcome to Book My Stay App       ");
        System.out.println("     Hotel Booking System v8.1         ");
        System.out.println("=======================================\n");

        // Initialize booking history
        BookingHistory history = new BookingHistory();

        // Simulate confirmed bookings (from Use Case 6)
        Reservation r1 = new Reservation("SR-101", "Alice", "Single Room");
        Reservation r2 = new Reservation("DR-202", "Bob", "Double Room");
        Reservation r3 = new Reservation("SR-102", "Charlie", "Single Room");

        // Add to history
        history.addReservation(r1);
        history.addReservation(r2);
        history.addReservation(r3);

        // Display history
        history.displayAll();

        // Generate report
        BookingReportService reportService = new BookingReportService(history);
        reportService.generateSummaryReport();

        System.out.println("\nReporting completed. No data modified.");
    }
}