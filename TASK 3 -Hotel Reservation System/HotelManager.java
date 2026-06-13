import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Service layer for hotel operations, validation, analytics, and File I/O.
 */
public class HotelManager {
    private static final Pattern PHONE_PATTERN = Pattern.compile("^[0-9]{10,15}$");
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");

    private final List<Room> rooms = new ArrayList<>();
    private final List<Customer> customers = new ArrayList<>();
    private final List<Reservation> reservations = new ArrayList<>();
    private final List<Payment> payments = new ArrayList<>();

    private final Path dataDirectory;
    private final Path roomsFile;
    private final Path customersFile;
    private final Path reservationsFile;
    private final Path paymentsFile;

    public HotelManager() {
        this(Paths.get("data"));
    }

    public HotelManager(Path dataDirectory) {
        this.dataDirectory = dataDirectory;
        this.roomsFile = dataDirectory.resolve("rooms.txt");
        this.customersFile = dataDirectory.resolve("customers.txt");
        this.reservationsFile = dataDirectory.resolve("reservations.txt");
        this.paymentsFile = dataDirectory.resolve("payments.txt");
        loadData();
    }

    public final void loadData() {
        try {
            Files.createDirectories(dataDirectory);
            loadRooms();
            loadCustomers();
            loadReservations();
            loadPayments();
            seedRoomsIfEmpty();
        } catch (IOException | RuntimeException ex) {
            throw new IllegalStateException("Unable to load hotel data: " + ex.getMessage(), ex);
        }
    }

