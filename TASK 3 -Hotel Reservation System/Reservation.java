import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

/**
 * Represents a booking made by a customer for one room.
 */
public class Reservation {
    private final String reservationId;
    private final Customer customer;
    private final Room room;
    private final LocalDate checkInDate;
    private final LocalDate checkOutDate;
    private final int numberOfDays;
    private final double totalCost;
    private String bookingStatus;
    private String paymentStatus;

    public Reservation(String reservationId, Customer customer, Room room,
                       LocalDate checkInDate, LocalDate checkOutDate,
                       String bookingStatus, String paymentStatus) {
        this.reservationId = reservationId;
        this.customer = customer;
        this.room = room;
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
        this.numberOfDays = (int) ChronoUnit.DAYS.between(checkInDate, checkOutDate);
        this.totalCost = numberOfDays * room.getPricePerNight();
        this.bookingStatus = bookingStatus;
        this.paymentStatus = paymentStatus;
    }

    public String getReservationId() {
        return reservationId;
    }

    public Customer getCustomer() {
        return customer;
    }

    public Room getRoom() {
        return room;
    }

    public LocalDate getCheckInDate() {
        return checkInDate;
    }

    public LocalDate getCheckOutDate() {
        return checkOutDate;
    }

    public int getNumberOfDays() {
        return numberOfDays;
    }

    public double getTotalCost() {
        return totalCost;
    }

    public String getBookingStatus() {
        return bookingStatus;
    }

    public void setBookingStatus(String bookingStatus) {
        this.bookingStatus = bookingStatus;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public String toFileString() {
        return reservationId + "|" + customer.getCustomerId() + "|" + room.getRoomNumber() + "|"
                + checkInDate + "|" + checkOutDate + "|" + bookingStatus + "|" + paymentStatus;
    }

    public static Reservation fromFileString(String line, HotelManager manager) {
        String[] parts = line.split("\\|", -1);
        if (parts.length != 7) {
            throw new IllegalArgumentException("Invalid reservation record: " + line);
        }

        Customer customer = manager.findCustomerById(parts[1]);
        Room room = manager.findRoomByNumber(Integer.parseInt(parts[2]));
        if (customer == null || room == null) {
            throw new IllegalArgumentException("Reservation references missing customer or room.");
        }

        return new Reservation(
                parts[0],
                customer,
                room,
                LocalDate.parse(parts[3]),
                LocalDate.parse(parts[4]),
                parts[5],
                parts[6]
        );
    }
}
