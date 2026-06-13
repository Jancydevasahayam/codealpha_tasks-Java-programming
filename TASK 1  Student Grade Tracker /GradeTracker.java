import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Manages student records and calculates class statistics.
 * The ArrayList is kept here so the GUI remains focused on presentation.
 */
public class GradeTracker {
    private final ArrayList<Student> students;

    /**
     * Creates an empty grade tracker.
     */
    public GradeTracker() {
        students = new ArrayList<Student>();
    }

    /**
     * Adds a new student record to the tracker.
     *
     * @param student student record to store
     */
    public void addStudent(Student student) {
        students.add(student);
    }

    /**
     * Returns all student records as a read-only list.
     *
     * @return unmodifiable list of students
     */
    public List<Student> getStudents() {
        return Collections.unmodifiableList(students);
    }

    /**
     * Counts all stored student records.
     *
     * @return total number of students
     */
    public int getTotalStudents() {
        return students.size();
    }

    /**
     * Calculates the overall class average using each student's average.
     *
     * @return overall class average, or 0 if no records exist
     */
    public double getOverallClassAverage() {
        if (students.isEmpty()) {
            return 0;
        }

        double total = 0;
        for (Student student : students) {
            total += student.getAverageMarks();
        }
        return total / students.size();
    }

    /**
     * Finds the student with the highest average marks.
     *
     * @return highest scoring student, or null if no records exist
     */
    public Student getHighestScoringStudent() {
        if (students.isEmpty()) {
            return null;
        }
        return Collections.max(students, Comparator.comparingDouble(Student::getAverageMarks));
    }

    /**
     * Finds the student with the lowest average marks.
     *
     * @return lowest scoring student, or null if no records exist
     */
    public Student getLowestScoringStudent() {
        if (students.isEmpty()) {
            return null;
        }
        return Collections.min(students, Comparator.comparingDouble(Student::getAverageMarks));
    }

    public double getJavaAverage() {
        return calculateSubjectAverage("java");
    }

    public double getPythonAverage() {
        return calculateSubjectAverage("python");
    }

    public double getDbmsAverage() {
        return calculateSubjectAverage("dbms");
    }

    public double getOperatingSystemsAverage() {
        return calculateSubjectAverage("operatingSystems");
    }

    public double getMathematicsAverage() {
        return calculateSubjectAverage("mathematics");
    }

    /**
     * Calculates the average for one subject.
     *
     * @param subject subject key used inside this controller
     * @return subject average, or 0 if no records exist
     */
    private double calculateSubjectAverage(String subject) {
        if (students.isEmpty()) {
            return 0;
        }

        double total = 0;
        for (Student student : students) {
            if ("java".equals(subject)) {
                total += student.getJavaMarks();
            } else if ("python".equals(subject)) {
                total += student.getPythonMarks();
            } else if ("dbms".equals(subject)) {
                total += student.getDbmsMarks();
            } else if ("operatingSystems".equals(subject)) {
                total += student.getOperatingSystemsMarks();
            } else if ("mathematics".equals(subject)) {
                total += student.getMathematicsMarks();
            }
        }
        return total / students.size();
    }
}
