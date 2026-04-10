package Frontend;

import atlantafx.base.theme.*;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import Backend.*;

public class MainApp extends Application {

    private static Stage primaryStage;
    private static Scene currentScene;

    @Override
    public void start(Stage stage) throws Exception {
        primaryStage = stage;
        Application.setUserAgentStylesheet(new NordDark().getUserAgentStylesheet());
        seedDemoData();
        loadScreen("login");
        primaryStage.setTitle("GPA Master");
        primaryStage.setMinWidth(900);
        primaryStage.setMinHeight(600);
        primaryStage.show();
    }

    public static void setTheme(String theme) {
        switch (theme) {
            case "NordDark"       -> Application.setUserAgentStylesheet(new NordDark().getUserAgentStylesheet());
            case "Dracula"        -> Application.setUserAgentStylesheet(new Dracula().getUserAgentStylesheet());
            case "CupertinoDark"  -> Application.setUserAgentStylesheet(new CupertinoDark().getUserAgentStylesheet());
            case "PrimerDark"     -> Application.setUserAgentStylesheet(new PrimerDark().getUserAgentStylesheet());
            case "PrimerLight"    -> Application.setUserAgentStylesheet(new PrimerLight().getUserAgentStylesheet());
            case "CupertinoLight" -> Application.setUserAgentStylesheet(new CupertinoLight().getUserAgentStylesheet());
        }
        if (currentScene != null) {
            currentScene.getStylesheets().clear();
            currentScene.getStylesheets().add(
                    MainApp.class.getResource("/Frontend/css/app.css").toExternalForm()
            );
        }
    }

    public static void loadScreen(String screenName) {
        try {
            FXMLLoader loader = new FXMLLoader(
                    MainApp.class.getResource("/Frontend/fxml/" + screenName + ".fxml")
            );
            Parent root = loader.load();
            currentScene = new Scene(root, 1100, 700);
            currentScene.getStylesheets().add(
                    MainApp.class.getResource("/Frontend/css/app.css").toExternalForm()
            );
            primaryStage.setScene(currentScene);
        } catch (Exception e) {
            System.err.println("Failed to load screen: " + screenName);
            e.printStackTrace();
        }
    }

    public static <T> T loadScreenWithController(String screenName) {
        try {
            FXMLLoader loader = new FXMLLoader(
                    MainApp.class.getResource("/Frontend/fxml/" + screenName + ".fxml")
            );
            Parent root = loader.load();
            currentScene = new Scene(root, 1100, 700);
            currentScene.getStylesheets().add(
                    MainApp.class.getResource("/Frontend/css/app.css").toExternalForm()
            );
            primaryStage.setScene(currentScene);
            return loader.getController();
        } catch (Exception e) {
            System.err.println("Failed to load screen: " + screenName);
            e.printStackTrace();
            return null;
        }
    }

    public static Stage getStage() { return primaryStage; }

    private void seedDemoData() {
        // Admins
        Admin admin = new Admin(1, "Main Admin", "admin123");
        AdminControl.addAdmin(admin);

        // Teachers
        Teacher t1 = new Teacher(101, "Mr. Brown", "teach101");
        Teacher t2 = new Teacher(102, "Ms. Green", "teach102");
        TeacherControl.addTeacher(t1);
        TeacherControl.addTeacher(t2);

        // Courses
        Course c1 = new Course("COMP1", "Programming 1");
        Course c2 = new Course("MATH1", "Calculus 1");
        Course c3 = new Course("COMP2", "Data Structures");
        CourseControl.addCourse(c1);
        CourseControl.addCourse(c2);
        CourseControl.addCourse(c3);

        // Assign courses to teachers
        t1.addCourse(c1);
        t1.addCourse(c3);
        t2.addCourse(c2);

        // Students
        Student s1 = new Student(201, "David McLish",  "stud201");
        Student s2 = new Student(202, "Anna Baptiste", "stud202");
        Student s3 = new Student(203, "James Carter",  "stud203");
        StudentControl.addStudent(s1);
        StudentControl.addStudent(s2);
        StudentControl.addStudent(s3);

        // Enroll with grades using new backend
        StudentControl.enrollStudentInCourse(201, "COMP1", 78);
        StudentControl.enrollStudentInCourse(201, "MATH1", 85);
        StudentControl.enrollStudentInCourse(202, "COMP1", 91);
        StudentControl.enrollStudentInCourse(202, "MATH1", 73);
        StudentControl.enrollStudentInCourse(203, "COMP2", 66);
    }

    public static void main(String[] args) {
        launch(args);
    }
}