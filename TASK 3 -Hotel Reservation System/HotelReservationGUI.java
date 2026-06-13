import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 * Swing dashboard for room search, booking, cancellation, payments, and reports.
 */
public class HotelReservationGUI extends JFrame {
    private final HotelManager manager;

    private JTable roomTable;
    private JTable reservationTable;
    private DefaultTableModel roomTableModel;
    private DefaultTableModel reservationTableModel;

    private JTextField nameField;
    private JTextField phoneField;
    private JTextField emailField;
    private JTextField checkInField;
    private JTextField checkOutField;
    private JTextField minPriceField;
    private JTextField maxPriceField;
    private JComboBox<String> roomTypeCombo;
    private JComboBox<String> paymentMethodCombo;
    private JTextArea reportArea;
    private boolean darkMode;

    public HotelReservationGUI() {
        this.manager = new HotelManager();
        configureLookAndFeel();
        initializeComponents();
        refreshTables();
        refreshReports();
    }

    private void configureLookAndFeel() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {
            // Default Swing look and feel is acceptable if system theme fails.
        }
    }

    private void initializeComponents() {
        setTitle("Hotel Reservation System - Grand Palace Hotel");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(1180, 760));
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        add(createHeaderPanel(), BorderLayout.NORTH);
        add(createDashboardPanel(), BorderLayout.CENTER);
        add(createFooterPanel(), BorderLayout.SOUTH);
        applyTheme();
    }

    private JPanel createHeaderPanel() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBorder(new EmptyBorder(18, 24, 18, 24));

        JLabel title = new JLabel("Grand Palace Hotel Reservation Dashboard");
        title.setFont(new Font("Segoe UI", Font.BOLD, 26));
        title.setForeground(Color.WHITE);

        JButton darkModeButton = new JButton("Dark Mode");
        darkModeButton.addActionListener(event -> {
            darkMode = !darkMode;
            darkModeButton.setText(darkMode ? "Light Mode" : "Dark Mode");
            applyTheme();
        });

        header.add(title, BorderLayout.WEST);
        header.add(darkModeButton, BorderLayout.EAST);
        header.setBackground(new Color(35, 72, 108));
        return header;
    }

    private JPanel createDashboardPanel() {
        JPanel root = new JPanel(new BorderLayout(14, 14));
        root.setBorder(new EmptyBorder(14, 14, 14, 14));

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, createFormPanel(), createTablesPanel());
        splitPane.setResizeWeight(0.34);
        splitPane.setContinuousLayout(true);
        root.add(splitPane, BorderLayout.CENTER);
        return root;
    }

    private JPanel createFormPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createTitledBorder("Booking Form"));

        JPanel fields = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        gbc.insets.set(6, 6, 6, 6);

        nameField = new JTextField();
        phoneField = new JTextField();
        emailField = new JTextField();
        checkInField = new JTextField("2026-07-01");
        checkOutField = new JTextField("2026-07-03");
        minPriceField = new JTextField();
        maxPriceField = new JTextField();
        roomTypeCombo = new JComboBox<>(new String[]{"All", "Standard Room", "Deluxe Room", "Suite Room"});
        paymentMethodCombo = new JComboBox<>(new String[]{"Credit Card", "Debit Card", "UPI", "Cash"});

        addField(fields, gbc, 0, "Customer Name", nameField);
        addField(fields, gbc, 1, "Phone Number", phoneField);
        addField(fields, gbc, 2, "Email Address", emailField);
        addField(fields, gbc, 3, "Room Category", roomTypeCombo);
        addField(fields, gbc, 4, "Check-in Date (yyyy-mm-dd)", checkInField);
        addField(fields, gbc, 5, "Check-out Date (yyyy-mm-dd)", checkOutField);
        addField(fields, gbc, 6, "Min Price", minPriceField);
        addField(fields, gbc, 7, "Max Price", maxPriceField);
        addField(fields, gbc, 8, "Payment Method", paymentMethodCombo);

        panel.add(fields, BorderLayout.NORTH);
        panel.add(createButtonPanel(), BorderLayout.CENTER);

        reportArea = new JTextArea(9, 28);
        reportArea.setEditable(false);
        reportArea.setFont(new Font("Consolas", Font.PLAIN, 13));
        JScrollPane reportScroll = new JScrollPane(reportArea);
        reportScroll.setBorder(BorderFactory.createTitledBorder("Reports and Analytics"));
        panel.add(reportScroll, BorderLayout.SOUTH);
        return panel;
    }

    private void addField(JPanel panel, GridBagConstraints gbc, int row, String label, java.awt.Component field) {
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.weightx = 0.35;
        JLabel fieldLabel = new JLabel(label);
        fieldLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        panel.add(fieldLabel, gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.65;
        panel.add(field, gbc);
    }

    private JPanel createButtonPanel() {
        JPanel buttons = new JPanel(new GridLayout(0, 2, 8, 8));
        buttons.setBorder(new EmptyBorder(8, 6, 8, 6));

        JButton searchRooms = new JButton("Search Rooms");
        JButton bookRoom = new JButton("Book Room");
        JButton cancelReservation = new JButton("Cancel Reservation");
        JButton viewReservations = new JButton("View Reservations");
        JButton processPayment = new JButton("Process Payment");
        JButton clearForm = new JButton("Clear Form");
        JButton exportHistory = new JButton("Export History");
        JButton exit = new JButton("Exit");

        searchRooms.addActionListener(event -> searchRooms());
        bookRoom.addActionListener(event -> bookSelectedRoom());
        cancelReservation.addActionListener(event -> cancelSelectedReservation());
        viewReservations.addActionListener(event -> showReservationDetails());
        processPayment.addActionListener(event -> processPayment());
        clearForm.addActionListener(event -> clearForm());
        exportHistory.addActionListener(event -> exportBookingHistory());
        exit.addActionListener(event -> dispose());

        buttons.add(searchRooms);
        buttons.add(bookRoom);
        buttons.add(cancelReservation);
        buttons.add(viewReservations);
        buttons.add(processPayment);
        buttons.add(clearForm);
        buttons.add(exportHistory);
        buttons.add(exit);
        return buttons;
    }

    private JPanel createTablesPanel() {
        JPanel tables = new JPanel(new GridLayout(2, 1, 12, 12));

        roomTableModel = new DefaultTableModel(new Object[]{"Room No", "Type", "Price/Night", "Available", "Description"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        roomTable = new JTable(roomTableModel);
        roomTable.setRowHeight(26);
        JScrollPane roomScroll = new JScrollPane(roomTable);
        roomScroll.setBorder(BorderFactory.createTitledBorder("Rooms"));

        reservationTableModel = new DefaultTableModel(new Object[]{
                "Reservation ID", "Customer", "Room No", "Type", "Check-in", "Check-out", "Days",
                "Total", "Booking", "Payment"
        }, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        reservationTable = new JTable(reservationTableModel);
        reservationTable.setRowHeight(26);
        JScrollPane reservationScroll = new JScrollPane(reservationTable);
        reservationScroll.setBorder(BorderFactory.createTitledBorder("Reservations"));

        tables.add(roomScroll);
        tables.add(reservationScroll);
        return tables;
    }

    private JPanel createFooterPanel() {
        JPanel footer = new JPanel(new BorderLayout());
        footer.setBorder(new EmptyBorder(8, 18, 8, 18));
        JLabel label = new JLabel("Status: Ready | Select a room to book, select a reservation to cancel or pay.");
        label.setHorizontalAlignment(SwingConstants.LEFT);
        footer.add(label, BorderLayout.WEST);
        return footer;
    }

    private void searchRooms() {
        try {
            Double min = parseOptionalDouble(minPriceField.getText());
            Double max = parseOptionalDouble(maxPriceField.getText());
            if (min != null && max != null && min > max) {
                throw new IllegalArgumentException("Minimum price cannot be greater than maximum price.");
            }

            List<Room> results = manager.searchAvailableRooms((String) roomTypeCombo.getSelectedItem(), min, max);
            loadRoomsIntoTable(results);
            JOptionPane.showMessageDialog(this, results.size() + " available room(s) found.",
                    "Search Complete", JOptionPane.INFORMATION_MESSAGE);
        } catch (RuntimeException ex) {
            showError(ex.getMessage());
        }
    }

    private void bookSelectedRoom() {
        int selectedRow = roomTable.getSelectedRow();
        if (selectedRow < 0) {
            showError("Please select an available room from the room table.");
            return;
        }

        try {
            int modelRow = roomTable.convertRowIndexToModel(selectedRow);
            int roomNumber = Integer.parseInt(roomTableModel.getValueAt(modelRow, 0).toString());
            Reservation reservation = manager.bookRoom(
                    nameField.getText(),
                    phoneField.getText(),
                    emailField.getText(),
                    roomNumber,
                    LocalDate.parse(checkInField.getText().trim()),
                    LocalDate.parse(checkOutField.getText().trim())
            );
            refreshTables();
            refreshReports();
            JOptionPane.showMessageDialog(this,
                    "Booking confirmed!\nReservation ID: " + reservation.getReservationId()
                            + "\nTotal Amount: Rs. " + String.format("%.2f", reservation.getTotalCost()),
                    "Booking Confirmation", JOptionPane.INFORMATION_MESSAGE);
        } catch (RuntimeException ex) {
            showError(ex.getMessage());
        }
    }

    private void cancelSelectedReservation() {
        Reservation reservation = getSelectedReservation();
        if (reservation == null) {
            showError("Please select a reservation to cancel.");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this,
                "Cancel reservation " + reservation.getReservationId() + "?",
                "Cancellation Confirmation", JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION) {
            return;
        }

        try {
            manager.cancelReservation(reservation.getReservationId());
            refreshTables();
            refreshReports();
            JOptionPane.showMessageDialog(this, "Reservation cancelled successfully.",
                    "Cancellation Confirmation", JOptionPane.INFORMATION_MESSAGE);
        } catch (RuntimeException ex) {
            showError(ex.getMessage());
        }
    }

    private void showReservationDetails() {
        Reservation reservation = getSelectedReservation();
        if (reservation == null) {
            showError("Please select a reservation to view details.");
            return;
        }

        String details = "Reservation ID: " + reservation.getReservationId()
                + "\nCustomer Name: " + reservation.getCustomer().getName()
                + "\nRoom Number: " + reservation.getRoom().getRoomNumber()
                + "\nRoom Type: " + reservation.getRoom().getRoomType()
                + "\nCheck-in Date: " + reservation.getCheckInDate()
                + "\nCheck-out Date: " + reservation.getCheckOutDate()
                + "\nTotal Amount: Rs. " + String.format("%.2f", reservation.getTotalCost())
                + "\nPayment Status: " + reservation.getPaymentStatus();
        JOptionPane.showMessageDialog(this, details, "Reservation Details", JOptionPane.INFORMATION_MESSAGE);
    }

    private void processPayment() {
        Reservation reservation = getSelectedReservation();
        if (reservation == null) {
            showError("Please select a reservation for payment.");
            return;
        }

        try {
            String method = (String) paymentMethodCombo.getSelectedItem();
            Payment payment = manager.processPayment(reservation.getReservationId(), method);
            refreshTables();
            refreshReports();
            JOptionPane.showMessageDialog(this, payment.generateReceipt(reservation),
                    "Payment Confirmation", JOptionPane.INFORMATION_MESSAGE);
        } catch (RuntimeException ex) {
            showError(ex.getMessage());
        }
    }

    private void exportBookingHistory() {
        try {
            java.nio.file.Path exportFile = java.nio.file.Paths.get("data", "booking_history_export.txt");
            java.util.List<String> lines = new java.util.ArrayList<>();
            lines.add("Grand Palace Hotel - Booking History Export");
            lines.add("ReservationID | Customer | Room | Type | CheckIn | CheckOut | Total | Booking | Payment");
            for (Reservation reservation : manager.getReservations()) {
                lines.add(reservation.getReservationId() + " | "
                        + reservation.getCustomer().getName() + " | "
                        + reservation.getRoom().getRoomNumber() + " | "
                        + reservation.getRoom().getRoomType() + " | "
                        + reservation.getCheckInDate() + " | "
                        + reservation.getCheckOutDate() + " | Rs. "
                        + String.format("%.2f", reservation.getTotalCost()) + " | "
                        + reservation.getBookingStatus() + " | "
                        + reservation.getPaymentStatus());
            }
            java.nio.file.Files.write(exportFile, lines);
            JOptionPane.showMessageDialog(this, "Booking history exported to " + exportFile,
                    "Export Complete", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception ex) {
            showError("Unable to export booking history: " + ex.getMessage());
        }
    }

    private Reservation getSelectedReservation() {
        int selectedRow = reservationTable.getSelectedRow();
        if (selectedRow < 0) {
            return null;
        }
        int modelRow = reservationTable.convertRowIndexToModel(selectedRow);
        String reservationId = reservationTableModel.getValueAt(modelRow, 0).toString();
        return manager.findReservationById(reservationId);
    }

    private void clearForm() {
        nameField.setText("");
        phoneField.setText("");
        emailField.setText("");
        checkInField.setText("");
        checkOutField.setText("");
        minPriceField.setText("");
        maxPriceField.setText("");
        roomTypeCombo.setSelectedIndex(0);
        paymentMethodCombo.setSelectedIndex(0);
    }

    private void refreshTables() {
        loadRoomsIntoTable(manager.getRooms());
        loadReservationsIntoTable(manager.getReservations());
    }

    private void loadRoomsIntoTable(List<Room> rooms) {
        roomTableModel.setRowCount(0);
        for (Room room : rooms) {
            roomTableModel.addRow(new Object[]{
                    room.getRoomNumber(),
                    room.getRoomType(),
                    String.format("Rs. %.2f", room.getPricePerNight()),
                    room.isAvailable() ? "Yes" : "No",
                    room.getDescription()
            });
        }
    }

    private void loadReservationsIntoTable(List<Reservation> reservations) {
        reservationTableModel.setRowCount(0);
        for (Reservation reservation : reservations) {
            reservationTableModel.addRow(new Object[]{
                    reservation.getReservationId(),
                    reservation.getCustomer().getName(),
                    reservation.getRoom().getRoomNumber(),
                    reservation.getRoom().getRoomType(),
                    reservation.getCheckInDate(),
                    reservation.getCheckOutDate(),
                    reservation.getNumberOfDays(),
                    String.format("Rs. %.2f", reservation.getTotalCost()),
                    reservation.getBookingStatus(),
                    reservation.getPaymentStatus()
            });
        }
    }

    private void refreshReports() {
        Map<String, String> report = manager.getReportSummary();
        StringBuilder builder = new StringBuilder();
        for (Map.Entry<String, String> entry : report.entrySet()) {
            builder.append(String.format("%-20s : %s%n", entry.getKey(), entry.getValue()));
        }
        reportArea.setText(builder.toString());
    }

    private Double parseOptionalDouble(String value) {
        if (value == null || value.trim().isEmpty()) {
            return null;
        }
        return Double.parseDouble(value.trim());
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    private void applyTheme() {
        Color background = darkMode ? new Color(31, 36, 42) : new Color(245, 247, 250);
        Color foreground = darkMode ? Color.WHITE : new Color(30, 36, 45);
        Color tableBackground = darkMode ? new Color(42, 48, 56) : Color.WHITE;

        applyColors(getContentPane(), background, foreground, tableBackground);
        SwingUtilities.updateComponentTreeUI(this);
    }

    private void applyColors(java.awt.Component component, Color background, Color foreground, Color tableBackground) {
        component.setForeground(foreground);
        if (component instanceof JTable) {
            component.setBackground(tableBackground);
            component.setForeground(foreground);
            return;
        }
        if (!(component instanceof JButton) && !(component instanceof JTextField) && !(component instanceof JComboBox)) {
            component.setBackground(background);
        }
        if (component instanceof java.awt.Container) {
            for (java.awt.Component child : ((java.awt.Container) component).getComponents()) {
                applyColors(child, background, foreground, tableBackground);
            }
        }
    }
}