    private void loadRooms() throws IOException {
        rooms.clear();
        if (!Files.exists(roomsFile)) {
            return;
        }
        try (BufferedReader reader = Files.newBufferedReader(roomsFile, StandardCharsets.UTF_8)) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    rooms.add(Room.fromFileString(line));
                }
            }
        }
    }

    private void loadCustomers() throws IOException {
        customers.clear();
        if (!Files.exists(customersFile)) {
            return;
        }
        try (BufferedReader reader = Files.newBufferedReader(customersFile, StandardCharsets.UTF_8)) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    customers.add(Customer.fromFileString(line));
                }
            }
        }
    }

    private void loadReservations() throws IOException {
        reservations.clear();
        if (!Files.exists(reservationsFile)) {
            return;
        }
        try (BufferedReader reader = Files.newBufferedReader(reservationsFile, StandardCharsets.UTF_8)) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    reservations.add(Reservation.fromFileString(line, this));
                }
            }
        }
    }

    private void loadPayments() throws IOException {
        payments.clear();
        if (!Files.exists(paymentsFile)) {
            return;
        }
        try (BufferedReader reader = Files.newBufferedReader(paymentsFile, StandardCharsets.UTF_8)) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    payments.add(Payment.fromFileString(line));
                }
            }
        }
    }

    private void seedRoomsIfEmpty() {
        if (!rooms.isEmpty()) {
            return;
        }
        rooms.add(new StandardRoom(101, 2200.00, true));
        rooms.add(new StandardRoom(102, 2200.00, true));
        rooms.add(new StandardRoom(103, 2400.00, true));
        rooms.add(new DeluxeRoom(201, 3600.00, true));
        rooms.add(new DeluxeRoom(202, 3900.00, true));
        rooms.add(new DeluxeRoom(203, 4200.00, true));
        rooms.add(new SuiteRoom(301, 6500.00, true));
        rooms.add(new SuiteRoom(302, 7200.00, true));
        saveRooms();
    }

    public List<Room> getRooms() {
        return new ArrayList<>(rooms);
    }

    public List<Customer> getCustomers() {
        return new ArrayList<>(customers);
    }

    public List<Reservation> getReservations() {
        return new ArrayList<>(reservations);
    }

    public List<Payment> getPayments() {
        return new ArrayList<>(payments);
    }

    public Room findRoomByNumber(int roomNumber) {
        return rooms.stream()
                .filter(room -> room.getRoomNumber() == roomNumber)
                .findFirst()
                .orElse(null);
    }

    public Customer findCustomerById(String customerId) {
        return customers.stream()
                .filter(customer -> customer.getCustomerId().equals(customerId))
                .findFirst()
                .orElse(null);
    }

    public Reservation findReservationById(String reservationId) {
        return reservations.stream()
                .filter(reservation -> reservation.getReservationId().equals(reservationId))
                .findFirst()
                .orElse(null);
    }

    public Payment findPaymentByReservationId(String reservationId) {
        return payments.stream()
                .filter(payment -> payment.getReservationId().equals(reservationId))
                .findFirst()
                .orElse(null);
    }

    public List<Room> searchAvailableRooms(String roomType, Double minPrice, Double maxPrice) {
        return rooms.stream()
                .filter(Room::isAvailable)
                .filter(room -> roomType == null || roomType.equals("All") || room.getRoomType().equals(roomType))
                .filter(room -> minPrice == null || room.getPricePerNight() >= minPrice)
                .filter(room -> maxPrice == null || room.getPricePerNight() <= maxPrice)
                .sorted(Comparator.comparing(Room::getRoomType).thenComparing(Room::getRoomNumber))
                .collect(Collectors.toList());
    }

    public Reservation bookRoom(String name, String phone, String email, int roomNumber,
                                LocalDate checkIn, LocalDate checkOut) {
        validateCustomer(name, phone, email);
        validateDates(checkIn, checkOut);

        Room room = findRoomByNumber(roomNumber);
        if (room == null) {
            throw new IllegalArgumentException("Selected room does not exist.");
        }
        if (!room.isAvailable()) {
            throw new IllegalArgumentException("Room is not available.");
        }
        if (hasActiveReservationForRoom(roomNumber)) {
            throw new IllegalArgumentException("Duplicate active reservation is not allowed for this room.");
        }

        Customer customer = new Customer(nextId("C", customers.size() + 1), name.trim(), phone.trim(), email.trim());
        Reservation reservation = new Reservation(nextId("R", reservations.size() + 1), customer, room,
                checkIn, checkOut, "Confirmed", "Pending");

        room.setAvailable(false);
        customers.add(customer);
        reservations.add(reservation);
        saveAll();
        return reservation;
    }

    public void cancelReservation(String reservationId) {
        Reservation reservation = findReservationById(reservationId);
        if (reservation == null) {
            throw new IllegalArgumentException("Reservation ID not found.");
        }
        if ("Cancelled".equalsIgnoreCase(reservation.getBookingStatus())) {
            throw new IllegalArgumentException("Reservation is already cancelled.");
        }

        reservation.setBookingStatus("Cancelled");
        reservation.getRoom().setAvailable(true);
        saveRooms();
        saveReservations();
    }

    public Payment processPayment(String reservationId, String method) {
        Reservation reservation = findReservationById(reservationId);
        if (reservation == null) {
            throw new IllegalArgumentException("Reservation ID not found.");
        }
        if ("Cancelled".equalsIgnoreCase(reservation.getBookingStatus())) {
            throw new IllegalArgumentException("Cannot process payment for a cancelled reservation.");
        }
        if ("Paid".equalsIgnoreCase(reservation.getPaymentStatus())) {
            throw new IllegalArgumentException("Payment is already completed for this reservation.");
        }

        Payment payment = new Payment(nextId("P", payments.size() + 1), reservationId, method,
                reservation.getTotalCost(), "Paid", LocalDateTime.now());
        payments.add(payment);
        reservation.setPaymentStatus("Paid");
        saveReservations();
        savePayments();
        return payment;
    }

    public Map<String, String> getReportSummary() {
        Map<String, String> report = new HashMap<>();
        long availableRooms = rooms.stream().filter(Room::isAvailable).count();
        long occupiedRooms = rooms.size() - availableRooms;
        double revenue = payments.stream()
                .filter(payment -> "Paid".equalsIgnoreCase(payment.getStatus()))
                .mapToDouble(Payment::getAmount)
                .sum();

        report.put("Total Rooms", String.valueOf(rooms.size()));
        report.put("Available Rooms", String.valueOf(availableRooms));
        report.put("Occupied Rooms", String.valueOf(occupiedRooms));
        report.put("Total Reservations", String.valueOf(reservations.size()));
        report.put("Revenue Generated", "Rs. " + String.format("%.2f", revenue));
        report.put("Category Statistics", getRoomCategoryStatistics());
        return report;
    }

    private String getRoomCategoryStatistics() {
        Map<String, Long> stats = rooms.stream()
                .collect(Collectors.groupingBy(Room::getRoomType, Collectors.counting()));
        return stats.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .map(entry -> entry.getKey() + ": " + entry.getValue())
                .collect(Collectors.joining(", "));
    }

    private boolean hasActiveReservationForRoom(int roomNumber) {
        return reservations.stream().anyMatch(reservation ->
                reservation.getRoom().getRoomNumber() == roomNumber
                        && !"Cancelled".equalsIgnoreCase(reservation.getBookingStatus()));
    }

    private void validateCustomer(String name, String phone, String email) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Customer name cannot be empty.");
        }
        if (phone == null || !PHONE_PATTERN.matcher(phone.trim()).matches()) {
            throw new IllegalArgumentException("Phone number must contain 10 to 15 digits.");
        }
        if (email == null || !EMAIL_PATTERN.matcher(email.trim()).matches()) {
            throw new IllegalArgumentException("Please enter a valid email address.");
        }
    }

    private void validateDates(LocalDate checkIn, LocalDate checkOut) {
        if (checkIn == null || checkOut == null) {
            throw new IllegalArgumentException("Both check-in and check-out dates are required.");
        }
        if (!checkIn.isBefore(checkOut)) {
            throw new IllegalArgumentException("Check-in date must be before check-out date.");
        }
    }

    private String nextId(String prefix, int start) {
        int value = start;
        while (idExists(prefix + String.format("%04d", value))) {
            value++;
        }
        return prefix + String.format("%04d", value);
    }

    private boolean idExists(String id) {
        return customers.stream().anyMatch(customer -> customer.getCustomerId().equals(id))
                || reservations.stream().anyMatch(reservation -> reservation.getReservationId().equals(id))
                || payments.stream().anyMatch(payment -> payment.getPaymentId().equals(id));
    }

    private void saveAll() {
        saveRooms();
        saveCustomers();
        saveReservations();
        savePayments();
    }

    public void saveRooms() {
        writeLines(roomsFile, rooms.stream().map(Room::toFileString).collect(Collectors.toList()));
    }

    public void saveCustomers() {
        writeLines(customersFile, customers.stream().map(Customer::toFileString).collect(Collectors.toList()));
    }

    public void saveReservations() {
        writeLines(reservationsFile, reservations.stream().map(Reservation::toFileString).collect(Collectors.toList()));
    }

    public void savePayments() {
        writeLines(paymentsFile, payments.stream().map(Payment::toFileString).collect(Collectors.toList()));
    }

    private void writeLines(Path file, List<String> lines) {
        try {
            Files.createDirectories(dataDirectory);
            try (BufferedWriter writer = Files.newBufferedWriter(file, StandardCharsets.UTF_8)) {
                for (String line : lines) {
                    writer.write(line);
                    writer.newLine();
                }
            }
        } catch (IOException ex) {
            throw new IllegalStateException("Unable to save data file " + file + ": " + ex.getMessage(), ex);
        }
    }
}
