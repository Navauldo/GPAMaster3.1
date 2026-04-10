package Backend;

import java.util.ArrayList;

public class Student extends User {

    private ArrayList<Enrollment> enrollments=new ArrayList<>();

    public Student(int id, String name, String password)
    {
        super(id,name,password);
    }

    public void addEnrollment(Enrollment e) {this.enrollments.add(e);}

    public ArrayList<Enrollment> getEnrollments() {return this.enrollments;}

    public Enrollment getEnrollmentForCourse(Course c)
    {
        for (Enrollment e : enrollments)
        {
            if (e.getCourse() == c)
            {
                return e;
            }
        }
        return null;
    }

    // a new method returning an array os student grades could work as well
    public void viewGrades()
    {
        for(Enrollment en: getEnrollments())
        {
            System.out.println(en.getGrade());
        }
    }



    @Override
    public String toString()
    {
        String result = "Student ID: " + getId() + "\n";
        result += "Student Name: " + getName() + "\n";
        result += "Courses and Grades:\n";

        for (Enrollment e : getEnrollments()) {
            result += e.getCourse().getCCode() + " - " + e.getCourse().getCName()
                    + " : " + e.getGrade() + "\n";
        }

        return result;
    }
}
