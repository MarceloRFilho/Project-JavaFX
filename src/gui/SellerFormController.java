package gui;

import java.net.URL;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

import db.DBException;
import gui.listeners.DataChangeListener;
import gui.util.Alerts;
import gui.util.Constraints;
import gui.util.Utils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import model.entities.Seller;
import model.exceptions.ValidationException;
import model.services.SellerService;

public class SellerFormController implements Initializable{

	private Seller entity;
	
	private SellerService service;
	
	private List<DataChangeListener> dChangeListeners = new ArrayList<>();
	
	@FXML
	private TextField txtId ;
	
	@FXML
	private TextField txtName;
	
	@FXML
	private TextField txtEmail;
	
	@FXML
	private DatePicker dpBDate;
	
	@FXML
	private TextField txtSalary;
	
	@FXML
	private Label labelErrorName;
	
	@FXML
	private Label labelErrorEmail;
	
	@FXML
	private Label labelErrorBDate;
	
	@FXML
	private Label labelErrorSalary;
	
	@FXML
	private Button btSave;
	
	@FXML
	private Button btCancel;
	
	public void setSeller(Seller entity) {
		this.entity = entity;
	}
	
	public void setSellerService(SellerService service) {
		this.service = service;
	}
	
	public void subDataChangeListener(DataChangeListener listener) {
		dChangeListeners.add(listener);
	}
	
	@FXML
	public void onBtSaveAction(ActionEvent event) {
		if(entity == null) {
			Alerts.showAlert("Exception", "Entity was null", null, AlertType.ERROR);
		}
		if(service == null) {
			Alerts.showAlert("Exception", "Service was null", null, AlertType.ERROR);
		}
		try {
		entity = getFormData();
		service.saveOrUpdate(entity);
		notifyDataChangeListeners();
		Utils.currentStage(event).close();
		} catch(DBException e) {
			Alerts.showAlert("DB Exception", e.getMessage(), null, AlertType.ERROR);
		}
		catch (ValidationException e) {
			setErrorName(e.getErrors());
		}
	}
	
	@FXML
	public void onBtCancelAction(ActionEvent event) {
		Utils.currentStage(event).close();
	}
	
	private void notifyDataChangeListeners() {
		for(DataChangeListener listeners : dChangeListeners) {
			listeners.onDataChanged();
		}
	}

	private Seller getFormData() {
		Seller dep = new Seller();
		ValidationException exception = new ValidationException("Validation Error");
		dep.setId(Utils.tryParseToInteger(txtId.getText()));
		
		if(txtName.getText() == null || txtName.getText().trim().equals("")) {
			exception.addError("name", "Field cannot be empty");
		}
		dep.setName(txtName.getText());
		
		if(exception.getErrors().size() > 0) {
			throw exception;
		}
		
		return dep;
	}
	
	private void setErrorName(Map<String, String> errors) {
		Set<String> fields = errors.keySet();
		if (fields.contains("name")) {
			labelErrorName.setText(errors.get("name"));
		}
	}
	
	@Override
	public void initialize(URL url, ResourceBundle rb) {
		inicializeNodes();
	}
	
	private void inicializeNodes() {
		Constraints.setTextFieldInteger(txtId);
		Constraints.setTextFieldMaxLenght(txtName, 10);
		Constraints.setTextFieldString(txtName);
		Constraints.setTextFieldMaxLenght(txtEmail, 30);
		Constraints.setTextFieldDouble(txtSalary);
		Utils.formatDatePicker(dpBDate, "dd/MM/yyyy");
	}

	public void updateFormData() {
		if(entity == null) {
			Alerts.showAlert("Exception", "Entity was null", null, AlertType.ERROR);
		}
		txtId.setText(String.valueOf(entity.getId()));
		txtName.setText(entity.getName());
		txtEmail.setText(entity.getEmail());
		if(entity.getbDate() != null) {
		dpBDate.setValue(LocalDate.ofInstant(entity.getbDate().toInstant(), ZoneId.systemDefault()));
		}
		if(entity.getSalary() != null) {
			txtSalary.setText(String.format("%.2f" ,entity.getSalary()));
		}
	}
}
