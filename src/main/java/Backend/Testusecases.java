package Backend;

import java.util.ArrayList;

// Backend usage demonstration for GPA Master
// Covers:
// 1. CRUD for admin/teacher/student/course
// 2. Teacher-course assignment
// 3. Student enrollment
// 4. Grade updates
// 5. GPA / graduation status / honors
// 6. Sorting and filtering
// 7. Delete rules
// 8. File save/load persistence
public class Testusecases {
    public static void main(String[] args) {

        System.out.println("====================================");
        System.out.println(" GPA MASTER BACKEND TEST USE CASES ");
        System.out.println("====================================");

        // =========================
        // CREATE BASE OBJECTS
        // =========================
        Admin admin1 = new Admin(1, "Main Admin", "admin123");
        AdminControl.addAdmin(admin1);

        Teacher teacher1 = new Teacher(101, "Mr Brown", "teach101");
        Teacher teacher2 = new Teacher(102, "Ms Green", "teach102");

        Student student1 = new Student(201, "David", "stud201");
        Student student2 = new Student(202, "Anna", "stud202");
        Student student3 = new Student(203, "Mark", "stud203");

        Course course1 = new Course("COMP1", "Programming 1");
        Course course2 = new Course("MATH1", "Calculus 1");
        Course course3 = new Course("PHYS1", "Physics 1");

        // =========================
        // TEST 1: ADD / CRUD BASICS
        // =========================
        System.out.println("\n=== TEST 1: ADD / CRUD BASICS ===");

        System.out.println("Add teacher1: " + admin1.addTeacher(teacher1));
        System.out.println("Add teacher2: " + admin1.addTeacher(teacher2));
        System.out.println("Add duplicate teacher1: " + admin1.addTeacher(teacher1));

        System.out.println("Add student1: " + admin1.addStudent(student1));
        System.out.println("Add student2: " + admin1.addStudent(student2));
        System.out.println("Add student3: " + admin1.addStudent(student3));

        System.out.println("Add course1: " + admin1.addCourse(course1));
        System.out.println("Add course2: " + admin1.addCourse(course2));
        System.out.println("Add course3: " + admin1.addCourse(course3));

        System.out.println("\nTeachers:");
        for (Teacher t : admin1.viewAllTeachers()) {
            System.out.println(t);
        }

        System.out.println("\nStudents:");
        for (Student s : admin1.viewAllStudents()) {
            System.out.println(s.getId() + " - " + s.getName());
        }

        System.out.println("\nCourses:");
        for (Course c : admin1.viewAllCourses()) {
            System.out.println(c);
        }

        // =========================
        // TEST 2: EDIT
        // =========================
        System.out.println("\n=== TEST 2: EDIT ===");

        System.out.println("Edit teacher1: " + admin1.editTeacher(101, "Mr Smith", "newTeach101"));
        System.out.println("Edit student1: " + admin1.editStudent(201, 211, "David McLish", "newStud201"));
        System.out.println("Edit course1: " + admin1.editCourse("COMP1", "Introduction to Programming"));

        System.out.println("\nAfter edits:");
        System.out.println("Teacher 101 -> " + TeacherControl.findTeacherById(101));
        System.out.println("Student 211 -> " + StudentControl.getStudent(211).getName());
        System.out.println("Course COMP1 -> " + CourseControl.findCourseByCode("COMP1"));

        // =========================
        // TEST 3: ASSIGN COURSES TO TEACHERS
        // =========================
        System.out.println("\n=== TEST 3: ASSIGN COURSES TO TEACHERS ===");

        teacher1.addCourse(course1);
        teacher1.addCourse(course2);
        teacher2.addCourse(course3);

        System.out.println("Teacher 101 courses:");
        for (Course c : teacher1.getCoursesTaught()) {
            System.out.println(c);
        }

        System.out.println("Teacher 102 courses:");
        for (Course c : teacher2.getCoursesTaught()) {
            System.out.println(c);
        }

        // =========================
        // TEST 4: ENROLL STUDENTS
        // =========================
        System.out.println("\n=== TEST 4: ENROLL STUDENTS ===");

        System.out.println("Enroll David in COMP1 with grade 85: " + admin1.enrollStudentInCourse(211, "COMP1", 85));
        System.out.println("Enroll David in MATH1 with grade 72: " + admin1.enrollStudentInCourse(211, "MATH1", 72));
        System.out.println("Enroll Anna in COMP1 with grade 65: " + admin1.enrollStudentInCourse(202, "COMP1", 65));
        System.out.println("Enroll Anna in PHYS1 with grade 90: " + admin1.enrollStudentInCourse(202, "PHYS1", 90));
        System.out.println("Enroll Mark in PHYS1 without grade: " + admin1.enrollStudentInCourse(203, "PHYS1"));
        System.out.println("Try duplicate enroll David in COMP1: " + admin1.enrollStudentInCourse(211, "COMP1", 88));

        System.out.println("\nDavid record:");
        System.out.println(StudentControl.getStudent(211));

        System.out.println("Anna record:");
        System.out.println(StudentControl.getStudent(202));

        System.out.println("Mark record:");
        System.out.println(StudentControl.getStudent(203));

        // =========================
        // TEST 5: TEACHER VIEW OWN STUDENTS
        // =========================
        System.out.println("\n=== TEST 5: TEACHER VIEW OWN STUDENTS ===");

        ArrayList<Student> teacher1Students = TeacherControl.getTeacherStudents(101);
        System.out.println("Teacher 101 students:");
        for (Student s : teacher1Students) {
            System.out.println(s.getId() + " - " + s.getName());
        }

        ArrayList<Student> teacher2Students = TeacherControl.getTeacherStudents(102);
        System.out.println("Teacher 102 students:");
        for (Student s : teacher2Students) {
            System.out.println(s.getId() + " - " + s.getName());
        }

        // =========================
        // TEST 6: TEACHER INPUT / UPDATE GRADES
        // =========================
        System.out.println("\n=== TEST 6: TEACHER INPUT / UPDATE GRADES ===");

        System.out.println("Teacher 101 updates David COMP1 to 88: " +
                TeacherControl.updateStudentGrade(101, 211, "COMP1", 88));

        System.out.println("Teacher 101 updates Anna PHYS1 (should fail): " +
                TeacherControl.updateStudentGrade(101, 202, "PHYS1", 77));

        System.out.println("Teacher 102 updates Mark PHYS1 to 79: " +
                TeacherControl.updateStudentGrade(102, 203, "PHYS1", 79));

        System.out.println("\nDavid updated record:");
        System.out.println(StudentControl.getStudent(211));

        System.out.println("Mark updated record:");
        System.out.println(StudentControl.getStudent(203));

        // =========================
        // TEST 7: GPA / STATUS / HONORS
        // =========================
        System.out.println("\n=== TEST 7: GPA / STATUS / HONORS ===");

        int[] studentIds = {211, 202, 203};

        for (int id : studentIds) {
            Student s = StudentControl.getStudent(id);
            System.out.println("Student: " + s.getName());
            System.out.println("GPA: " + StudentControl.getStudentGPA(id));
            System.out.println("Graduation Status: " + StudentControl.getStudentGraduationStatus(id));
            System.out.println("Academic Honors: " + StudentControl.getStudentAcademicHonors(id));
            System.out.println();
        }

        // =========================
        // TEST 8: SORTING / FILTERING
        // =========================
        System.out.println("\n=== TEST 8: SORTING / FILTERING ===");

        System.out.println("Students sorted by name:");
        for (Student s : StudentControl.sortStudentsByName()) {
            System.out.println(s.getId() + " - " + s.getName());
        }

        System.out.println("\nStudents sorted by ID:");
        for (Student s : StudentControl.sortStudentsById()) {
            System.out.println(s.getId() + " - " + s.getName());
        }

        System.out.println("\nStudents sorted by GPA:");
        for (Student s : StudentControl.sortStudentsByGPA()) {
            System.out.println(s.getId() + " - " + s.getName() + " | GPA = " + GPA.calculateGPA(s));
        }

        System.out.println("\nGraduating students only:");
        for (Student s : StudentControl.filterGraduatingStudents()) {
            System.out.println(s.getId() + " - " + s.getName());
        }

        // =========================
        // TEST 9: BLOCK COURSE DELETE IF ENROLLED
        // =========================
        System.out.println("\n=== TEST 9: BLOCK COURSE DELETE IF ENROLLED ===");

        System.out.println("Try deleting enrolled course COMP1: " + admin1.deleteCourse("COMP1"));
        System.out.println("Try deleting enrolled course PHYS1: " + admin1.deleteCourse("PHYS1"));
        System.out.println("Try deleting enrolled course MATH1: " + admin1.deleteCourse("MATH1"));

        // =========================
        // TEST 10: DELETE STUDENT WITH ENROLLMENT CLEANUP
        // =========================
        System.out.println("\n=== TEST 10: DELETE STUDENT WITH ENROLLMENT CLEANUP ===");

        Course phys1Before = CourseControl.findCourseByCode("PHYS1");
        System.out.println("PHYS1 students BEFORE deleting Mark:");
        for (Student s : phys1Before.getStudentsEnrolled()) {
            System.out.println(s.getId() + " - " + s.getName());
        }

        System.out.println("Delete Mark (203): " + admin1.deleteStudent(203));

        Course phys1After = CourseControl.findCourseByCode("PHYS1");
        System.out.println("PHYS1 students AFTER deleting Mark:");
        for (Student s : phys1After.getStudentsEnrolled()) {
            System.out.println(s.getId() + " - " + s.getName());
        }

        System.out.println("Student 203 exists? " + StudentControl.studentExists(203));

        // =========================
        // TEST 11: FILE SAVE / LOAD
        // =========================
        System.out.println("\n=== TEST 11: FILE SAVE / LOAD ===");

        String filePath = "gpa_master_data.txt";

        System.out.println("Save system through Admin: " + admin1.saveSystem(filePath));

        AdminControl.clearAdmins();
        TeacherControl.clearTeachers();
        StudentControl.clearStudents();
        CourseControl.clearCourses();

        System.out.println("\nAfter clearing controls:");
        System.out.println("Admins: " + AdminControl.getAllAdmins().size());
        System.out.println("Teachers: " + TeacherControl.getAllTeachers().size());
        System.out.println("Students: " + StudentControl.getEStudents().size());
        System.out.println("Courses: " + CourseControl.getAllCourses().size());

        System.out.println("\nLoad system through FileUpdate: " + FileUpdate.loadSystem(filePath));

        System.out.println("\nAfter loading:");
        System.out.println("Admins: " + AdminControl.getAllAdmins().size());
        System.out.println("Teachers: " + TeacherControl.getAllTeachers().size());
        System.out.println("Students: " + StudentControl.getEStudents().size());
        System.out.println("Courses: " + CourseControl.getAllCourses().size());

        System.out.println("\nLoaded students:");
        for (Student s : StudentControl.getEStudents()) {
            System.out.println(s);
            System.out.println("GPA: " + GPA.calculateGPA(s));
            System.out.println("Status: " + GPA.determineGraduationStatus(s));
            System.out.println("Honors: " + GPA.determineAcademicHonors(s));
            System.out.println("-------------------------");
        }

        System.out.println("\n=== ALL TESTS FINISHED ===");
    }
}