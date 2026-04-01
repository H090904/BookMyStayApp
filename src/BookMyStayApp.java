import java.util.*;

/**
 * Use Case 10: Booking Cancellation & Inventory Rollback
 *
 * @version 10.0
 */
class Reservation {
    private String guestName;
    private String roomType;
    private String roomId;

    public Reservation(String guestName, String roomType, String roomId) {
        this.guestName = guestName;
        this.roomType = roomType;
        this.roomId = roomId;
    }

    public String getGuestName() {
        return guestName;
    }

    public String getRoomType() {
        return roomType;
    }

    public String getRoomId() {
        return roomId;
    }

    @Override
    public String toString() {
        return guestName + " - " + roomType + " (" + roomId + ")";
    }
}

/**
 * RoomInventory manages room counts
 */
class RoomInventory {
    private Map<String, Integer> inventory = new HashMap<>();

    public RoomInventory() {
        inventory.put("Single Room", 2);
        inventory.put("Double Room", 1);
        inventory.put("Suite Room", 1);
    }

    public boolean isValidRoomType(String roomType) {
        return inventory.containsKey(roomType);
    }

    public int getAvailability(String roomType) {
        return inventory.getOrDefault(roomType, -1);
    }

    public void incrementRoom(String roomType) {
        inventory.put(roomType, inventory.getOrDefault(roomType, 0) + 1);
    }

    public void decrementRoom(String roomType) {
        inventory.put(roomType, inventory.getOrDefault(roomType, 0) - 1);
    }

    public void displayInventory() {
        System.out.println("\n---- Current Inventory ----");
        inventory.forEach((type, count) -> System.out.println(type + " : " + count));
    }
}

/**
 * BookingService manages confirmed bookings
 */
class BookingService {

    private RoomInventory inventory;
    private Map<String, Reservation> activeBookings = new HashMap<>();
    private Stack<String> cancelledRoomIds = new Stack<>();
    private int roomCounter = 100; // for unique room IDs

    public BookingService(RoomInventory inventory) {
        this.inventory = inventory;
    }

    // Confirm a booking
    public Reservation bookRoom(String guestName, String roomType) {
        if (!inventory.isValidRoomType(roomType) || inventory.getAvailability(roomType) <= 0) {
            System.out.println("Booking failed: Room not available or invalid type.");
            return null;
        }

        // Generate unique room ID
        String roomId = roomType.substring(0, 2).toUpperCase() + roomCounter++;
        Reservation reservation = new Reservation(guestName, roomType, roomId);

        // Update inventory and active bookings
        inventory.decrementRoom(roomType);
        activeBookings.put(roomId, reservation);

        System.out.println("Booking Confirmed: " + reservation);
        return reservation;
    }

    // Cancel a booking
    public void cancelBooking(String roomId) {
        if (!activeBookings.containsKey(roomId)) {
            System.out.println("Cancellation failed: No active booking with Room ID " + roomId);
            return;
        }

        // Remove booking and rollback inventory
        Reservation reservation = activeBookings.remove(roomId);
        inventory.incrementRoom(reservation.getRoomType());
        cancelledRoomIds.push(roomId);

        System.out.println("Booking Cancelled: " + reservation);
    }

    // Display active bookings
    public void displayActiveBookings() {
        System.out.println("\n---- Active Bookings ----");
        if (activeBookings.isEmpty()) {
            System.out.println("No active bookings.");
        } else {
            activeBookings.values().forEach(System.out::println);
        }
    }

    // Display cancelled bookings
    public void displayCancelledBookings() {
        System.out.println("\n---- Cancelled Bookings (Rollback Stack) ----");
        if (cancelledRoomIds.isEmpty()) {
            System.out.println("No cancellations yet.");
        } else {
            cancelledRoomIds.forEach(id -> System.out.println("Room ID: " + id));
        }
    }
}

/**
 * Main Class: UseCase10BookingCancellation
 */
public class BookMyStayApp {

    public static void main(String[] args) {

        System.out.println("=======================================");
        System.out.println("     Welcome to Book My Stay App       ");
        System.out.println("     Hotel Booking System v10.0        ");
        System.out.println("=======================================\n");

        RoomInventory inventory = new RoomInventory();
        BookingService bookingService = new BookingService(inventory);

        // Book some rooms
        Reservation r1 = bookingService.bookRoom("Alice", "Single Room");
        Reservation r2 = bookingService.bookRoom("Bob", "Double Room");
        Reservation r3 = bookingService.bookRoom("Charlie", "Suite Room");

        // Display current bookings and inventory
        bookingService.displayActiveBookings();
        inventory.displayInventory();

        // Perform cancellations
        if (r2 != null) bookingService.cancelBooking(r2.getRoomId());
        bookingService.cancelBooking("INVALID_ID"); // invalid test
        if (r1 != null) bookingService.cancelBooking(r1.getRoomId());

        // Display final active and cancelled bookings
        bookingService.displayActiveBookings();
        bookingService.displayCancelledBookings();
        inventory.displayInventory();

        System.out.println("\nSystem state restored successfully after cancellations.");
    }
}