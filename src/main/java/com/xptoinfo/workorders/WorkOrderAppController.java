package com.xptoinfo.workorders;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

public class WorkOrderAppController {

    @FXML
    private TextField username;
    @FXML
    private PasswordField password;
    @FXML
    private Button login;

    @FXML
    protected void onLoginButtonClick() {

        String username = this.username.getText();
        String password = this.password.getText();

        try (Connection conn = DatabaseUtil.getConnection()) {
            int iduser = DatabaseUtil.checkUserCredentials(conn, username, password);

            if (iduser == -1) {
                showAlert(Alert.AlertType.ERROR, "Login", "Login failed", "Check your credentials and try again.");
            } else {
                showAlert(Alert.AlertType.INFORMATION, "Login", "Login Successful!", "User ID is: " + iduser);

                /* Debugging: Print resource path
                System.out.println("Loading mainPanel.fxml from: " + getClass().getResource("mainPanel.fxml"));
                System.out.println("Resource for mainPanel.fxml: " + WorkOrderAppController.class.getResource("mainPanel.fxml"));
                System.out.println("Resource for workOrderGUI.fxml: " + WorkOrderAppController.class.getResource("workOrderGUI.fxml"));
                 */

                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("mainPanel.fxml"));
                Scene scene = new Scene(fxmlLoader.load());
                Stage stage = (Stage) login.getScene().getWindow();
                MainPanelController mainController = fxmlLoader.getController();

                /* Debugging: Check if mainController is loaded
                if (mainController == null) {
                    System.out.println("mainController is null");
                } else {
                    System.out.println("mainController is successfully loaded");

                }

                 */

                stage.setScene(scene);
                stage.setTitle("Main Panel");
                stage.show();
            }
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Database Error", "Something went wrong with the database", e.getMessage());
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Loading Error", "Unable to load main panel", e.getMessage());
        }
    }

    private void showAlert(Alert.AlertType alertType, String title, String header, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
