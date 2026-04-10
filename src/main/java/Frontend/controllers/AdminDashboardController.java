package Frontend.controllers;

import Frontend.MainApp;
import Backend.*;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.*;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.FileChooser;

import java.io.File;
import java.util.stream.Collectors;

public class AdminDashboardController {

    @FXML private Label     adminNameLabel;
    @FXML private Label     pageTitle;
    @FXML private Button    addBtn;
    @FXML private TabPane   tabPane;
    @FXML private ComboBox<String> themePicker;

    @FXML private Tab studentsTab, teachersTab, coursesTab, gradesTab, enrollTab;
    @FXML private Button navStudents, navTeachers, navCourses, navGrades, navEnroll;

    @FXML private Label statStudents, statTeachers, statCourses, statEnrollments;

    // Students table
    @FXML private TableView<Student>          studentTable;
    @FXML private TableColumn<Student,String> colStudId, colStudName, colStudEnr, colStudGPA, colStudAct;
    @FXML private TextField studentSearch;

    // Teachers table
    @FXML private TableView<Teacher>          teacherTable;
    @FXML private TableColumn<Teacher,String> colTeachId, colTeachName, colTeachCourses, colTeachAct;
    @FXML private TextField teacherSearch;

    // Courses table
    @FXML private TableView<Course>          courseTable;
    @FXML private TableColumn<Course,String> colCourseCode, colCourseName, colCourseEnr, colCourseAct;
    @FXML private TextField courseSearch;

    // Grade input
    @FXML private TextField gradeStudId, gradeCode, gradeValue;
    @FXML private Label     adminGradeMsg;

    // Enroll tab
    @FXML private TextField enrollStudId, enrollCourseCode;
    @FXML private Label     enrollMsg;

    // Save/Load
    @FXML private Label saveLoadMsg;

    private Admin loggedAdmin;
    private ObservableList<Student> studentData = FXCollections.observableArrayList();
    private ObservableList<Teacher> teacherData = FXCollections.observableArrayList();
    private ObservableList<Course>  courseData  = FXCollections.observableArrayList();

    public void initData(Admin admin) {
        this.loggedAdmin = admin;
        adminNameLabel.setText(admin.getName());
        setupTables();
        refreshAll();
        setupThemePicker();
        showStudentsTab();
    }

    private void setupThemePicker() {
        themePicker.getItems().addAll(
                "NordDark","Dracula","CupertinoDark","PrimerDark","PrimerLight","CupertinoLight");
        themePicker.setValue("NordDark");
        themePicker.setOnAction(e -> MainApp.setTheme(themePicker.getValue()));
    }

