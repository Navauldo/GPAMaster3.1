package Backend;

import java.util.ArrayList;

public class CourseControl {
    static ArrayList<Course> ecourses = new ArrayList<>();

    // CONSTRUCTOR, SINCE FUNCTIONS ARE STATIC WE DONT NEED CONSTRUCTOR REALLY...
    public CourseControl(){}

    public static Course findCourseByCode(String coursecode)
    {
        for (Course c : ecourses)
        {
            if (c.getCCode().equals(coursecode))
            {
                return c;
            }
        }
        return null;
    }

    public static ArrayList<Course> getAllCourses(){
        return ecourses;
    }

    public static boolean addCourse(Course course){
        if (course == null) {
            return false;
        }

        if (isCourseCodeUnique(course.getCCode()))
        {
            ecourses.add(course);
            return true;
        }
        else
        {
            System.out.println("Course code already exists.");
            return false;
        }
    }

    public static boolean editCourse(String coursecode, String newName)
    {
        Course course = findCourseByCode(coursecode);

        if (course == null)
        {
            System.out.println("Course does not exist.");
            return false;
        }

        course.setCName(newName);
        return true;
    }

    public static boolean deleteCourse(String coursecode)
    {
        Course course = findCourseByCode(coursecode);

        if (course == null)
        {
            System.out.println("Course does not exist.");
            return false;
        }

        if (!canDeleteCourse(coursecode))
        {
            System.out.println("Course cannot be deleted because students are still enrolled.");
            return false;
        }

        for (Teacher teacher : TeacherControl.getAllTeachers()) {
            teacher.removeCourse(course);
        }

        ecourses.remove(course);
        return true;
    }

    public static boolean isCourseCodeUnique(String coursecode)
    {
        return findCourseByCode(coursecode) == null;
    }

    public static boolean courseExists(String coursecode)
    {
        return findCourseByCode(coursecode) != null;
    }

    public static boolean canDeleteCourse(String coursecode)
    {
        Course course = findCourseByCode(coursecode);

        if (course == null)
        {
            return false;
        }

        return course.getEnrollments().isEmpty();
    }

    //FOR FILE UPDATING
    public static void clearCourses() {
        ecourses.clear();
    }
}