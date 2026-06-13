import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Simulates hotel reservation payments and receipt generation.
 */
public class Payment {
    private final String paymentId;
    private final String reservationId;
    private final String method;
    private final double amount;
    private final String status;
    private final LocalDateTime paidAt;

    public Payment(String paymentId, String reservationId, String method, double amount,
                   String status, LocalDateTime paidAt) {
        this.paymentId = paymentId;
        this.reservationId = reservationId;
        this.method = method;
        this.amount = amount;
        this.status = status;
        this.paidAt = paidAt;
    }

    public String getPaymentId() {
        return paymentId;
    }

    public String getReservationId() {
        return reservationId;
    }

    public String getMethod() {
        return method;
    }

    public double getAmount() {
        return amount;
    }

    public String getStatus() {
        return status;
    }

    public LocalDateTime getPaidAt() {
        return paidAt;
    }

    public String toFileString() {
        return paymentId + "|" + reservationId + "|" + method + "|" + amount + "|" + status + "|" + paidAt;
    }

    public static Payment fromFileString(String line) {
        String[] parts = line.split("\\|", -1);
        if (parts.length != 6) {
            throw new IllegalArgumentException("Invalid payment record: " + line);
        }
        return new Payment(parts[0], parts[1], parts[2], Double.parseDouble(parts[3]),
                parts[4], LocalDateTime.parse(parts[5]));
    }

    public String generateReceipt(Reservation reservation) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMM yyyy, hh:mm a");
        Customer customer = reservation.getCustomer();
        Room room = reservation.getRoom();

        return "GRAND PALACE HOTEL\n"
                + "Professional Booking Receipt\n"
                + "----------------------------------------\n"
                + "Payment ID      : " + paymentId + "\n"
                + "Reservation ID  : " + reservationId + "\n"
                + "Customer ID     : " + customer.getCustomerId() + "\n"
                + "Customer Name   : " + customer.getName() + "\n"
                + "Phone           : " + customer.getPhone() + "\n"
                + "Email           : " + customer.getEmail() + "\n"
                + "Room Number     : " + room.getRoomNumber() + "\n"
                + "Room Type       : " + room.getRoomType() + "\n"
                + "Check-in Date   : " + reservation.getCheckInDate() + "\n"
                + "Check-out Date  : " + reservation.getCheckOutDate() + "\n"
                + "Number of Days  : " + reservation.getNumberOfDays() + "\n"
                + "Amount Paid     : Rs. " + String.format("%.2f", amount) + "\n"
                + "Payment Method  : " + method + "\n"
                + "Payment Status  : " + status + "\n"
                + "Paid At         : " + paidAt.format(formatter) + "\n"
                + "----------------------------------------\n"
                + "Thank you for choosing Grand Palace Hotel.";
    }
}
