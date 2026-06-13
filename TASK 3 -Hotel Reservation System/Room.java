import java.util.Locale;

/**
 * Abstract base class for all hotel rooms.
 * Demonstrates encapsulation, abstraction, inheritance, and polymorphism.
 */
public abstract class Room {
    private final int roomNumber;
    private final String roomType;
    private final double pricePerNight;
    private boolean available;

    protected Room(int roomNumber, String roomType, double pricePerNight, boolean available) {
        this.roomNumber = roomNumber;
        this.roomType = roomType;
        this.pricePerNight = pricePerNight;
        this.available = available;
    }

    public int getRoomNumber() {
        return roomNumber;
    }

    public String getRoomType() {
        return roomType;
    }

    public double getPricePerNight() {
        return pricePerNight;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public abstract String getDescription();

    public String toFileString() {
        return roomNumber + "|" + roomType + "|" + pricePerNight + "|" + available;
    }

    public static Room fromFileString(String line) {
        String[] parts = line.split("\\|");
        if (parts.length != 4) {
            throw new IllegalArgumentException("Invalid room record: " + line);
        }

        int number = Integer.parseInt(parts[0]);
        String type = parts[1];
        double price = Double.parseDouble(parts[2]);
        boolean available = Boolean.parseBoolean(parts[3]);

        switch (type.toLowerCase(Locale.ROOT)) {
            case "standard room":
                return new StandardRoom(number, price, available);
            case "deluxe room":
                return new DeluxeRoom(number, price, available);
            case "suite room":
                return new SuiteRoom(number, price, available);
            default:
                throw new IllegalArgumentException("Unknown room type: " + type);
        }
    }
}

class StandardRoom extends Room {
    public StandardRoom(int roomNumber, double pricePerNight, boolean available) {
        super(roomNumber, "Standard Room", pricePerNight, available);
    }

    @Override
    public String getDescription() {
        return "Comfortable standard room with essential amenities.";
    }
}

class DeluxeRoom extends Room {
    public DeluxeRoom(int roomNumber, double pricePerNight, boolean available) {
        super(roomNumber, "Deluxe Room", pricePerNight, available);
    }

    @Override
    public String getDescription() {
        return "Spacious deluxe room with premium furnishings.";
    }
}

class SuiteRoom extends Room {
    public SuiteRoom(int roomNumber, double pricePerNight, boolean available) {
        super(roomNumber, "Suite Room", pricePerNight, available);
    }

    @Override
    public String getDescription() {
        return "Luxury suite with separate living area and executive services.";
    }
}