    private void setupTables() {
        // Students
        colStudId.setCellValueFactory(c -> new SimpleStringProperty(String.valueOf(c.getValue().getId())));
        colStudName.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getName()));
        colStudEnr.setCellValueFactory(c -> {
            String courses = c.getValue().getEnrollments().stream()
                    .map(e -> e.getCourse().getCCode()).collect(Collectors.joining(", "));
            return new SimpleStringProperty(courses.isEmpty() ? "—" : courses);
        });
        colStudGPA.setCellValueFactory(c -> new SimpleStringProperty(
                String.format("%.2f", GPA.calculateGPA(c.getValue()))));
        colStudAct.setCellFactory(col -> new TableCell<>() {
            final Button edit = new Button("Edit"), del = new Button("Delete");
            {
                edit.setStyle("-fx-font-size:11px;");
                del.setStyle("-fx-font-size:11px;");
                edit.setOnAction(e -> editStudent(getTableView().getItems().get(getIndex())));
                del.setOnAction(e -> deleteStudent(getTableView().getItems().get(getIndex())));
            }
            @Override protected void updateItem(String i, boolean empty) {
                super.updateItem(i, empty);
                setGraphic(empty ? null : new HBox(6, edit, del));
            }
        });
        studentTable.setItems(studentData);

        // Teachers
        colTeachId.setCellValueFactory(c -> new SimpleStringProperty(String.valueOf(c.getValue().getId())));
        colTeachName.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getName()));
        colTeachCourses.setCellValueFactory(c -> {
            String courses = c.getValue().getCoursesTaught().stream()
                    .map(Course::getCCode).collect(Collectors.joining(", "));
            return new SimpleStringProperty(courses.isEmpty() ? "—" : courses);
        });
        colTeachAct.setCellFactory(col -> new TableCell<>() {
            final Button edit   = new Button("Edit");
            final Button assign = new Button("Assign Course");
            final Button remove = new Button("Remove Course");
            final Button del    = new Button("Delete");

            {
                edit.setStyle("-fx-font-size:11px;");
                assign.setStyle("-fx-font-size:11px;");
                remove.setStyle("-fx-font-size:11px;");
                del.setStyle("-fx-font-size:11px;");

                edit.setOnAction(e -> editTeacher(getTableView().getItems().get(getIndex())));
                assign.setOnAction(e -> assignCourseToTeacher(getTableView().getItems().get(getIndex())));
                remove.setOnAction(e -> removeCourseFromTeacher(getTableView().getItems().get(getIndex())));
                del.setOnAction(e -> deleteTeacher(getTableView().getItems().get(getIndex())));
            }

            @Override protected void updateItem(String i, boolean empty) {
                super.updateItem(i, empty);
                setGraphic(empty ? null : new HBox(6, edit, assign, remove, del));
            }
        });
        teacherTable.setItems(teacherData);
        colTeachAct.setPrefWidth(580);
        // Courses
        colCourseCode.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getCCode()));
        colCourseName.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getCName()));
        colCourseEnr.setCellValueFactory(c -> new SimpleStringProperty(
                String.valueOf(c.getValue().getStudentsEnrolled().size())));
        colCourseAct.setCellFactory(col -> new TableCell<>() {
            final Button edit = new Button("Edit"), del = new Button("Delete");
            {
                edit.setStyle("-fx-font-size:11px;");
                del.setStyle("-fx-font-size:11px;");
                edit.setOnAction(e -> editCourse(getTableView().getItems().get(getIndex())));
                del.setOnAction(e -> deleteCourse(getTableView().getItems().get(getIndex())));
            }
            @Override protected void updateItem(String i, boolean empty) {
                super.updateItem(i, empty);
                setGraphic(empty ? null : new HBox(6, edit, del));
            }
        });
        courseTable.setItems(courseData);
    }

    // ── Nav ───────────────────────────────────────────────────────────────────

    @FXML private void showStudentsTab() {
        tabPane.getSelectionModel().select(studentsTab);
        pageTitle.setText("Students");
        addBtn.setText("+ Add Student");
        addBtn.setVisible(true);
        setNavActive(navStudents);
        refreshStudents();
    }

    @FXML private void showTeachersTab() {
        tabPane.getSelectionModel().select(teachersTab);
        pageTitle.setText("Teachers");
        addBtn.setText("+ Add Teacher");
        addBtn.setVisible(true);
        setNavActive(navTeachers);
        refreshTeachers();
    }

    @FXML private void showCoursesTab() {
        tabPane.getSelectionModel().select(coursesTab);
        pageTitle.setText("Courses");
        addBtn.setText("+ Add Course");
        addBtn.setVisible(true);
        setNavActive(navCourses);
        refreshCourses();
    }

    @FXML private void showGradesTab() {
        tabPane.getSelectionModel().select(gradesTab);
        pageTitle.setText("Input Grades");
        addBtn.setVisible(false);
        setNavActive(navGrades);
    }

    @FXML private void showEnrollTab() {
        tabPane.getSelectionModel().select(enrollTab);
        pageTitle.setText("Enroll Student");
        addBtn.setVisible(false);
        setNavActive(navEnroll);
    }

    private void setNavActive(Button active) {
        for (Button b : new Button[]{navStudents, navTeachers, navCourses, navGrades, navEnroll}) {
            b.getStyleClass().remove("nav-btn-active");
        }
        active.getStyleClass().add("nav-btn-active");
    }

    @FXML private void handleAdd() {
        Tab sel = tabPane.getSelectionModel().getSelectedItem();
        if (sel == studentsTab) addStudent();
        else if (sel == teachersTab) addTeacher();
        else addCourse();
    }

    // ── Search ────────────────────────────────────────────────────────────────

    @FXML private void filterStudents() {
        String q = studentSearch.getText().toLowerCase();
        studentData.setAll(StudentControl.getEStudents().stream()
                .filter(s -> s.getName().toLowerCase().contains(q) || String.valueOf(s.getId()).contains(q))
                .collect(Collectors.toList()));
    }

    @FXML private void filterTeachers() {
        String q = teacherSearch.getText().toLowerCase();
        teacherData.setAll(TeacherControl.getAllTeachers().stream()
                .filter(t -> t.getName().toLowerCase().contains(q) || String.valueOf(t.getId()).contains(q))
                .collect(Collectors.toList()));
    }

    @FXML private void filterCourses() {
        String q = courseSearch.getText().toLowerCase();
        courseData.setAll(CourseControl.getAllCourses().stream()
                .filter(c -> c.getCName().toLowerCase().contains(q) || c.getCCode().toLowerCase().contains(q))
                .collect(Collectors.toList()));
    }

    // ── Refresh ───────────────────────────────────────────────────────────────

    private void refreshAll() {
        refreshStudents();
        refreshTeachers();
        refreshCourses();
        refreshStats();
    }

    private void refreshStudents() {
        studentData.setAll(StudentControl.getEStudents());
    }

    private void refreshTeachers() {
        teacherData.setAll(TeacherControl.getAllTeachers());
    }

    private void refreshCourses() {
        courseData.setAll(CourseControl.getAllCourses());
    }

    private void refreshStats() {
        statStudents.setText(String.valueOf(StudentControl.getEStudents().size()));
        statTeachers.setText(String.valueOf(TeacherControl.getAllTeachers().size()));
        statCourses.setText(String.valueOf(CourseControl.getAllCourses().size()));
        statEnrollments.setText(String.valueOf(
                StudentControl.getEStudents().stream().mapToInt(s -> s.getEnrollments().size()).sum()));
    }

    // ── Student CRUD ──────────────────────────────────────────────────────────

    private void addStudent() {
        buildStudentDialog("Add Student", null).showAndWait().ifPresent(s -> {
            loggedAdmin.addStudent(s);
            refreshAll();
        });
    }

    private void editStudent(Student s) {
        buildStudentDialog("Edit Student", s).showAndWait().ifPresent(u -> {
            loggedAdmin.editStudent(s.getId(), u.getId(), u.getName(), u.getPassword());
            refreshAll();
        });
    }

    private void deleteStudent(Student s) {
        if (confirmDelete("student", s.getName())) {
            loggedAdmin.deleteStudent(s.getId());
            refreshAll();
        }
    }

    private Dialog<Student> buildStudentDialog(String title, Student ex) {
        Dialog<Student> dlg = new Dialog<>();
        dlg.setTitle(title);
        dlg.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        GridPane g = new GridPane();
        g.setHgap(10);
        g.setVgap(10);
        g.setPadding(new Insets(20));

        TextField id = new TextField(ex != null ? String.valueOf(ex.getId()) : "");
        TextField nm = new TextField(ex != null ? ex.getName() : "");
        PasswordField pw = new PasswordField();
        if (ex != null) pw.setText(ex.getPassword());

        g.add(new Label("ID:"), 0, 0);
        g.add(id, 1, 0);
        g.add(new Label("Name:"), 0, 1);
        g.add(nm, 1, 1);
        g.add(new Label("Password:"), 0, 2);
        g.add(pw, 1, 2);

        dlg.getDialogPane().setContent(g);
        dlg.setResultConverter(btn -> {
            if (btn == ButtonType.OK) {
                try {
                    return new Student(Integer.parseInt(id.getText().trim()), nm.getText().trim(), pw.getText().trim());
                } catch (NumberFormatException ex2) {
                    showAlert("Invalid ID", "ID must be a number.");
                }
            }
            return null;
        });
        return dlg;
    }

    // ── Teacher CRUD ──────────────────────────────────────────────────────────

    private void addTeacher() {
        buildTeacherDialog("Add Teacher", null).showAndWait().ifPresent(t -> {
            loggedAdmin.addTeacher(t);
            refreshAll();
        });
    }

    private void editTeacher(Teacher t) {
        buildTeacherDialog("Edit Teacher", t).showAndWait().ifPresent(u -> {
            loggedAdmin.editTeacher(t.getId(), u.getName(), u.getPassword());
            refreshAll();
        });
    }

    private void deleteTeacher(Teacher t) {
        if (confirmDelete("teacher", t.getName())) {
            loggedAdmin.deleteTeacher(t.getId());
            refreshAll();
        }
    }

    private void assignCourseToTeacher(Teacher teacher) {
        Dialog<String> dlg = new Dialog<>();
        dlg.setTitle("Assign Course to Teacher");
        dlg.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        GridPane g = new GridPane();
        g.setHgap(10);
        g.setVgap(10);
        g.setPadding(new Insets(20));

        Label teacherLabel = new Label("Teacher: " + teacher.getName() + " (" + teacher.getId() + ")");
        TextField courseCodeField = new TextField();
        courseCodeField.setPromptText("Enter course code");

        g.add(teacherLabel, 0, 0, 2, 1);
        g.add(new Label("Course Code:"), 0, 1);
        g.add(courseCodeField, 1, 1);

        dlg.getDialogPane().setContent(g);

        dlg.setResultConverter(btn -> btn == ButtonType.OK ? courseCodeField.getText().trim() : null);

        dlg.showAndWait().ifPresent(courseCode -> {
            if (courseCode.isEmpty()) {
                showAlert("Missing Course Code", "Please enter a course code.");
                return;
            }

            boolean ok = loggedAdmin.assignCourseToTeacher(teacher.getId(), courseCode);
            if (ok) {
                refreshAll();
            } else {
                showAlert("Assignment Failed",
                        "Could not assign course. Check the course code or the teacher may already teach this course.");
            }
        });
    }

    private void removeCourseFromTeacher(Teacher teacher) {
        Dialog<String> dlg = new Dialog<>();
        dlg.setTitle("Remove Course from Teacher");
        dlg.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        GridPane g = new GridPane();
        g.setHgap(10);
        g.setVgap(10);
        g.setPadding(new Insets(20));

        Label teacherLabel = new Label("Teacher: " + teacher.getName() + " (" + teacher.getId() + ")");
        TextField courseCodeField = new TextField();
        courseCodeField.setPromptText("Enter course code");

        g.add(teacherLabel, 0, 0, 2, 1);
        g.add(new Label("Course Code:"), 0, 1);
        g.add(courseCodeField, 1, 1);

        dlg.getDialogPane().setContent(g);

        dlg.setResultConverter(btn -> btn == ButtonType.OK ? courseCodeField.getText().trim() : null);

        dlg.showAndWait().ifPresent(courseCode -> {
            if (courseCode.isEmpty()) {
                showAlert("Missing Course Code", "Please enter a course code.");
                return;
            }

            boolean ok = loggedAdmin.removeCourseFromTeacher(teacher.getId(), courseCode);
            if (ok) {
                refreshAll();
            } else {
                showAlert("Removal Failed",
                        "Could not remove course. Check that the teacher actually teaches that course.");
            }
        });
    }

    private Dialog<Teacher> buildTeacherDialog(String title, Teacher ex) {
        Dialog<Teacher> dlg = new Dialog<>();
        dlg.setTitle(title);
        dlg.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        GridPane g = new GridPane();
        g.setHgap(10);
        g.setVgap(10);
        g.setPadding(new Insets(20));

        TextField id = new TextField(ex != null ? String.valueOf(ex.getId()) : "");
        TextField nm = new TextField(ex != null ? ex.getName() : "");
        PasswordField pw = new PasswordField();
        if (ex != null) pw.setText(ex.getPassword());
        if (ex != null) id.setDisable(true);

        g.add(new Label("ID:"), 0, 0);
        g.add(id, 1, 0);
        g.add(new Label("Name:"), 0, 1);
        g.add(nm, 1, 1);
        g.add(new Label("Password:"), 0, 2);
        g.add(pw, 1, 2);

        dlg.getDialogPane().setContent(g);
        dlg.setResultConverter(btn -> {
            if (btn == ButtonType.OK) {
                try {
                    int tid = ex != null ? ex.getId() : Integer.parseInt(id.getText().trim());
                    return new Teacher(tid, nm.getText().trim(), pw.getText().trim());
                } catch (NumberFormatException ex2) {
                    showAlert("Invalid ID", "ID must be a number.");
                }
            }
            return null;
        });
        return dlg;
    }

    // ── Course CRUD ───────────────────────────────────────────────────────────

    private void addCourse() {
        buildCourseDialog("Add Course", null).showAndWait().ifPresent(c -> {
            loggedAdmin.addCourse(c);
            refreshAll();
        });
    }

    private void editCourse(Course c) {
        buildCourseDialog("Edit Course", c).showAndWait().ifPresent(u -> {
            loggedAdmin.editCourse(c.getCCode(), u.getCName());
            refreshAll();
        });
    }

    private void deleteCourse(Course c) {
        if (confirmDelete("course", c.getCName())) {
            loggedAdmin.deleteCourse(c.getCCode());
            refreshAll();
        }
    }

    private Dialog<Course> buildCourseDialog(String title, Course ex) {
        Dialog<Course> dlg = new Dialog<>();
        dlg.setTitle(title);
        dlg.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        GridPane g = new GridPane();
        g.setHgap(10);
        g.setVgap(10);
        g.setPadding(new Insets(20));

        TextField code = new TextField(ex != null ? ex.getCCode() : "");
        TextField name = new TextField(ex != null ? ex.getCName() : "");
        if (ex != null) code.setDisable(true);

        g.add(new Label("Course Code:"), 0, 0);
        g.add(code, 1, 0);
        g.add(new Label("Course Name:"), 0, 1);
        g.add(name, 1, 1);

        dlg.getDialogPane().setContent(g);
        dlg.setResultConverter(btn -> btn == ButtonType.OK
                ? new Course(code.getText().trim(), name.getText().trim()) : null);
        return dlg;
    }

    // ── UC-003: Input Grade (Admin) ───────────────────────────────────────────

    @FXML private void handleAdminGrade() {
        adminGradeMsg.setVisible(false);
        adminGradeMsg.setManaged(false);

        String sid = gradeStudId.getText().trim();
        String cc  = gradeCode.getText().trim();
        String gv  = gradeValue.getText().trim();

        if (sid.isEmpty() || cc.isEmpty() || gv.isEmpty()) {
            showGradeMsg("Please fill in all fields.", false);
            return;
        }

        int studentId, grade;
        try {
            studentId = Integer.parseInt(sid);
            grade = Integer.parseInt(gv);
        } catch (NumberFormatException e) {
            showGradeMsg("ID and grade must be numbers.", false);
            return;
        }

        if (grade < 0 || grade > 100) {
            showGradeMsg("Grade must be 0–100.", false);
            return;
        }

        Student student = StudentControl.getStudent(studentId);
        if (student == null) {
            showGradeMsg("Student ID " + studentId + " not found.", false);
            return;
        }

        Course course = CourseControl.findCourseByCode(cc);
        if (course == null) {
            showGradeMsg("Course code " + cc + " not found.", false);
            return;
        }

        Enrollment e = student.getEnrollmentForCourse(course);
        if (e == null) {
            showGradeMsg("Student is not enrolled in that course.", false);
            return;
        }

        e.setGrade(grade);
        showGradeMsg("Grade updated successfully!", true);
        gradeStudId.clear();
        gradeCode.clear();
        gradeValue.clear();
        refreshAll();
    }

    private void showGradeMsg(String msg, boolean ok) {
        adminGradeMsg.setText(msg);
        adminGradeMsg.setStyle(ok ? "-fx-text-fill: -color-success-fg;" : "-fx-text-fill: -color-danger-fg;");
        adminGradeMsg.setVisible(true);
        adminGradeMsg.setManaged(true);
    }

    // ── Enroll Student in Course (Admin) ──────────────────────────────────────

    @FXML private void handleEnroll() {
        enrollMsg.setVisible(false);
        enrollMsg.setManaged(false);

        String sid = enrollStudId.getText().trim();
        String cc  = enrollCourseCode.getText().trim();

        if (sid.isEmpty() || cc.isEmpty()) {
            showEnrollMsg("Please fill in all fields.", false);
            return;
        }

        int studentId;
        try {
            studentId = Integer.parseInt(sid);
        } catch (NumberFormatException e) {
            showEnrollMsg("Student ID must be a number.", false);
            return;
        }

        boolean ok = loggedAdmin.enrollStudentInCourse(studentId, cc);
        if (ok) {
            showEnrollMsg("Student enrolled successfully!", true);
            enrollStudId.clear();
            enrollCourseCode.clear();
            refreshAll();
        } else {
            showEnrollMsg("Enrolment failed. Check student ID and course code, or student already enrolled.", false);
        }
    }

    private void showEnrollMsg(String msg, boolean ok) {
        enrollMsg.setText(msg);
        enrollMsg.setStyle(ok ? "-fx-text-fill: -color-success-fg;" : "-fx-text-fill: -color-danger-fg;");
        enrollMsg.setVisible(true);
        enrollMsg.setManaged(true);
    }

    // ── Save / Load (FileUpdate) ──────────────────────────────────────────────

    @FXML private void handleSave() {
        FileChooser fc = new FileChooser();
        fc.setTitle("Save GPA Master Data");
        fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("Text Files", "*.txt"));
        fc.setInitialFileName("gpa_master_data.txt");
        File file = fc.showSaveDialog(MainApp.getStage());

        if (file != null) {
            boolean ok = loggedAdmin.saveSystem(file.getAbsolutePath());
            showSaveLoadMsg(ok ? "System saved to " + file.getName() : "Save failed.", ok);
        }
    }

    @FXML private void handleLoad() {
        FileChooser fc = new FileChooser();
        fc.setTitle("Load GPA Master Data");
        fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("Text Files", "*.txt"));
        File file = fc.showOpenDialog(MainApp.getStage());

        if (file != null) {
            boolean ok = loggedAdmin.loadSystem(file.getAbsolutePath());
            if (ok) {
                refreshAll();
                showSaveLoadMsg("System loaded from " + file.getName(), true);
            } else {
                showSaveLoadMsg("Load failed. File may be corrupt or missing.", false);
            }
        }
    }

    private void showSaveLoadMsg(String msg, boolean ok) {
        saveLoadMsg.setText(msg);
        saveLoadMsg.setStyle(ok ? "-fx-text-fill: -color-success-fg;" : "-fx-text-fill: -color-danger-fg;");
        saveLoadMsg.setVisible(true);
        saveLoadMsg.setManaged(true);
    }

    // ── Helpers ───────────────────────────────────────────────────────────────

    private boolean confirmDelete(String type, String name) {
        Alert a = new Alert(Alert.AlertType.CONFIRMATION);
        a.setTitle("Confirm Delete");
        a.setHeaderText("Delete " + type + "?");
        a.setContentText("Are you sure you want to delete \"" + name + "\"?");
        return a.showAndWait().filter(b -> b == ButtonType.OK).isPresent();
    }

    private void showAlert(String title, String msg) {
        Alert a = new Alert(Alert.AlertType.ERROR, msg, ButtonType.OK);
        a.setTitle(title);
        a.showAndWait();
    }

    @FXML private void handleLogout() {
        MainApp.loadScreen("login");
    }
}