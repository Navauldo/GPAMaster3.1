package Backend;

public class Enrollment {
    Student student;
    Course course;
    int grade;

    //CONSTRUCTORS BELOW///////////////////////////////////////////////////////
    public Enrollment(Student s, Course c, int grade)
    {
        this.student=s;
        this.course=c;
        this.grade=grade;
    }
    public Enrollment(Student s, Course c)
    {
        this.student=s;
        this.course=c;
    }
    //////////////////////////////////////////////////////////////////////////


    public Student getStudent(){return this.student;}
    public Course getCourse(){return this.course;}
    public int getGrade(){return this.grade;}

    //SETTER METHOD FOR THE USE CASE OF INPUT GRADE BELOW
    public void setGrade(int newGrade){this.grade=newGrade;}


}
