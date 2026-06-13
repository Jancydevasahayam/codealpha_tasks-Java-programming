import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

/**
 * Application entry point with a simple admin/customer login simulation.
 */
public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            if (login()) {
                HotelReservationGUI gui = new HotelReservationGUI();
                gui.setVisible(true);
            }
        });
    }

    private static boolean login() {
        String[] roles = {"Admin", "Customer"};
        String role = (String) JOptionPane.showInputDialog(null,
                "Select login type", "Hotel Reservation Login",
                JOptionPane.QUESTION_MESSAGE, null, roles, roles[0]);
        if (role == null) {
            return false;
        }

        String username = JOptionPane.showInputDialog(null, role + " Username:");
        if (username == null) {
            return false;
        }
        String password = JOptionPane.showInputDialog(null, role + " Password:");
        if (password == null) {
            return false;
        }

        boolean adminOk = role.equals("Admin") && username.equals("admin") && password.equals("admin123");
        boolean customerOk = role.equals("Customer") && username.equals("customer") && password.equals("customer123");
        if (adminOk || customerOk) {
            JOptionPane.showMessageDialog(null, "Login successful. Welcome, " + role + "!");
            return true;
        }

        JOptionPane.showMessageDialog(null,
                "Invalid login.\nAdmin: admin/admin123\nCustomer: customer/customer123",
                "Login Failed", JOptionPane.ERROR_MESSAGE);
        return false;
    }
}
