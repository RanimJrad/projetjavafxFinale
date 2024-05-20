package application;

import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ResourceBundle;

import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

public class SignInController implements Initializable {

	@FXML
	private AnchorPane main_form;

	@FXML
	private TextField username;

	@FXML
	private PasswordField password;

	@FXML
	private Button loginBtn;

	@FXML
	private Button close;

	@FXML
	private AnchorPane su_signupForm;

	@FXML
	private TextField su_username;

	@FXML
	private PasswordField su_password;

	@FXML
	private Button su_signupBtn;

	@FXML
	private Button side_alreadyHave;

	@FXML
	private Button side_CreateBtn;

	@FXML
	private AnchorPane side_form;

	private Connection connect;
	private PreparedStatement prepare;
	private ResultSet result;

	private double x = 0;
	private double y = 0;

	private Alert alert;

	public void regBtn() {

		if (su_username.getText().isEmpty() || su_password.getText().isEmpty()) {
			alert = new Alert(AlertType.ERROR);
			alert.setTitle("Error Message");
			alert.setHeaderText(null);
			alert.setContentText("Please fill all blank fields");
			alert.showAndWait();
		} else {
			String regData = "Insert into admin(username,password)" + " values (?,?)";
			connect = connexion.connectDb();
			try {

				prepare = connect.prepareStatement(regData);
				prepare.setString(1, su_username.getText());
				prepare.setString(2, su_password.getText());

				prepare.executeUpdate();

				alert = new Alert(AlertType.INFORMATION);
				alert.setTitle("Information Message");
				alert.setHeaderText(null);
				alert.setContentText("Successfully registred Account!");
				alert.showAndWait();

				su_username.setText("");
				su_password.setText("");

				TranslateTransition slider = new TranslateTransition();

				slider.setNode(side_form);
				slider.setToX(0);
				slider.setDuration(Duration.seconds(.5));

				slider.setOnFinished((ActionEvent e) -> {
					side_alreadyHave.setVisible(false);
					side_CreateBtn.setVisible(true);
				});
				slider.play();

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public void switchForm(ActionEvent event) {
		TranslateTransition slider = new TranslateTransition();

		if (event.getSource() == side_CreateBtn) {
			slider.setNode(side_form);
			slider.setToX(300);
			slider.setDuration(Duration.seconds(.5));

			slider.setOnFinished((ActionEvent e) -> {
				side_alreadyHave.setVisible(true);
				side_CreateBtn.setVisible(false);
			});
			slider.play();
		} else if (event.getSource() == side_alreadyHave) {
			slider.setNode(side_form);
			slider.setToX(0);
			slider.setDuration(Duration.seconds(.5));

			slider.setOnFinished((ActionEvent e) -> {
				side_alreadyHave.setVisible(false);
				side_CreateBtn.setVisible(true);
			});
			slider.play();
		}

	}

	public void loginAdmin() {

		connect = connexion.connectDb();
		String sql = "SELECT * FROM admin WHERE username = ? and password = ?";

		try {
			Alert alert;

			prepare = connect.prepareStatement(sql);
			prepare.setString(1, username.getText());
			prepare.setString(2, password.getText());

			result = prepare.executeQuery();

			if (username.getText().isEmpty() || password.getText().isEmpty()) {
				alert = new Alert(AlertType.ERROR);
				alert.setTitle("Error Message");
				alert.setHeaderText(null);
				alert.setContentText("Please fill all blank fields");
				alert.showAndWait();
			} else {
				if (result.next()) {

					getData.username = username.getText();

					alert = new Alert(AlertType.INFORMATION);
					alert.setTitle("Information Message");
					alert.setHeaderText(null);
					alert.setContentText("Successfully Login");
					alert.showAndWait();
					loginBtn.getScene().getWindow().hide();
					try {
						FileWriter outputFile = new FileWriter("D:\\fichiers\\users.txt");
						outputFile.write(username.getText());
						outputFile.write(" ");
						outputFile.write(password.getText());
						outputFile.close();

					} catch (IOException e) {
						e.printStackTrace();
					}

					if (username.getText().equals("admin")) {
						Parent root = FXMLLoader.load(getClass().getResource("/FXML/Home.FXML"));

						Stage stage = new Stage();
						Scene scene = new Scene(root);

						root.setOnMousePressed((MouseEvent event) -> {
							x = event.getSceneX();
							y = event.getSceneY();
						});

						root.setOnMouseDragged((MouseEvent event) -> {
							stage.setX(event.getScreenX() - x);
							stage.setY(event.getScreenY() - y);
						});

						stage.initStyle(StageStyle.TRANSPARENT);

						stage.setScene(scene);
						stage.show();
					} else {
						Parent root = FXMLLoader.load(getClass().getResource("/FXML/client.FXML"));

						Stage stage = new Stage();
						Scene scene = new Scene(root);

						root.setOnMousePressed((MouseEvent event) -> {
							x = event.getSceneX();
							y = event.getSceneY();
						});

						root.setOnMouseDragged((MouseEvent event) -> {
							stage.setX(event.getScreenX() - x);
							stage.setY(event.getScreenY() - y);
						});

						stage.initStyle(StageStyle.TRANSPARENT);

						stage.setScene(scene);
						stage.show();
					}

				} else { // IF WRONG USERNAME OR PASSWORD
					alert = new Alert(AlertType.ERROR);
					alert.setTitle("Error Message");
					alert.setHeaderText(null);
					alert.setContentText("Wrong Username/Password");
					alert.showAndWait();
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void close() {
		System.exit(0);
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub

	}

}
