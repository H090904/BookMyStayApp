import java.util.HashMap;
import java.util.Map;

/**
 * Abstract Room Class
 * Defines common properties of all rooms
 *
 * @version 4.0
 */
abstract class Room {

    private String roomType;
    private double price;

    public Room(String roomType, double price) {
        this.roomType = roomType;
        this.price = price;
    }

    public String getRoomType() {
        return roomType;
    }

    public double getPrice() {
        return price;
    }

    public abstract void displayDetails();
}


/**
 * Concrete Room Classes
 * @version 4.0
 */
class SingleRoom extends Room {
    public SingleRoom() {
        super("Single Room", 2000);
    }

    public void displayDetails() {
        System.out.println("Room: " + getRoomType() + " | Price: ₹" + getPrice());
    }
}

class DoubleRoom extends Room {
    public DoubleRoom() {
        super("Double Room", 3500);
    }

    public void displayDetails() {
        System.out.println("Room: " + getRoomType() + " | Price: ₹" + getPrice());
    }
}

class SuiteRoom extends Room {
    public SuiteRoom() {
        super("Suite Room", 6000);
    }

    public void displayDetails() {
        System.out.println("Room: " + getRoomType() + " | Price: ₹" + getPrice());
    }
}


/**
 * RoomInventory - Centralized state holder
 * @version 4.0
 */
class RoomInventory {

    private Map<String, Integer> inventory = new HashMap<>();

    public RoomInventory() {
        inventory.put("Single Room", 5);
        inventory.put("Double Room", 3);
        inventory.put("Suite Room", 0); // Example: unavailable
    }

    // Read-only access
    public int getAvailability(String roomType) {
        return inventory.getOrDefault(roomType, 0);
    }

    // Expose full inventory (read-only usage)
    public Map<String, Integer> getAllAvailability() {
        return inventory;
    }
}


/**
 * SearchService - Handles read-only operations
 * @version 4.0
 */
class SearchService {

    private RoomInventory inventory;
    private Map<String, Room> roomCatalog;

    public SearchService(RoomInventory inventory) {
        this.inventory = inventory;

        // Room domain objects
        roomCatalog = new HashMap<>();
        roomCatalog.put("Single Room", new SingleRoom());
        roomCatalog.put("Double Room", new DoubleRoom());
        roomCatalog.put("Suite Room", new SuiteRoom());
    }

    // Search available rooms (READ-ONLY)
    public void searchAvailableRooms() {
        System.out.println("---- Available Rooms ----");

        for (String roomType : roomCatalog.keySet()) {

            int available = inventory.getAvailability(roomType);

            // Defensive check: show only available rooms
            if (available > 0) {
                Room room = roomCatalog.get(roomType);
                room.displayDetails();
                System.out.println("Available: " + available);
                System.out.println("-------------------------");
            }
        }
    }
}


/**
 * UseCase4RoomSearch
 * Entry point
 *
 * @version 4.1
 */
public class BookMyStayApp {

    public static void main(String[] args) {

        System.out.println("=======================================");
        System.out.println("     Welcome to Book My Stay App       ");
        System.out.println("     Hotel Booking System v4.1         ");
        System.out.println("=======================================\n");

        // Initialize inventory
        RoomInventory inventory = new RoomInventory();

        // Initialize search service
        SearchService searchService = new SearchService(inventory);

        // Perform search (read-only)
        searchService.searchAvailableRooms();

        System.out.println("\nSearch completed. No changes made to inventory.");
    }
}