package Backend;

import java.util.ArrayList;

public class GPA {

    public GPA() {}

    public static boolean isValidGrade(int grade) {
        return grade >= 0 && grade <= 100;
    }

    public static double convertGradeToPoints(int grade) {
        if (!isValidGrade(grade)) {
            return -1.0;
        }

        if (grade >= 90) return 4.3;
        if (grade >= 80) return 4.0;
        if (grade >= 75) return 3.7;
        if (grade >= 70) return 3.3;
        if (grade >= 65) return 3.0;
        if (grade >= 60) return 2.7;
        if (grade >= 55) return 2.3;
        if (grade >= 50) return 2.0;
        if (grade >= 40) return 1.0;

        return 0.0;
    }

    public static double calculateGPA(Student student) {
        if (student == null) {
            return 0.0;
        }

        ArrayList<Enrollment> enrollments = student.getEnrollments();

        if (enrollments == null || enrollments.isEmpty()) {
            return 0.0;
        }

        double totalPoints = 0.0;
        int countedCourses = 0;

        for (Enrollment e : enrollments) {
            if (e == null) {
                continue;
            }

            int grade = e.getGrade();

            if (!isValidGrade(grade)) {
                continue;
            }

            double points = convertGradeToPoints(grade);

            if (points >= 0.0) {
                totalPoints += points;
                countedCourses++;
            }
        }

        if (countedCourses == 0) {
            return 0.0;
        }

        return totalPoints / countedCourses;
    }

    public static String determineGraduationStatus(Student student) {
        if (student == null) {
            return "Not Eligible";
        }

        double gpa = calculateGPA(student);

        if (gpa >= 2.0) {
            return "Eligible to Graduate";
        }

        return "Not Eligible";
    }

    public static String determineAcademicHonors(Student student) {
        if (student == null) {
            return "No Honors";
        }

        double gpa = calculateGPA(student);

        if (gpa >= 3.6) {
            return "First Class Honors";
        } else if (gpa >= 3.0) {
            return "Second Class Honors";
        } else if (gpa >= 2.0) {
            return "Pass";
        }

        return "No Honors";
    }
}