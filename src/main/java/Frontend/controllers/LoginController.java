package Frontend.controllers;

import Frontend.MainApp;
import Backend.*;

import javafx.fxml.FXML;
import javafx.scene.control.*;

public class LoginController {

    @FXML private TextField     idField;
    @FXML private PasswordField passwordField;
    @FXML private Label         errorLabel;
    @FXML private Label         hintLabel;
    @FXML private Button        roleAdmin;
    @FXML private Button        roleTeacher;
    @FXML private Button        roleStudent;

    private String selectedRole = "Admin";

    @FXML private void selectAdmin() {
        selectedRole = "Admin";
        setRoleActive(roleAdmin);
        hintLabel.setText("Demo: ID 1 / admin123");
    }

    @FXML private void selectTeacher() {
        selectedRole = "Teacher";
        setRoleActive(roleTeacher);
        hintLabel.setText("Demo: ID 101 / teach101");
    }

    @FXML private void selectStudent() {
        selectedRole = "Student";
        setRoleActive(roleStudent);
        hintLabel.setText("Demo: ID 201 / stud201");
    }

    private void setRoleActive(Button active) {
        roleAdmin  .getStyleClass().remove("role-btn-active");
        roleTeacher.getStyleClass().remove("role-btn-active");
        roleStudent.getStyleClass().remove("role-btn-active");
        active.getStyleClass().add("role-btn-active");
        errorLabel.setVisible(false);
        errorLabel.setManaged(false);
    }

    @FXML private void handleLogin() {
        String idText = idField.getText().trim();
        String pass   = passwordField.getText().trim();
        if (idText.isEmpty() || pass.isEmpty()) { showError("Please enter both ID and password."); return; }
        int id;
        try { id = Integer.parseInt(idText); }
        catch (NumberFormatException e) { showError("User ID must be a number."); return; }

        switch (selectedRole) {
            case "Admin" -> {
                Admin admin = AdminControl.findAdminById(id);
                if (admin != null && admin.getPassword().equals(pass)) {
                    AdminDashboardController ctrl = MainApp.loadScreenWithController("admin-dashboard");
                    if (ctrl != null) ctrl.initData(admin);
                    return;
                }
            }
            case "Teacher" -> {
                Teacher teacher = TeacherControl.findTeacherById(id);
                if (teacher != null && teacher.getPassword().equals(pass)) {
                    TeacherDashboardController ctrl = MainApp.loadScreenWithController("teacher-dashboard");
                    if (ctrl != null) ctrl.initData(teacher);
                    return;
                }
            }
            case "Student" -> {
                Student student = StudentControl.getStudent(id);
                if (student != null && student.getPassword().equals(pass)) {
                    StudentDashboardController ctrl = MainApp.loadScreenWithController("student-dashboard");
                    if (ctrl != null) ctrl.initData(student);
                    return;
                }
            }
        }
        showError("Invalid ID or password for " + selectedRole + ".");
    }

    private void showError(String msg) {
        errorLabel.setText(msg);
        errorLabel.setVisible(true);
        errorLabel.setManaged(true);
    }
}