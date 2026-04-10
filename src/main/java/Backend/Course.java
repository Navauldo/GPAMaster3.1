package Backend;

import java.util.ArrayList;

public class Course {
    private String coursecode, coursename;
    private ArrayList<Enrollment> enrollments =new ArrayList<>();

    public Course(String ccode, String cname)
    {
        this.coursecode=ccode;
        this.coursename=cname;
    }


    //METHODS
    public String getCCode(){return this.coursecode;}
    public String getCName(){return this.coursename;}

    //SETTERS
    public void setCName(String newName) {this.coursename=newName;}
    public void setCCode(String newCCode){coursecode=newCCode;}

    //public ArrayList<Enrollment> getEnrollmentsbycourse(Course c){return c.enrollments;} SEEMS REDUNDANT
    public ArrayList<Enrollment> getEnrollments(){return this.enrollments;}


    public ArrayList<Student>getStudentsEnrolled()
    {
        ArrayList<Student> studentsenrolled=new ArrayList<>();
        for(Enrollment e: getEnrollments())
        {
            studentsenrolled.add(e.getStudent());
        }
        return studentsenrolled;
    }

    public void addEnrollment(Enrollment e){this.getEnrollments().add(e);}


    @Override
    public String toString(){
        return "Course Code: " + getCCode() + ", Course Name: " + getCName();
    }
}
