import java.util.*;

/**
 * Reservation
 * Represents a booking request
 *
 * @version 6.0
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
}


/**
 * BookingRequestQueue
 * FIFO queue for booking requests
 *
 * @version 6.0
 */
class BookingRequestQueue {
    private Queue<Reservation> queue = new LinkedList<>();

    public void addRequest(Reservation r) {
        queue.offer(r);
    }

    public Reservation getNextRequest() {
        return queue.poll(); // dequeue
    }

    public boolean isEmpty() {
        return queue.isEmpty();
    }
}


/**
 * RoomInventory
 * Centralized inventory management
 *
 * @version 6.0
 */
class RoomInventory {
    private Map<String, Integer> inventory = new HashMap<>();

    public RoomInventory() {
        inventory.put("Single Room", 2);
        inventory.put("Double Room", 1);
        inventory.put("Suite Room", 1);
    }

    public int getAvailability(String roomType) {
        return inventory.getOrDefault(roomType, 0);
    }

    public void decrementRoom(String roomType) {
        inventory.put(roomType, inventory.get(roomType) - 1);
    }

    public void displayInventory() {
        System.out.println("\n---- Current Inventory ----");
        for (Map.Entry<String, Integer> entry : inventory.entrySet()) {
            System.out.println(entry.getKey() + " : " + entry.getValue());
        }
    }
}


/**
 * BookingService
 * Handles allocation and confirmation
 *
 * @version 6.0
 */
class BookingService {

    private RoomInventory inventory;

    // Track allocated room IDs per room type
    private Map<String, Set<String>> allocatedRooms = new HashMap<>();

    // Global set to ensure uniqueness
    private Set<String> allAllocatedRoomIds = new HashSet<>();

    public BookingService(RoomInventory inventory) {
        this.inventory = inventory;
    }

    // Generate unique room ID
    private String generateRoomId(String roomType) {
        String prefix = roomType.substring(0, 2).toUpperCase();
        String roomId;

        do {
            roomId = prefix + "-" + (int)(Math.random() * 1000);
        } while (allAllocatedRoomIds.contains(roomId));

        return roomId;
    }

    // Process booking request
    public void processRequest(Reservation r) {

        String roomType = r.getRoomType();

        System.out.println("\nProcessing request for " + r.getGuestName());

        // Check availability
        if (inventory.getAvailability(roomType) > 0) {

            // Generate unique room ID
            String roomId = generateRoomId(roomType);

            // Add to global set
            allAllocatedRoomIds.add(roomId);

            // Map room type → allocated IDs
            allocatedRooms.putIfAbsent(roomType, new HashSet<>());
            allocatedRooms.get(roomType).add(roomId);

            // Update inventory immediately
            inventory.decrementRoom(roomType);

            // Confirm reservation
            System.out.println("Booking Confirmed!");
            System.out.println("Guest: " + r.getGuestName());
            System.out.println("Room Type: " + roomType);
            System.out.println("Allocated Room ID: " + roomId);

        } else {
            System.out.println("Booking Failed for " + r.getGuestName() +
                    " (No rooms available)");
        }
    }

    public void displayAllocations() {
        System.out.println("\n---- Allocated Rooms ----");
        for (String type : allocatedRooms.keySet()) {
            System.out.println(type + " -> " + allocatedRooms.get(type));
        }
    }
}


/**
 * UseCase6RoomAllocationService
 * Entry point
 *
 * @version 6.1
 */
public class BookMyStayApp {

    public static void main(String[] args) {

        System.out.println("=======================================");
        System.out.println("     Welcome to Book My Stay App       ");
        System.out.println("     Hotel Booking System v6.1         ");
        System.out.println("=======================================\n");

        // Initialize components
        RoomInventory inventory = new RoomInventory();
        BookingRequestQueue queue = new BookingRequestQueue();
        BookingService bookingService = new BookingService(inventory);

        // Add booking requests
        queue.addRequest(new Reservation("Alice", "Single Room"));
        queue.addRequest(new Reservation("Bob", "Single Room"));
        queue.addRequest(new Reservation("Charlie", "Single Room")); // should fail
        queue.addRequest(new Reservation("David", "Double Room"));

        // Process queue (FIFO)
        while (!queue.isEmpty()) {
            Reservation r = queue.getNextRequest();
            bookingService.processRequest(r);
        }

        // Display results
        bookingService.displayAllocations();
        inventory.displayInventory();

        System.out.println("\nAll bookings processed successfully.");
    }
}