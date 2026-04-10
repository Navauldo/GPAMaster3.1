package Backend;

import java.util.ArrayList;

public class Teacher extends User {

    private ArrayList<Course>coursestaught=new ArrayList<>();

    //CONSTRUCTOR
    public Teacher(int id, String name, String password)
    {
        super(id,name,password);
    }



    public boolean addCourse(Course c)
    {
        if (c == null) {
            return false;
        }

        if (coursestaught.contains(c)) {
            return false;
        }

        coursestaught.add(c);
        return true;
    }


    public boolean removeCourse(Course c)
    {
        if (c == null) {
            return false;
        }

        return coursestaught.remove(c);
    }

    public ArrayList<Course> getCoursesTaught(){return this.coursestaught;}

    public void updateStudentGrade(Student s, Course c, int newGrade)
    {
        if (!coursestaught.contains(c)) {
            System.out.println("Teacher does not teach this course.");
            return;
        }

        Enrollment e = s.getEnrollmentForCourse(c);

        if (e == null) {
            System.out.println("Student is not enrolled in this course.");
            return;
        }

        e.setGrade(newGrade);
    }


    @Override
    public String toString(){
        return "Teacher ID: " + getId() + ", Teacher Name: " + getName();
    }

}
