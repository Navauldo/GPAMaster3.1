package Frontend.controllers;

import Frontend.MainApp;
import Backend.*;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.*;
import javafx.fxml.*;
import javafx.scene.control.*;

public class TeacherDashboardController {

    @FXML private Label     teacherNameLabel;
    @FXML private Label     pageTitle;
    @FXML private TabPane   tabPane;
    @FXML private Tab       studentsTab, gradesTab, academicTab;
    @FXML private Button    navMyStudents, navGrades, navAcademic;
    @FXML private ComboBox<String> themePicker;

    @FXML private Label statStudents, statCourses, statGrades;

    @FXML private TableView<Enrollment>          studentTable;
    @FXML private TableColumn<Enrollment,String> colId, colName, colCourse, colGrade;

    @FXML private TextField gradeStudId, gradeCode, gradeValue;
    @FXML private Label     gradeMsg;

    // Academic info lookup (UC: Teacher views student academic info)
    @FXML private TextField academicStudId;
    @FXML private Label     academicName, academicGPA, academicHonours,
            academicStatus, academicMsg;

    private Teacher loggedTeacher;
    private ObservableList<Enrollment> enrollmentData = FXCollections.observableArrayList();

    public void initData(Teacher teacher) {
        this.loggedTeacher = teacher;
        teacherNameLabel.setText(teacher.getName());
        setupTable();
        refreshStudents();
        setupThemePicker();
        showStudentsView();
    }

    private void setupThemePicker() {
        themePicker.getItems().addAll(
                "NordDark","Dracula","CupertinoDark","PrimerDark","PrimerLight","CupertinoLight");
        themePicker.setValue("NordDark");
        themePicker.setOnAction(e -> MainApp.setTheme(themePicker.getValue()));
    }

    private void setupTable() {
        colId    .setCellValueFactory(c -> new SimpleStringProperty(String.valueOf(c.getValue().getStudent().getId())));
        colName  .setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getStudent().getName()));
        colCourse.setCellValueFactory(c -> new SimpleStringProperty(
                c.getValue().getCourse().getCCode() + " — " + c.getValue().getCourse().getCName()));
        colGrade .setCellValueFactory(c -> new SimpleStringProperty(String.valueOf(c.getValue().getGrade())));
        studentTable.setItems(enrollmentData);
    }

    private void refreshStudents() {
        enrollmentData.clear();
        for (Course course : loggedTeacher.getCoursesTaught())
            enrollmentData.addAll(course.getEnrollments());
        refreshStats();
    }

    private void refreshStats() {
        statStudents.setText(String.valueOf(
                loggedTeacher.getCoursesTaught().stream()
                        .mapToInt(c -> c.getStudentsEnrolled().size()).sum()));
        statCourses.setText(String.valueOf(loggedTeacher.getCoursesTaught().size()));
        statGrades .setText(String.valueOf(enrollmentData.size()));
    }

    @FXML private void showStudentsView() {
        tabPane.getSelectionModel().select(studentsTab);
        pageTitle.setText("My Students");
        setNavActive(navMyStudents);
        refreshStudents();
    }
    @FXML private void showGradesView() {
        tabPane.getSelectionModel().select(gradesTab);
        pageTitle.setText("Input Grades");
        setNavActive(navGrades);
    }
    @FXML private void showAcademicView() {
        tabPane.getSelectionModel().select(academicTab);
        pageTitle.setText("Student Academic Info");
        setNavActive(navAcademic);
    }

    private void setNavActive(Button active) {
        for (Button b : new Button[]{navMyStudents, navGrades, navAcademic})
            b.getStyleClass().remove("nav-btn-active");
        active.getStyleClass().add("nav-btn-active");
    }

    // ── UC-003: Teacher inputs grade ──────────────────────────────────────────

    @FXML private void handleSubmitGrade() {
        gradeMsg.setVisible(false); gradeMsg.setManaged(false);
        String sid = gradeStudId.getText().trim();
        String cc  = gradeCode.getText().trim();
        String gv  = gradeValue.getText().trim();
        if (sid.isEmpty() || cc.isEmpty() || gv.isEmpty()) {
            showMsg(gradeMsg, "Please fill in all fields.", false); return;
        }
        int studentId, grade;
        try { studentId = Integer.parseInt(sid); grade = Integer.parseInt(gv); }
        catch (NumberFormatException e) { showMsg(gradeMsg, "ID and grade must be numbers.", false); return; }
        if (grade < 0 || grade > 100) { showMsg(gradeMsg, "Grade must be 0–100.", false); return; }

        boolean ok = TeacherControl.updateStudentGrade(
                loggedTeacher.getId(), studentId, cc, grade);
        if (ok) {
            showMsg(gradeMsg, "Grade updated successfully!", true);
            gradeStudId.clear(); gradeCode.clear(); gradeValue.clear();
            refreshStudents();
        } else {
            showMsg(gradeMsg, "Failed. Check student ID, course code, and that you teach this course.", false);
        }
    }

    // ── Teacher views student academic info ───────────────────────────────────

    @FXML private void handleLookupAcademic() {
        academicMsg.setVisible(false); academicMsg.setManaged(false);
        clearAcademicLabels();
        String sid = academicStudId.getText().trim();
        if (sid.isEmpty()) { showMsg(academicMsg, "Please enter a student ID.", false); return; }
        int studentId;
        try { studentId = Integer.parseInt(sid); }
        catch (NumberFormatException e) { showMsg(academicMsg, "Student ID must be a number.", false); return; }

        Student student = TeacherControl.viewStudentAcademicInfo(loggedTeacher.getId(), studentId);
        if (student == null) {
            showMsg(academicMsg, "Student not found or not taught by you.", false); return;
        }
        academicName   .setText(student.getName() + "  (ID: " + student.getId() + ")");
        academicGPA    .setText(String.format("%.2f", GPA.calculateGPA(student)));
        academicHonours.setText(GPA.determineAcademicHonors(student));
        academicStatus .setText(GPA.determineGraduationStatus(student));
        showMsg(academicMsg, "Student found.", true);
    }

    private void clearAcademicLabels() {
        academicName.setText("—"); academicGPA.setText("—");
        academicHonours.setText("—"); academicStatus.setText("—");
    }

    private void showMsg(Label label, String msg, boolean ok) {
        label.setText(msg);
        label.setStyle(ok ? "-fx-text-fill: -color-success-fg;" : "-fx-text-fill: -color-danger-fg;");
        label.setVisible(true); label.setManaged(true);
    }

    @FXML private void handleLogout() { MainApp.loadScreen("login"); }
}

