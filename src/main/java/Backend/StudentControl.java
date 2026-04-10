package Backend;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class StudentControl {
    static private ArrayList<Student> estudents = new ArrayList<>();

    // CONSTRUCTOR
    public StudentControl() {}

    // METHODS BELOW
    public static boolean addStudent(Student s) {
        if (s == null) {
            return false;
        }

        if (!isStudentIdUnique(s.getId())) {
            return false;
        }

        estudents.add(s);
        return true;
    }

    public static ArrayList<Student> getEStudents() {
        return StudentControl.estudents;
    }

    public static Student getStudent(int id) {
        for (Student st : estudents) {
            if (st.getId() == id) {
                return st;
            }
        }
        return null;
    }

    public static boolean studentExists(int id) {
        return getStudent(id) != null;
    }

    public static boolean isStudentIdUnique(int id) {
        return getStudent(id) == null;
    }

    public static boolean editStudent(int id, int newid, String name, String password) {
        Student student = StudentControl.getStudent(id);

        if (student == null) {
            return false;
        }

        // only block newid if it belongs to a different student
        if (id != newid && !isStudentIdUnique(newid)) {
            return false;
        }

        student.setId(newid);
        student.setName(name);
        student.setPassword(password);
        return true;
    }

    public static boolean deleteStudent(int id) {
        Student todeletestudent = StudentControl.getStudent(id);

        if (todeletestudent == null) {
            return false;
        }

        // remove this student's enrollments from all related courses first
        ArrayList<Enrollment> studentEnrollmentsCopy = new ArrayList<>(todeletestudent.getEnrollments());

        for (Enrollment e : studentEnrollmentsCopy) {
            if (e == null) {
                continue;
            }

            Course course = e.getCourse();
            if (course != null) {
                course.getEnrollments().remove(e);
            }
        }

        // clear student's own enrollment list
        todeletestudent.getEnrollments().clear();

        // finally remove student from master student list
        StudentControl.getEStudents().remove(todeletestudent);
        return true;
    }

    // METHODS TO CREATE ENROLLMENT BELOW
    // A FACADE METHOD IN ADMIN WILL CALL ONE OF THESE TWO DEPENDING ON WHETHER GRADE EXISTS OR NOT
    public static boolean enrollStudentInCourse(int studentId, String courseCode) {
        Student student = getStudent(studentId);
        if (student == null) {
            System.out.println("Student does not exist.");
            return false;
        }

        Course course = CourseControl.findCourseByCode(courseCode);
        if (course == null) {
            System.out.println("Course does not exist.");
            return false;
        }

        if (student.getEnrollmentForCourse(course) != null) {
            System.out.println("Student is already enrolled in this course.");
            return false;
        }

        Enrollment enrollment = new Enrollment(student, course);

        student.addEnrollment(enrollment);
        course.addEnrollment(enrollment);

        return true;
    }

    public static boolean enrollStudentInCourse(int studentId, String courseCode, int grade) {
        Student student = getStudent(studentId);
        if (student == null) {
            System.out.println("Student does not exist.");
            return false;
        }

        Course course = CourseControl.findCourseByCode(courseCode);
        if (course == null) {
            System.out.println("Course does not exist.");
            return false;
        }

        if (!GPA.isValidGrade(grade)) {
            System.out.println("Invalid grade.");
            return false;
        }

        if (student.getEnrollmentForCourse(course) != null) {
            System.out.println("Student is already enrolled in this course.");
            return false;
        }

        Enrollment enrollment = new Enrollment(student, course, grade);

        student.addEnrollment(enrollment);
        course.addEnrollment(enrollment);

        return true;
    }

    // GPA / STATUS / HONORS WIRING BELOW
    public static double getStudentGPA(int studentId) {
        Student student = getStudent(studentId);

        if (student == null) {
            return 0.0;
        }

        return GPA.calculateGPA(student);
    }

    public static String getStudentGraduationStatus(int studentId) {
        Student student = getStudent(studentId);

        if (student == null) {
            return "Not Eligible";
        }

        return GPA.determineGraduationStatus(student);
    }

    public static String getStudentAcademicHonors(int studentId) {
        Student student = getStudent(studentId);

        if (student == null) {
            return "No Honors";
        }

        return GPA.determineAcademicHonors(student);
    }

    //SORTING METHODS
    public static ArrayList<Student> sortStudentsByName() {
        ArrayList<Student> sortedStudents = new ArrayList<>(estudents);

        Collections.sort(sortedStudents, Comparator.comparing(Student::getName));

        return sortedStudents;
    }

    public static ArrayList<Student> sortStudentsById() {
        ArrayList<Student> sortedStudents = new ArrayList<>(estudents);

        Collections.sort(sortedStudents, Comparator.comparingInt(Student::getId));

        return sortedStudents;
    }

    public static ArrayList<Student> sortStudentsByGPA() {
        ArrayList<Student> sortedStudents = new ArrayList<>(estudents);

        Collections.sort(sortedStudents, Comparator.comparingDouble(GPA::calculateGPA).reversed());

        return sortedStudents;
    }

    public static ArrayList<Student> filterGraduatingStudents() {
        ArrayList<Student> graduatingStudents = new ArrayList<>();

        for (Student student : estudents) {
            if (GPA.determineGraduationStatus(student).equals("Eligible to Graduate")) {
                graduatingStudents.add(student);
            }
        }

        return graduatingStudents;
    }

    //FOR FILE CLASS BELOW
    public static void clearStudents() {
        estudents.clear();
    }

    public static boolean loadSystem(String filePath){
        return FileUpdate.loadSystem(filePath);
    }
}