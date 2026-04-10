package Frontend.controllers;

import Frontend.MainApp;
import Backend.*;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.*;
import javafx.fxml.*;
import javafx.scene.control.*;

import java.util.List;

public class StudentDashboardController {

    @FXML private Label   studentNameLabel, studentIdLabel, pageTitle;
    @FXML private TabPane tabPane;
    @FXML private Tab     gradesTab, gpaTab;
    @FXML private Button  navGrades, navGPA;
    @FXML private ComboBox<String> themePicker;

    @FXML private TableView<Enrollment>          gradesTable;
    @FXML private TableColumn<Enrollment,String> colCode, colCName, colGrade, colLetter;

    @FXML private Label gpaValue, courseCount, standingLabel;

    // GPA tab detail labels
    @FXML private Label gpaDetailed, graduationStatus, academicHonours;

    private Student loggedStudent;
    private ObservableList<Enrollment> enrollmentData = FXCollections.observableArrayList();

    public void initData(Student student) {
        this.loggedStudent = student;
        studentNameLabel.setText(student.getName());
        studentIdLabel  .setText("ID: " + student.getId());
        setupTable();
        refreshData();
        setupThemePicker();
        showGradesView();
    }

    private void setupThemePicker() {
        themePicker.getItems().addAll(
                "NordDark","Dracula","CupertinoDark","PrimerDark","PrimerLight","CupertinoLight");
        themePicker.setValue("NordDark");
        themePicker.setOnAction(e -> MainApp.setTheme(themePicker.getValue()));
    }

    private void setupTable() {
        colCode  .setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getCourse().getCCode()));
        colCName .setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getCourse().getCName()));
        colGrade .setCellValueFactory(c -> new SimpleStringProperty(String.valueOf(c.getValue().getGrade())));
        colLetter.setCellValueFactory(c -> new SimpleStringProperty(toLetter(c.getValue().getGrade())));
        gradesTable.setItems(enrollmentData);
    }

    private void refreshData() {
        List<Enrollment> enrollments = loggedStudent.getEnrollments();
        enrollmentData.setAll(enrollments);
        courseCount.setText(String.valueOf(enrollments.size()));

        if (enrollments.isEmpty()) {
            gpaValue.setText("N/A");
            standingLabel.setText("No courses yet");
            if (gpaDetailed    != null) gpaDetailed.setText("—");
            if (graduationStatus != null) graduationStatus.setText("—");
            if (academicHonours  != null) academicHonours.setText("—");
            return;
        }

        // Use real GPA backend
        double gpa = GPA.calculateGPA(loggedStudent);
        String honours  = GPA.determineAcademicHonors(loggedStudent);
        String gradStat = GPA.determineGraduationStatus(loggedStudent);

        gpaValue.setText(String.format("%.2f", gpa));
        standingLabel.setText(honours);

        if (gpaDetailed    != null) gpaDetailed.setText(String.format("%.4f", gpa));
        if (graduationStatus != null) graduationStatus.setText(gradStat);
        if (academicHonours  != null) academicHonours.setText(honours);
    }

    @FXML private void showGradesView() {
        tabPane.getSelectionModel().select(gradesTab);
        pageTitle.setText("My Grades");
        setNavActive(navGrades);
        refreshData();
    }

    @FXML private void showGPAView() {
        tabPane.getSelectionModel().select(gpaTab);
        pageTitle.setText("GPA and Status");
        setNavActive(navGPA);
        refreshData();
    }

    private void setNavActive(Button active) {
        navGrades.getStyleClass().remove("nav-btn-active");
        navGPA   .getStyleClass().remove("nav-btn-active");
        active.getStyleClass().add("nav-btn-active");
    }

    private String toLetter(int grade) {
        if (grade >= 90) return "A+";
        if (grade >= 80) return "A";
        if (grade >= 75) return "B+";
        if (grade >= 70) return "B";
        if (grade >= 65) return "C+";
        if (grade >= 60) return "C";
        if (grade >= 55) return "D+";
        if (grade >= 50) return "D";
        if (grade >= 40) return "E";
        return "F";
    }

    @FXML private void handleLogout() { MainApp.loadScreen("login"); }
}