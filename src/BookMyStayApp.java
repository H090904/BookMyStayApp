import java.io.*;
import java.util.*;

/**
 * Use Case 12: Data Persistence & System Recovery
 *
 * @version 12.0
 */
class Reservation implements Serializable {
    private static final long serialVersionUID = 1L;
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

class RoomInventory implements Serializable {
    private static final long serialVersionUID = 1L;
    private Map<String, Integer> inventory = new HashMap<>();

    public RoomInventory() {
        inventory.put("Single Room", 2);
        inventory.put("Double Room", 1);
        inventory.put("Suite Room", 1);
    }

    public boolean isAvailable(String roomType) {
        return inventory.getOrDefault(roomType, 0) > 0;
    }

    public void decrement(String roomType) {
        inventory.put(roomType, inventory.get(roomType) - 1);
    }

    public void increment(String roomType) {
        inventory.put(roomType, inventory.getOrDefault(roomType, 0) + 1);
    }

    public void displayInventory() {
        System.out.println("\n---- Current Inventory ----");
        inventory.forEach((type, count) -> System.out.println(type + " : " + count));
    }
}

class BookingService implements Serializable {
    private static final long serialVersionUID = 1L;
    private RoomInventory inventory;
    private List<Reservation> activeBookings = new ArrayList<>();
    private int roomCounter = 100;

    public BookingService(RoomInventory inventory) {
        this.inventory = inventory;
    }

    public Reservation bookRoom(String guestName, String roomType) {
        if (!inventory.isAvailable(roomType)) {
            System.out.println("Booking failed for " + guestName + ": " + roomType + " not available.");
            return null;
        }

        String roomId = roomType.substring(0, 2).toUpperCase() + roomCounter++;
        Reservation reservation = new Reservation(guestName, roomType, roomId);
        inventory.decrement(roomType);
        activeBookings.add(reservation);

        System.out.println("Booking Confirmed: " + reservation);
        return reservation;
    }

    public void displayActiveBookings() {
        System.out.println("\n---- Active Bookings ----");
        if (activeBookings.isEmpty()) {
            System.out.println("No active bookings.");
        } else {
            activeBookings.forEach(System.out::println);
        }
    }

    public List<Reservation> getActiveBookings() {
        return activeBookings;
    }
}

/**
 * Persistence Service handles saving and loading state
 */
class PersistenceService {
    private static final String FILE_NAME = "hotel_state.ser";

    public static void saveState(RoomInventory inventory, BookingService bookingService) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {
            oos.writeObject(inventory);
            oos.writeObject(bookingService);
            System.out.println("\nSystem state saved successfully to " + FILE_NAME);
        } catch (IOException e) {
            System.out.println("Error saving system state: " + e.getMessage());
        }
    }

    public static Object[] loadState() {
        File file = new File(FILE_NAME);
        if (!file.exists()) {
            System.out.println("No saved state found. Starting fresh.");
            return null;
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILE_NAME))) {
            RoomInventory inventory = (RoomInventory) ois.readObject();
            BookingService bookingService = (BookingService) ois.readObject();
            System.out.println("\nSystem state loaded successfully from " + FILE_NAME);
            return new Object[]{inventory, bookingService};
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error loading system state: " + e.getMessage());
            return null;
        }
    }
}

/**
 * Main Class: UseCase12DataPersistenceRecovery
 */
public class BookMyStayApp {

    public static void main(String[] args) {
        System.out.println("=======================================");
        System.out.println(" Book My Stay App - Persistence Demo ");
        System.out.println("=======================================\n");

        // Load previous state if available
        Object[] loadedState = PersistenceService.loadState();
        RoomInventory inventory;
        BookingService bookingService;

        if (loadedState != null) {
            inventory = (RoomInventory) loadedState[0];
            bookingService = (BookingService) loadedState[1];
        } else {
            inventory = new RoomInventory();
            bookingService = new BookingService(inventory);
        }

        // Simulate some bookings
        bookingService.bookRoom("Alice", "Single Room");
        bookingService.bookRoom("Bob", "Double Room");

        // Display current state
        bookingService.displayActiveBookings();
        inventory.displayInventory();

        // Save state before shutdown
        PersistenceService.saveState(inventory, bookingService);

        System.out.println("\nSystem shutdown simulation complete.");
    }
}