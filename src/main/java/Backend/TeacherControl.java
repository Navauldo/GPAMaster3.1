package Backend;

import java.util.ArrayList;

public class TeacherControl {
    static ArrayList<Teacher> eteachers = new ArrayList<>();

    public TeacherControl() {}

    public static boolean addTeacher(Teacher teacher) {
        if (teacher == null) {
            return false;
        }

        if (!isTeacherIdUnique(teacher.getId())) {
            return false;
        }

        eteachers.add(teacher);
        return true;
    }

    // edit and delete may want to have newid for editing field as well
    public static boolean editTeacher(int teacherId, String newName, String newPassword) {
        Teacher teacher = findTeacherById(teacherId);

        if (teacher == null) {
            System.out.println("Teacher does not exist.");
            return false;
        }

        teacher.setName(newName);
        teacher.setPassword(newPassword);
        return true;
    }

    public static boolean deleteTeacher(int teacherId) {
        Teacher teacher = findTeacherById(teacherId);

        if (teacher == null) {
            System.out.println("Teacher does not exist.");
            return false;
        }

        eteachers.remove(teacher);
        return true;
    }

    public static Teacher findTeacherById(int teacherId) {
        for (Teacher teacher : eteachers) {
            if (teacher.getId() == teacherId) {
                return teacher;
            }
        }
        return null;
    }

    public static boolean teacherExists(int teacherId) {
        return findTeacherById(teacherId) != null;
    }

    public static boolean isTeacherIdUnique(int teacherId) {
        return findTeacherById(teacherId) == null;
    }

    public static ArrayList<Teacher> getAllTeachers() {
        return eteachers;
    }

    public static ArrayList<Student> getTeacherStudents(int teacherId) {
        ArrayList<Student> StudentsTaught = new ArrayList<>();
        Teacher teacher = TeacherControl.findTeacherById(teacherId);

        if (teacher == null) {
            System.out.println("Teacher does not exist.");
            return StudentsTaught;
        }

        ArrayList<Course> coursesfind = teacher.getCoursesTaught();

        for (Course c : coursesfind) {
            for (Student s : c.getStudentsEnrolled()) {
                boolean alreadyAdded = false;

                for (Student existing : StudentsTaught) {
                    if (existing.getId() == s.getId()) {
                        alreadyAdded = true;
                        break;
                    }
                }

                if (!alreadyAdded) {
                    StudentsTaught.add(s);
                }
            }
        }

        return StudentsTaught;
    }

    public static boolean updateStudentGrade(int teacherId, int studentId, String courseCode, int newGrade) {
        Teacher teacher = findTeacherById(teacherId);
        if (teacher == null) {
            System.out.println("Teacher does not exist.");
            return false;
        }

        Student student = StudentControl.getStudent(studentId);
        if (student == null) {
            System.out.println("Student does not exist.");
            return false;
        }

        Course course = null;
        for (Course c : teacher.getCoursesTaught()) {
            if (c.getCCode().equals(courseCode)) {
                course = c;
                break;
            }
        }

        if (course == null) {
            System.out.println("Course does not exist for this teacher.");
            return false;
        }

        teacher.updateStudentGrade(student, course, newGrade);
        return true;
    }

    public static Student viewStudentAcademicInfo(int teacherId, int studentId) {
        Teacher teacher = findTeacherById(teacherId);
        if (teacher == null) {
            System.out.println("Teacher does not exist.");
            return null;
        }

        Student student = StudentControl.getStudent(studentId);
        if (student == null) {
            System.out.println("Student does not exist.");
            return null;
        }

        boolean taughtByTeacher = false;

        for (Course c : teacher.getCoursesTaught()) {
            for (Student s : c.getStudentsEnrolled()) {
                if (s.getId() == studentId) {
                    taughtByTeacher = true;
                    break;
                }
            }
            if (taughtByTeacher) {
                break;
            }
        }

        if (!taughtByTeacher) {
            System.out.println("This teacher does not teach this student.");
            return null;
        }

        return student;
    }

    //Add courses to teachers
    public static boolean assignCourseToTeacher(int teacherId, String courseCode) {
        Teacher teacher = findTeacherById(teacherId);
        if (teacher == null) {
            System.out.println("Teacher does not exist.");
            return false;
        }

        Course course = CourseControl.findCourseByCode(courseCode);
        if (course == null) {
            System.out.println("Course does not exist.");
            return false;
        }

        return teacher.addCourse(course);
    }

    public static boolean removeCourseFromTeacher(int teacherId, String courseCode) {
        Teacher teacher = findTeacherById(teacherId);
        if (teacher == null) {
            System.out.println("Teacher does not exist.");
            return false;
        }

        Course course = CourseControl.findCourseByCode(courseCode);
        if (course == null) {
            System.out.println("Course does not exist.");
            return false;
        }

        return teacher.removeCourse(course);
    }

    // FOR FILE CLASS BELOW
    public static void clearTeachers() {
        eteachers.clear();
    }

    public static boolean saveSystem(String filePath){
        return FileUpdate.saveSystem(filePath);
    }

    public static boolean loadSystem(String filePath){
        return FileUpdate.loadSystem(filePath);
    }
}