package gui;

import java.net.URL;
import java.util.ResourceBundle;

import application.Main;
import gui.util.Alerts;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.entities.Department;

public class DepartmentController implements Initializable{

	@FXML
	private TableView<Department> tbvDepartment;
	
	@FXML
	private TableColumn<Department, Integer> tbcId;
	
	@FXML
	private TableColumn<Department, String> tbcName;
	
	@FXML
	private Button btNew;
	
	public void onBTNewAction() {
		Alerts.showAlert("Working", null, "Test", AlertType.CONFIRMATION);
	}

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		initializeNodes();
	}

	private void initializeNodes() {
		tbcId.setCellValueFactory(new PropertyValueFactory<>("id"));
		tbcName.setCellValueFactory(new PropertyValueFactory<>("name"));
		
		Stage stage = (Stage) Main.getMainScene().getWindow();
		tbvDepartment.prefHeightProperty().bind(stage.heightProperty());

	}
	
	
}
