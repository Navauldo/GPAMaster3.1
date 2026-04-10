package Backend;

import java.io.*;
import java.util.ArrayList;

public class FileUpdate {

    public FileUpdate() {}

    public static boolean saveSystem(String filePath) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(filePath))) {

            // ADMINS
            writer.println("ADMINS");
            for (Admin admin : AdminControl.getAllAdmins()) {
                writer.println(admin.getId() + "|" + admin.getName() + "|" + admin.getPassword());
            }

            // TEACHERS
            writer.println("TEACHERS");
            for (Teacher teacher : TeacherControl.getAllTeachers()) {
                writer.println(teacher.getId() + "|" + teacher.getName() + "|" + teacher.getPassword());
            }

            // STUDENTS
            writer.println("STUDENTS");
            for (Student student : StudentControl.getEStudents()) {
                writer.println(student.getId() + "|" + student.getName() + "|" + student.getPassword());
            }

            // COURSES
            writer.println("COURSES");
            for (Course course : CourseControl.getAllCourses()) {
                writer.println(course.getCCode() + "|" + course.getCName());
            }

            // TEACHERCOURSES
            writer.println("TEACHERCOURSES");
            for (Teacher teacher : TeacherControl.getAllTeachers()) {
                for (Course course : teacher.getCoursesTaught()) {
                    writer.println(teacher.getId() + "|" + course.getCCode());
                }
            }

            // ENROLLMENTS
            writer.println("ENROLLMENTS");
            for (Student student : StudentControl.getEStudents()) {
                for (Enrollment enrollment : student.getEnrollments()) {
                    writer.println(
                            student.getId() + "|" +
                                    enrollment.getCourse().getCCode() + "|" +
                                    enrollment.getGrade()
                    );
                }
            }

            return true;

        } catch (IOException e) {
            System.out.println("Error saving system: " + e.getMessage());
            return false;
        }
    }

    public static boolean loadSystem(String filePath) {
        File file = new File(filePath);

        if (!file.exists()) {
            System.out.println("File does not exist.");
            return false;
        }

        ArrayList<String> adminLines = new ArrayList<>();
        ArrayList<String> teacherLines = new ArrayList<>();
        ArrayList<String> studentLines = new ArrayList<>();
        ArrayList<String> courseLines = new ArrayList<>();
        ArrayList<String> teacherCourseLines = new ArrayList<>();
        ArrayList<String> enrollmentLines = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String section = "";
            String line;

            while ((line = reader.readLine()) != null) {
                if (line.equals("ADMINS") || line.equals("TEACHERS") || line.equals("STUDENTS")
                        || line.equals("COURSES") || line.equals("TEACHERCOURSES")
                        || line.equals("ENROLLMENTS")) {
                    section = line;
                    continue;
                }

                if (line.trim().isEmpty()) {
                    continue;
                }

                switch (section) {
                    case "ADMINS":
                        adminLines.add(line);
                        break;
                    case "TEACHERS":
                        teacherLines.add(line);
                        break;
                    case "STUDENTS":
                        studentLines.add(line);
                        break;
                    case "COURSES":
                        courseLines.add(line);
                        break;
                    case "TEACHERCOURSES":
                        teacherCourseLines.add(line);
                        break;
                    case "ENROLLMENTS":
                        enrollmentLines.add(line);
                        break;
                }
            }

            // clear old data first
            AdminControl.clearAdmins();
            TeacherControl.clearTeachers();
            StudentControl.clearStudents();
            CourseControl.clearCourses();

            // rebuild base objects first

            // ADMINS
            for (String s : adminLines) {
                String[] parts = s.split("\\|");
                int id = Integer.parseInt(parts[0]);
                String name = parts[1];
                String password = parts[2];

                AdminControl.addAdmin(new Admin(id, name, password));
            }

            // TEACHERS
            for (String s : teacherLines) {
                String[] parts = s.split("\\|");
                int id = Integer.parseInt(parts[0]);
                String name = parts[1];
                String password = parts[2];

                TeacherControl.addTeacher(new Teacher(id, name, password));
            }

            // STUDENTS
            for (String s : studentLines) {
                String[] parts = s.split("\\|");
                int id = Integer.parseInt(parts[0]);
                String name = parts[1];
                String password = parts[2];

                StudentControl.addStudent(new Student(id, name, password));
            }

            // COURSES
            for (String s : courseLines) {
                String[] parts = s.split("\\|");
                String code = parts[0];
                String name = parts[1];

                CourseControl.addCourse(new Course(code, name));
            }

            // rebuild teacher-course relationships
            for (String s : teacherCourseLines) {
                String[] parts = s.split("\\|");
                int teacherId = Integer.parseInt(parts[0]);
                String courseCode = parts[1];

                Teacher teacher = TeacherControl.findTeacherById(teacherId);
                Course course = CourseControl.findCourseByCode(courseCode);

                if (teacher != null && course != null) {
                    teacher.addCourse(course);
                }
            }

            // rebuild enrollments
            for (String s : enrollmentLines) {
                String[] parts = s.split("\\|");
                int studentId = Integer.parseInt(parts[0]);
                String courseCode = parts[1];
                int grade = Integer.parseInt(parts[2]);

                StudentControl.enrollStudentInCourse(studentId, courseCode, grade);
            }

            return true;

        } catch (IOException e) {
            System.out.println("Error loading system: " + e.getMessage());
            return false;
        } catch (Exception e) {
            System.out.println("Error rebuilding system data: " + e.getMessage());
            return false;
        }
    }
}