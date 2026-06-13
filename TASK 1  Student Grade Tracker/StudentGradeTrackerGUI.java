import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.text.DecimalFormat;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

/**
 * Professional Java Swing GUI for the Student Grade Tracker System.
 */
public class StudentGradeTrackerGUI extends JFrame {
    private static final DecimalFormat MARKS_FORMAT = new DecimalFormat("0.00");

    private final GradeTracker gradeTracker;
    private final JTextField studentIdField;
    private final JTextField studentNameField;
    private final JTextField javaMarksField;
    private final JTextField pythonMarksField;
    private final JTextField dbmsMarksField;
    private final JTextField operatingSystemsMarksField;
    private final JTextField mathematicsMarksField;
    private final DefaultTableModel tableModel;
    private final JTable studentTable;

    /**
     * Builds the application window and initializes all GUI components.
     */
    public StudentGradeTrackerGUI() {
        gradeTracker = new GradeTracker();
        studentIdField = new JTextField(14);
        studentNameField = new JTextField(18);
        javaMarksField = new JTextField(8);
        pythonMarksField = new JTextField(8);
        dbmsMarksField = new JTextField(8);
        operatingSystemsMarksField = new JTextField(8);
        mathematicsMarksField = new JTextField(8);
        tableModel = createTableModel();
        studentTable = new JTable(tableModel);

        configureWindow();
        add(createHeaderPanel(), BorderLayout.NORTH);
        add(createMainPanel(), BorderLayout.CENTER);
        add(createButtonPanel(), BorderLayout.SOUTH);
    }

