/**
 * Represents a single student record in the Student Grade Tracker System.
 * This class keeps student data and grade calculations separate from the GUI.
 */
public class Student {
    private final String studentId;
    private final String studentName;
    private final double javaMarks;
    private final double pythonMarks;
    private final double dbmsMarks;
    private final double operatingSystemsMarks;
    private final double mathematicsMarks;

    /**
     * Creates a complete student record with marks for all subjects.
     *
     * @param studentId               unique student ID
     * @param studentName             student's full name
     * @param javaMarks               Java marks from 0 to 100
     * @param pythonMarks             Python marks from 0 to 100
     * @param dbmsMarks               DBMS marks from 0 to 100
     * @param operatingSystemsMarks   Operating Systems marks from 0 to 100
     * @param mathematicsMarks        Mathematics marks from 0 to 100
     */
    public Student(String studentId, String studentName, double javaMarks, double pythonMarks,
            double dbmsMarks, double operatingSystemsMarks, double mathematicsMarks) {
        this.studentId = studentId;
        this.studentName = studentName;
        this.javaMarks = javaMarks;
        this.pythonMarks = pythonMarks;
        this.dbmsMarks = dbmsMarks;
        this.operatingSystemsMarks = operatingSystemsMarks;
        this.mathematicsMarks = mathematicsMarks;
    }

    public String getStudentId() {
        return studentId;
    }

    public String getStudentName() {
        return studentName;
    }

    public double getJavaMarks() {
        return javaMarks;
    }

    public double getPythonMarks() {
        return pythonMarks;
    }

    public double getDbmsMarks() {
        return dbmsMarks;
    }

    public double getOperatingSystemsMarks() {
        return operatingSystemsMarks;
    }

    public double getMathematicsMarks() {
        return mathematicsMarks;
    }

    /**
     * Calculates total marks across all five subjects.
     *
     * @return total marks
     */
    public double getTotalMarks() {
        return javaMarks + pythonMarks + dbmsMarks + operatingSystemsMarks + mathematicsMarks;
    }

    /**
     * Calculates average marks across all five subjects.
     *
     * @return average marks
     */
    public double getAverageMarks() {
        return getTotalMarks() / 5.0;
    }

    /**
     * Converts the average marks into the required letter grade.
     *
     * @return grade based on average marks
     */
    public String getGrade() {
        double average = getAverageMarks();

        if (average >= 90) {
            return "A+";
        } else if (average >= 80) {
            return "A";
        } else if (average >= 70) {
            return "B";
        } else if (average >= 60) {
            return "C";
        } else if (average >= 50) {
            return "D";
        }
        return "F";
    }
}
