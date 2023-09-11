package gui;

import java.net.URL;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

import db.DBException;
import gui.listeners.DataChangeListener;
import gui.util.Alerts;
import gui.util.Constraints;
import gui.util.Utils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.util.Callback;
import model.entities.Department;
import model.entities.Seller;
import model.exceptions.ValidationException;
import model.services.DepartmentService;
import model.services.SellerService;

public class SellerFormController implements Initializable {

	private Seller entity;

	private SellerService service;

	private List<DataChangeListener> dChangeListeners = new ArrayList<>();

	private DepartmentService departmentService;

	@FXML
	private TextField txtId;

	@FXML
	private TextField txtName;

	@FXML
	private TextField txtEmail;

	@FXML
	private DatePicker dpBDate;

	@FXML
	private TextField txtSalary;

	@FXML
	private ComboBox<Department> cbDepartment;

	private ObservableList<Department> obsList;

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

	public void setServices(SellerService service, DepartmentService departmentService) {
		this.service = service;
		this.departmentService = departmentService;
	}

	public void subDataChangeListener(DataChangeListener listener) {
		dChangeListeners.add(listener);
	}

	@FXML
	public void onBtSaveAction(ActionEvent event) {
		if (entity == null) {
			Alerts.showAlert("Exception", "Entity was null", null, AlertType.ERROR);
		}
		if (service == null) {
			Alerts.showAlert("Exception", "Service was null", null, AlertType.ERROR);
		}
		try {
			entity = getFormData();
			service.saveOrUpdate(entity);
			notifyDataChangeListeners();
			Utils.currentStage(event).close();
		} catch (DBException e) {
			Alerts.showAlert("DB Exception", e.getMessage(), null, AlertType.ERROR);
		} catch (ValidationException e) {
			setErrorName(e.getErrors());
		}
	}

	@FXML
	public void onBtCancelAction(ActionEvent event) {
		Utils.currentStage(event).close();
	}

	private void notifyDataChangeListeners() {
		for (DataChangeListener listeners : dChangeListeners) {
			listeners.onDataChanged();
		}
	}

	private Seller getFormData() {
		Seller dep = new Seller();
		ValidationException exception = new ValidationException("Validation Error");
		dep.setId(Utils.tryParseToInteger(txtId.getText()));

		if (txtName.getText() == null || txtName.getText().trim().equals("")) {
			exception.addError("name", "Field cannot be empty");
		}
		dep.setName(txtName.getText());
		
		if (txtEmail.getText() == null || txtEmail.getText().trim().equals("")) {
			exception.addError("email", "Field cannot be empty");
		}
		dep.setEmail(txtEmail.getText());
		
		if (dpBDate.getValue() == null)	{
			exception.addError("bDate", "Field cannot be empty");
		} else {
			Instant instant = Instant.from(dpBDate.getValue().atStartOfDay(ZoneId.systemDefault()));
			dep.setbDate(Date.from(instant));
		}
		
		if (exception.getErrors().size() > 0) {
			throw exception;
		}
		
		if (txtSalary.getText() == null || txtSalary.getText().trim().equals("")) {
			exception.addError("salary", "Field cannot be empty");
		}
		dep.setSalary(Utils.tryParseToDouble(txtSalary.getText()));
		
		dep.setDepartment(cbDepartment.getValue());
		
		if(exception.getErrors().size() > 0) {
			throw exception;
		}

		return dep;
	}

	public void loadAssociatedObjects() {
		if (departmentService == null) {
			throw new IllegalStateException("Departmente Service is null");
		}
		List<Department> list = departmentService.findAll();
		obsList = FXCollections.observableArrayList(list);
		cbDepartment.setItems(obsList);
	}

	private void setErrorName(Map<String, String> errors) {
		Set<String> fields = errors.keySet();
		
		labelErrorName.setText((fields.contains("name") ? errors.get("name") : ""));
		labelErrorEmail.setText((fields.contains("email") ? errors.get("email") : ""));
		labelErrorBDate.setText((fields.contains("bDate") ? errors.get("bDate") : ""));
		labelErrorSalary.setText((fields.contains("salary") ? errors.get("salary") : ""));
		
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
		initializeComboBoxDepartment();
	}

	public void updateFormData() {
		if (entity == null) {
			Alerts.showAlert("Exception", "Entity was null", null, AlertType.ERROR);
		}
		txtId.setText(String.valueOf(entity.getId()));
		txtName.setText(entity.getName());
		txtEmail.setText(entity.getEmail());
		if (entity.getbDate() != null) {
			dpBDate.setValue(LocalDate.ofInstant(entity.getbDate().toInstant(), ZoneId.systemDefault()));
		}
		if (entity.getSalary() != null) {
			txtSalary.setText(String.format("%.2f", entity.getSalary()));
		}
		if(entity.getDepartment() == null) {
			cbDepartment.getSelectionModel().selectFirst();
		} else {
			cbDepartment.setValue(entity.getDepartment());
		}
		
	}

	private void initializeComboBoxDepartment() {
		Callback<ListView<Department>, ListCell<Department>> factory = lv -> new ListCell<Department>() {
			@Override
			protected void updateItem(Department item, boolean empty) {
				super.updateItem(item, empty);
				setText(empty ? "" : item.getName());
			}
		};
		cbDepartment.setCellFactory(factory);
		cbDepartment.setButtonCell(factory.call(null));
	}
}
