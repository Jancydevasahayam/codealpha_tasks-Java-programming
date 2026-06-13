/**
 * Stores customer identity and contact details.
 */
public class Customer {
    private final String customerId;
    private String name;
    private String phone;
    private String email;

    public Customer(String customerId, String name, String phone, String email) {
        this.customerId = customerId;
        this.name = name;
        this.phone = phone;
        this.email = email;
    }

    public String getCustomerId() {
        return customerId;
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

    public String getEmail() {
        return email;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String toFileString() {
        return customerId + "|" + escape(name) + "|" + phone + "|" + email;
    }

    public static Customer fromFileString(String line) {
        String[] parts = line.split("\\|", -1);
        if (parts.length != 4) {
            throw new IllegalArgumentException("Invalid customer record: " + line);
        }
        return new Customer(parts[0], unescape(parts[1]), parts[2], parts[3]);
    }

    private static String escape(String value) {
        return value == null ? "" : value.replace("|", " ");
    }

    private static String unescape(String value) {
        return value == null ? "" : value;
    }
}
