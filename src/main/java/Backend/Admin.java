package Backend;

import java.util.ArrayList;

public class Admin extends User {

    public Admin(int id, String name, String password){
        super(id, name, password);
    }

    // ADMIN MANAGEMENT OF OTHER ADMINS
    public boolean addAdmin(Admin admin){
        return AdminControl.addAdmin(admin);
    }

    public boolean editAdmin(int adminId, String newName, String newPassword){
        return AdminControl.editAdmin(adminId, newName, newPassword);
    }

    public boolean deleteAdmin(int adminId){
        return AdminControl.deleteAdmin(adminId);
    }

    // STUDENT MANAGEMENT
    public boolean addStudent(Student student){
        return StudentControl.addStudent(student);
    }

    public boolean editStudent(int id, int newId, String name, String password){
        return StudentControl.editStudent(id, newId, name, password);
    }

    public boolean deleteStudent(int id){
        return StudentControl.deleteStudent(id);
    }

    // TEACHER MANAGEMENT
    public boolean addTeacher(Teacher teacher){
        return TeacherControl.addTeacher(teacher);
    }

    public boolean editTeacher(int teacherId, String newName, String newPassword){
        return TeacherControl.editTeacher(teacherId, newName, newPassword);
    }

    public boolean deleteTeacher(int teacherId){
        return TeacherControl.deleteTeacher(teacherId);
    }

    public boolean assignCourseToTeacher(int teacherId, String courseCode){
        return TeacherControl.assignCourseToTeacher(teacherId, courseCode);
    }

    public boolean removeCourseFromTeacher(int teacherId, String courseCode){
        return TeacherControl.removeCourseFromTeacher(teacherId, courseCode);
    }

    // COURSE MANAGEMENT
    public boolean addCourse(Course course){
        return CourseControl.addCourse(course);
    }

    public boolean editCourse(String courseCode, String newName){
        return CourseControl.editCourse(courseCode, newName);
    }

    public boolean deleteCourse(String courseCode){
        return CourseControl.deleteCourse(courseCode);
    }


    // VIEW METHODS
    public ArrayList<Student> viewAllStudents(){
        return StudentControl.getEStudents();
    }

    public ArrayList<Teacher> viewAllTeachers(){
        return TeacherControl.getAllTeachers();
    }

    public ArrayList<Course> viewAllCourses(){
        return CourseControl.getAllCourses();
    }

    public double viewStudentGPA(int studentId){
        return StudentControl.getStudentGPA(studentId);
    }

    public String viewStudentGraduationStatus(int studentId){
        return StudentControl.getStudentGraduationStatus(studentId);
    }

    public String viewStudentAcademicHonors(int studentId){
        return StudentControl.getStudentAcademicHonors(studentId);
    }

    //FACADE METHODS FOR ENROLLMENT BELOW
    public boolean enrollStudentInCourse(int studentId, String courseCode) {
        return StudentControl.enrollStudentInCourse(studentId, courseCode);
    }

    public boolean enrollStudentInCourse(int studentId, String courseCode, int grade) {
        return StudentControl.enrollStudentInCourse(studentId, courseCode, grade);
    }
    //////////////////////////////////////////////////////////////////////////////////////

    // PLACEHOLDERS FOR LATER
    public boolean saveSystem(String filePath){
        return FileUpdate.saveSystem(filePath);
    }

    public boolean loadSystem(String filePath){
        return FileUpdate.loadSystem(filePath);
    }
}