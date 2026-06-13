# Student Grade Tracker System

A professional Java Swing application for storing student subject marks, calculating averages and grades, displaying records in a JTable, and generating a class summary report.

## Source Files

- `Student.java` - OOP model class for one student record.
- `GradeTracker.java` - stores `ArrayList<Student>` records and calculates report statistics.
- `StudentGradeTrackerGUI.java` - Swing GUI, event handling, validation, JTable display, and report dialogs.

## Features

- Student ID and Student Name fields.
- Subject marks fields for Java, Python, DBMS, Operating Systems, and Mathematics.
- Input validation for empty fields, non-numeric marks, and marks outside 0 to 100.
- Automatic average and grade calculation.
- Grade criteria:
  - `A+` : 90-100
  - `A` : 80-89
  - `B` : 70-79
  - `C` : 60-69
  - `D` : 50-59
  - `F` : Below 50
- JTable display with all required student columns.
- Summary report with total students, overall class average, highest scoring student, lowest scoring student, and subject-wise averages.
- JOptionPane dialogs for success messages, validation errors, and report display.
- Professional dashboard-style Swing layout using BorderLayout, JPanel, JTable, and JScrollPane.

## Compile And Run From Command Line

Open a terminal in this folder and run:

```powershell
javac Student.java GradeTracker.java StudentGradeTrackerGUI.java
java StudentGradeTrackerGUI
```

## Run In NetBeans

1. Create a new Java Application project.
2. Copy `Student.java`, `GradeTracker.java`, and `StudentGradeTrackerGUI.java` into the `src` folder.
3. Keep the files in the default package, or add your own package statement to all three files.
4. Set `StudentGradeTrackerGUI` as the main class.
5. Run the project.

## Run In Eclipse

1. Create a new Java Project.
2. Right-click `src` and choose `New > Class` for each of the three classes, or copy the files into `src`.
3. Keep all three files in the same package.
4. Open `StudentGradeTrackerGUI.java`.
5. Click `Run`.

## Run In IntelliJ IDEA

1. Create a new Java project.
2. Copy the three `.java` files into the `src` folder.
3. Keep all three files in the same package.
4. Open `StudentGradeTrackerGUI.java`.
5. Click the green run button beside the `main` method.
