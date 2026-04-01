/**
 * RoomInventory
 *
 * This class manages centralized room availability using HashMap.
 * It acts as a single source of truth for all room inventory.
 *
 * @author YourName
 * @version 3.0
 */
import java.util.HashMap;
import java.util.Map;

class RoomInventory {

    private Map<String, Integer> inventory;

    // Constructor to initialize inventory
    public RoomInventory() {
        inventory = new HashMap<>();
    }

    // Method to add room type with availability
    public void addRoom(String roomType, int count) {
        inventory.put(roomType, count);
    }

    // Method to get availability
    public int getAvailability(String roomType) {
        return inventory.getOrDefault(roomType, 0);
    }

    // Method to update availability
    public void updateAvailability(String roomType, int newCount) {
        if (inventory.containsKey(roomType)) {
            inventory.put(roomType, newCount);
        } else {
            System.out.println("Room type not found: " + roomType);
        }
    }

    // Display full inventory
    public void displayInventory() {
        System.out.println("---- Current Room Inventory ----");
        for (Map.Entry<String, Integer> entry : inventory.entrySet()) {
            System.out.println(entry.getKey() + " : " + entry.getValue());
        }
    }
}


/**
 * UseCase3InventorySetup
 *
 * Entry point for demonstrating centralized inventory management.
 *
 * @author YourName
 * @version 3.1
 */
public class BookMyStayApp {

    public static void main(String[] args) {

        System.out.println("=======================================");
        System.out.println("     Welcome to Book My Stay App       ");
        System.out.println("     Hotel Booking System v3.1         ");
        System.out.println("=======================================\n");

        // Initialize inventory
        RoomInventory inventory = new RoomInventory();

        // Register room types with availability
        inventory.addRoom("Single Room", 5);
        inventory.addRoom("Double Room", 3);
        inventory.addRoom("Suite Room", 2);

        // Display initial inventory
        inventory.displayInventory();

        // Check availability
        System.out.println("\nChecking availability for Double Room:");
        System.out.println("Available: " + inventory.getAvailability("Double Room"));

        // Update availability
        System.out.println("\nUpdating availability for Double Room...");
        inventory.updateAvailability("Double Room", 4);

        // Display updated inventory
        System.out.println();
        inventory.displayInventory();

        System.out.println("\nApplication terminated successfully.");
    }
}