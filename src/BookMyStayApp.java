import java.util.*;
import java.util.concurrent.*;

/**
 * Use Case 11: Concurrent Booking Simulation (Thread Safety)
 *
 * @version 11.0
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
 * RoomInventory manages room counts safely
 */
class RoomInventory {
    private Map<String, Integer> inventory = new HashMap<>();

    public RoomInventory() {
        inventory.put("Single Room", 2);
        inventory.put("Double Room", 1);
        inventory.put("Suite Room", 1);
    }

    public synchronized boolean isAvailable(String roomType) {
        return inventory.getOrDefault(roomType, 0) > 0;
    }

    public synchronized void decrement(String roomType) {
        inventory.put(roomType, inventory.get(roomType) - 1);
    }

    public synchronized void increment(String roomType) {
        inventory.put(roomType, inventory.getOrDefault(roomType, 0) + 1);
    }

    public synchronized void displayInventory() {
        System.out.println("\n---- Current Inventory ----");
        inventory.forEach((type, count) -> System.out.println(type + " : " + count));
    }
}

/**
 * BookingService handles thread-safe bookings
 */
class BookingService {

    private RoomInventory inventory;
    private Map<String, Reservation> activeBookings = new ConcurrentHashMap<>();
    private int roomCounter = 100; // unique room IDs
    private final Object lock = new Object(); // lock for critical section

    public BookingService(RoomInventory inventory) {
        this.inventory = inventory;
    }

    public Reservation bookRoom(String guestName, String roomType) {
        synchronized (lock) {
            if (!inventory.isAvailable(roomType)) {
                System.out.println("Booking failed for " + guestName + ": " + roomType + " not available.");
                return null;
            }

            String roomId = roomType.substring(0, 2).toUpperCase() + roomCounter++;
            Reservation reservation = new Reservation(guestName, roomType, roomId);

            inventory.decrement(roomType);
            activeBookings.put(roomId, reservation);

            System.out.println("Booking Confirmed: " + reservation);
            return reservation;
        }
    }

    public void displayActiveBookings() {
        System.out.println("\n---- Active Bookings ----");
        if (activeBookings.isEmpty()) {
            System.out.println("No active bookings.");
        } else {
            activeBookings.values().forEach(System.out::println);
        }
    }
}

/**
 * Main Class: UseCase11ConcurrentBookingSimulation
 */
public class BookMyStayApp {

    public static void main(String[] args) throws InterruptedException {

        System.out.println("=======================================");
        System.out.println("  Book My Stay App - Concurrent Demo   ");
        System.out.println("=======================================\n");

        RoomInventory inventory = new RoomInventory();
        BookingService bookingService = new BookingService(inventory);

        // Simulate multiple guests booking concurrently
        String[] guests = {"Alice", "Bob", "Charlie", "David", "Eve"};
        String[] roomTypes = {"Single Room", "Double Room", "Suite Room", "Single Room", "Double Room"};

        ExecutorService executor = Executors.newFixedThreadPool(guests.length);

        for (int i = 0; i < guests.length; i++) {
            final int index = i;
            executor.submit(() -> {
                bookingService.bookRoom(guests[index], roomTypes[index]);
            });
        }

        executor.shutdown();
        executor.awaitTermination(10, TimeUnit.SECONDS);

        // Display final state
        bookingService.displayActiveBookings();
        inventory.displayInventory();

        System.out.println("\nConcurrent booking simulation completed successfully.");
    }
}