    /**
     * Starts the Swing application on the event dispatch thread.
     *
     * @param args command-line arguments, not used
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                setModernLookAndFeel();
                new StudentGradeTrackerGUI().setVisible(true);
            }
        });
    }

    /**
     * Uses the operating system look and feel when available.
     */
    private static void setModernLookAndFeel() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            // The default Swing look and feel is used if the system theme is unavailable.
        }
    }

    /**
     * Sets frame-level layout and styling.
     */
    private void configureWindow() {
        setTitle("Student Grade Tracker System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1120, 700);
        setMinimumSize(new Dimension(980, 620));
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(16, 16));
        getContentPane().setBackground(new Color(244, 247, 251));
    }

    /**
     * Creates the dashboard title area.
     *
     * @return header panel
     */
    private JPanel createHeaderPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(26, 54, 93));
        panel.setBorder(new EmptyBorder(22, 28, 22, 28));

        JLabel titleLabel = new JLabel("Student Grade Tracker System");
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));

        JLabel subtitleLabel = new JLabel("Student Management | Grade Calculation | Class Report");
        subtitleLabel.setForeground(new Color(214, 226, 242));
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        panel.add(titleLabel, BorderLayout.NORTH);
        panel.add(subtitleLabel, BorderLayout.SOUTH);
        return panel;
    }

    /**
     * Creates the center area containing the input form and records table.
     *
     * @return main panel
     */
    private JPanel createMainPanel() {
        JPanel panel = new JPanel(new BorderLayout(16, 16));
        panel.setOpaque(false);
        panel.setBorder(new EmptyBorder(0, 22, 0, 22));

        panel.add(createInputPanel(), BorderLayout.NORTH);
        panel.add(createTablePanel(), BorderLayout.CENTER);
        return panel;
    }

    /**
     * Creates the student input form.
     *
     * @return input panel
     */
    private JPanel createInputPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(218, 225, 235)),
                new EmptyBorder(18, 18, 18, 18)));

        addFormField(panel, "Student ID", studentIdField, 0, 0);
        addFormField(panel, "Student Name", studentNameField, 2, 0);
        addFormField(panel, "Java Marks", javaMarksField, 0, 1);
        addFormField(panel, "Python Marks", pythonMarksField, 2, 1);
        addFormField(panel, "DBMS Marks", dbmsMarksField, 4, 1);
        addFormField(panel, "Operating Systems Marks", operatingSystemsMarksField, 0, 2);
        addFormField(panel, "Mathematics Marks", mathematicsMarksField, 2, 2);

        return panel;
    }

    /**
     * Adds one labeled text field to the form grid.
     *
     * @param panel target panel
     * @param labelText label text
     * @param textField field to add
     * @param gridX label column
     * @param gridY row
     */
    private void addFormField(JPanel panel, String labelText, JTextField textField, int gridX, int gridY) {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(7, 8, 7, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel label = new JLabel(labelText);
        label.setFont(new Font("Segoe UI", Font.BOLD, 13));
        label.setForeground(new Color(45, 55, 72));

        textField.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        textField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(196, 205, 218)),
                new EmptyBorder(7, 8, 7, 8)));

        gbc.gridx = gridX;
        gbc.gridy = gridY;
        gbc.weightx = 0;
        panel.add(label, gbc);

        gbc.gridx = gridX + 1;
        gbc.gridy = gridY;
        gbc.weightx = 1;
        panel.add(textField, gbc);
    }

    /**
     * Creates the table panel used to display student records.
     *
     * @return table panel
     */
    private JPanel createTablePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(218, 225, 235)),
                new EmptyBorder(12, 12, 12, 12)));

        studentTable.setRowHeight(30);
        studentTable.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        studentTable.setGridColor(new Color(230, 235, 242));
        studentTable.setSelectionBackground(new Color(203, 226, 250));
        studentTable.setSelectionForeground(new Color(30, 41, 59));
        studentTable.setFillsViewportHeight(true);

        JTableHeader header = studentTable.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 13));
        header.setBackground(new Color(230, 236, 245));
        header.setForeground(new Color(30, 41, 59));

        JScrollPane scrollPane = new JScrollPane(studentTable);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(218, 225, 235)));

        panel.add(scrollPane, BorderLayout.CENTER);
        return panel;
    }

    /**
     * Creates the bottom action bar.
     *
     * @return button panel
     */
    private JPanel createButtonPanel() {
        JPanel panel = new JPanel();
        panel.setOpaque(false);
        panel.setBorder(new EmptyBorder(0, 22, 22, 22));

        JButton addButton = createActionButton("Add Student");
        JButton displayButton = createActionButton("Display Students");
        JButton reportButton = createActionButton("Generate Report");
        JButton clearButton = createActionButton("Clear Fields");
        JButton exitButton = createActionButton("Exit");

        addButton.addActionListener(e -> addStudent());
        displayButton.addActionListener(e -> displayStudents());
        reportButton.addActionListener(e -> generateReport());
        clearButton.addActionListener(e -> clearFields());
        exitButton.addActionListener(e -> exitApplication());

        panel.add(addButton);
        panel.add(displayButton);
        panel.add(reportButton);
        panel.add(clearButton);
        panel.add(exitButton);
        return panel;
    }

    /**
     * Creates a styled dashboard button.
     *
     * @param text button text
     * @return styled button
     */
    private JButton createActionButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 13));
        button.setFocusPainted(false);
        button.setBackground(new Color(37, 99, 159));
        button.setForeground(Color.WHITE);
        button.setBorder(new EmptyBorder(10, 18, 10, 18));
        return button;
    }

    /**
     * Creates the non-editable table model with required columns.
     *
     * @return table model
     */
    private DefaultTableModel createTableModel() {
        String[] columns = {
            "Student ID",
            "Name",
            "Java",
            "Python",
            "DBMS",
            "Operating Systems",
            "Mathematics",
            "Average",
            "Grade"
        };

        return new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
    }

    /**
     * Validates form data, creates a Student object, and stores it in the ArrayList.
     */
    private void addStudent() {
        String studentId = studentIdField.getText().trim();
        String studentName = studentNameField.getText().trim();

        if (studentId.isEmpty()) {
            showError("Student ID cannot be empty.");
            studentIdField.requestFocus();
            return;
        }

        if (studentName.isEmpty()) {
            showError("Student Name cannot be empty.");
            studentNameField.requestFocus();
            return;
        }

        Double javaMarks = parseMarks(javaMarksField, "Java Marks");
        Double pythonMarks = parseMarks(pythonMarksField, "Python Marks");
        Double dbmsMarks = parseMarks(dbmsMarksField, "DBMS Marks");
        Double operatingSystemsMarks = parseMarks(operatingSystemsMarksField, "Operating Systems Marks");
        Double mathematicsMarks = parseMarks(mathematicsMarksField, "Mathematics Marks");

        if (javaMarks == null || pythonMarks == null || dbmsMarks == null
                || operatingSystemsMarks == null || mathematicsMarks == null) {
            return;
        }

        Student student = new Student(studentId, studentName, javaMarks, pythonMarks, dbmsMarks,
                operatingSystemsMarks, mathematicsMarks);
        gradeTracker.addStudent(student);
        addStudentToTable(student);
        clearFields();

        JOptionPane.showMessageDialog(this, "Student added successfully.", "Success",
                JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Parses and validates a marks field.
     *
     * @param field field containing marks
     * @param fieldName display name for error messages
     * @return valid marks value, or null if validation fails
     */
    private Double parseMarks(JTextField field, String fieldName) {
        String marksText = field.getText().trim();

        if (marksText.isEmpty()) {
            showError(fieldName + " cannot be empty.");
            field.requestFocus();
            return null;
        }

        double marks;
        try {
            marks = Double.parseDouble(marksText);
        } catch (NumberFormatException e) {
            showError(fieldName + " must be numeric.");
            field.requestFocus();
            return null;
        }

        if (marks < 0 || marks > 100) {
            showError(fieldName + " must be between 0 and 100.");
            field.requestFocus();
            return null;
        }
        return marks;
    }

    /**
     * Displays a validation error dialog.
     *
     * @param message error message
     */
    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Validation Error", JOptionPane.ERROR_MESSAGE);
    }

    /**
     * Reloads the JTable from records stored in GradeTracker.
     */
    private void displayStudents() {
        tableModel.setRowCount(0);
        List<Student> students = gradeTracker.getStudents();

        for (Student student : students) {
            addStudentToTable(student);
        }

        if (students.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No student records available.", "Information",
                    JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, "Student records displayed successfully.", "Information",
                    JOptionPane.INFORMATION_MESSAGE);
        }
    }

    /**
     * Adds one student record to the JTable.
     *
     * @param student student record to display
     */
    private void addStudentToTable(Student student) {
        Object[] row = {
            student.getStudentId(),
            student.getStudentName(),
            MARKS_FORMAT.format(student.getJavaMarks()),
            MARKS_FORMAT.format(student.getPythonMarks()),
            MARKS_FORMAT.format(student.getDbmsMarks()),
            MARKS_FORMAT.format(student.getOperatingSystemsMarks()),
            MARKS_FORMAT.format(student.getMathematicsMarks()),
            MARKS_FORMAT.format(student.getAverageMarks()),
            student.getGrade()
        };
        tableModel.addRow(row);
    }

    /**
     * Generates and displays the complete class summary report.
     */
    private void generateReport() {
        if (gradeTracker.getTotalStudents() == 0) {
            JOptionPane.showMessageDialog(this, "No student records available for report generation.",
                    "Summary Report", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        Student highest = gradeTracker.getHighestScoringStudent();
        Student lowest = gradeTracker.getLowestScoringStudent();

        String report = "Student Grade Summary Report\n"
                + "----------------------------------------\n"
                + "Total Students: " + gradeTracker.getTotalStudents() + "\n"
                + "Overall Class Average: " + MARKS_FORMAT.format(gradeTracker.getOverallClassAverage()) + "\n\n"
                + "Highest Scoring Student: " + highest.getStudentName()
                + " (" + highest.getStudentId() + ") - "
                + MARKS_FORMAT.format(highest.getAverageMarks()) + " [" + highest.getGrade() + "]\n"
                + "Lowest Scoring Student: " + lowest.getStudentName()
                + " (" + lowest.getStudentId() + ") - "
                + MARKS_FORMAT.format(lowest.getAverageMarks()) + " [" + lowest.getGrade() + "]\n\n"
                + "Subject-wise Average\n"
                + "----------------------------------------\n"
                + "Java: " + MARKS_FORMAT.format(gradeTracker.getJavaAverage()) + "\n"
                + "Python: " + MARKS_FORMAT.format(gradeTracker.getPythonAverage()) + "\n"
                + "DBMS: " + MARKS_FORMAT.format(gradeTracker.getDbmsAverage()) + "\n"
                + "Operating Systems: " + MARKS_FORMAT.format(gradeTracker.getOperatingSystemsAverage()) + "\n"
                + "Mathematics: " + MARKS_FORMAT.format(gradeTracker.getMathematicsAverage());

        JOptionPane.showMessageDialog(this, report, "Summary Report", JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Clears all input fields and returns focus to Student ID.
     */
    private void clearFields() {
        studentIdField.setText("");
        studentNameField.setText("");
        javaMarksField.setText("");
        pythonMarksField.setText("");
        dbmsMarksField.setText("");
        operatingSystemsMarksField.setText("");
        mathematicsMarksField.setText("");
        studentIdField.requestFocus();
    }

    /**
     * Confirms before closing the application.
     */
    private void exitApplication() {
        int choice = JOptionPane.showConfirmDialog(this, "Are you sure you want to exit?",
                "Exit Application", JOptionPane.YES_NO_OPTION);

        if (choice == JOptionPane.YES_OPTION) {
            dispose();
            System.exit(0);
        }
    }
}
