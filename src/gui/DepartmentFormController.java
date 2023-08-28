package gui;

import java.net.URL;
import java.util.ResourceBundle;

import db.DBException;
import gui.util.Alerts;
import gui.util.Constraints;
import gui.util.Utils;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import model.entities.Department;
import model.services.DepartmentService;

public class DepartmentFormController implements Initializable{

	private Department entity;
	
	private DepartmentService service;
	
	@FXML
	private TextField txtId ;
	
	@FXML
	private TextField txtName;
	
	@FXML
	private Label labelErrorName;
	
	@FXML
	private Button btSave;
	
	@FXML
	private Button btCancel;
	
	public void setDepartment(Department entity) {
		this.entity = entity;
	}
	
	public void setDepartmentService(DepartmentService service) {
		this.service = service;
	}
	
	@FXML
	public void onBtSaveAction() {
		if(entity == null) {
			Alerts.showAlert("Exception", "Entity was null", null, AlertType.ERROR);
		}
		if(service == null) {
			Alerts.showAlert("Exception", "Service was null", null, AlertType.ERROR);
		}
		try {
		entity = getFormData();
		service.saveOrUpdate(entity);
		} catch(DBException e) {
			Alerts.showAlert("DB Exception", e.getMessage(), null, AlertType.ERROR);
		}
	}
	
	private Department getFormData() {
		Department dep = new Department();
		dep.setId(Utils.tryParseToInteger(txtId.getText()));
		dep.setName(txtName.getText());
		return dep;
	}

	@FXML
	public void onBtCancelAction() {
		System.out.println("Canceled");
	}
	
	@Override
	public void initialize(URL url, ResourceBundle rb) {
		inicializeNodes();
	}
	
	private void inicializeNodes() {
		Constraints.setTextFieldInteger(txtId);
		Constraints.setTextFieldMaxLenght(txtName, 10);
		Constraints.setTextFieldString(txtName);
	}

	public void updateFormData() {
		if(entity == null) {
			Alerts.showAlert("Exception", "Entity was null", null, AlertType.ERROR);
		}
		txtId.setText(String.valueOf(entity.getId()));
		txtName.setText(entity.getName());
		
	}
}
