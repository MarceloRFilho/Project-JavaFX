package gui;

import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import application.Main;
import db.DBIntegrityException;
import gui.listeners.DataChangeListener;
import gui.util.Alerts;
import gui.util.Utils;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.entities.Seller;
import model.services.SellerService;

public class SellerController implements Initializable, DataChangeListener{

	private SellerService service;
	private ObservableList<Seller> obsList;
	
	@FXML
	private TableColumn<Seller, Seller> tbcEdit;
	
	@FXML
	private TableColumn<Seller, Seller> tbcRemove;
	
	@FXML
	private VBox vbSeller;
	
	@FXML
	private TableView<Seller> tableViewSeller;
	
	@FXML
	private TableColumn<Seller, Integer> tbcId;
	
	@FXML
	private TableColumn<Seller, String> tbcName;
	
	@FXML
	private Button btNew;
	
	public void onBTNewAction(ActionEvent event) {
		Stage parentStage = Utils.currentStage(event);
		Seller obj = new Seller();
		createDialogForm(obj, "SellerForm.fxml", parentStage);
	}
	
	public void setSellerService(SellerService service) {
		this.service= service;
	}

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		initializeNodes();
	}

	private void initializeNodes() {
		tbcId.setCellValueFactory(new PropertyValueFactory<>("id"));
		tbcName.setCellValueFactory(new PropertyValueFactory<>("name"));
		
		Stage stage = (Stage) Main.getMainScene().getWindow();
		tableViewSeller.prefHeightProperty().bind(stage.heightProperty());
	}
	
	public void updateTableView() {
		if (service == null) {
			Alerts.showAlert("Exception", "Entity was null", null, AlertType.ERROR);
		}
		
		List<Seller> list = service.findAll();
		obsList = FXCollections.observableArrayList(list);
		tableViewSeller.setItems(obsList);
		initEditButtons();
		initRemoveButtons();
	}
	
	private void createDialogForm(Seller obj, String absoluteName, Stage parentStage) {
//		try {
//			FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
//			Pane pane = loader.load();
//			
//			SellerFormController controller = loader.getController();
//			controller.setSeller(obj);
//			controller.setSellerService(new SellerService());
//			controller.subDataChangeListener(this);
//			controller.updateFormData();
//			
//			Stage dialogStage = new Stage();
//			dialogStage.setTitle("Enter Seller Data");
//			dialogStage.setScene(new Scene(pane));
//			dialogStage.setResizable(false);
//			dialogStage.initOwner(parentStage);
//			dialogStage.initModality(Modality.WINDOW_MODAL);
//			dialogStage.showAndWait();
//			
//			
//		} catch(IOException e) {
//			Alerts.showAlert("IO Exception", e.getMessage(), null, AlertType.ERROR);
//		}
//		catch(RuntimeException e) {
//			Alerts.showAlert("Run Time Exception", e.getMessage(), null, AlertType.ERROR);
//		}
	}

	@Override
	public void onDataChanged() {
		updateTableView();
	}
	
	private void initEditButtons() {
		tbcEdit.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
		tbcEdit.setCellFactory(param -> new TableCell<Seller, Seller>(){
			private final Button button = new Button("edit");
			
			@Override
			protected void updateItem(Seller obj, boolean empty) {
				super.updateItem(obj,empty);
				 
				if(obj == null) {
					setGraphic(null);
					return;
				}
				setGraphic(button);
				button.setOnAction(event -> createDialogForm(obj, "/gui/SellerForm.fxml", Utils.currentStage(event)));
			}
		});
	}
	
	private void initRemoveButtons() {
		tbcRemove.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
		tbcRemove.setCellFactory(param -> new TableCell<Seller, Seller>(){
			private final Button button = new Button("remove");
			
			@Override
			protected void updateItem(Seller obj, boolean empty) {
				super.updateItem(obj,empty);
				 
				if(obj == null) {
					setGraphic(null);
					return;
				}
				setGraphic(button);
				button.setOnAction(event -> removeEntity(obj));
			}
		});
	}

	private void removeEntity(Seller obj) {
		Optional<ButtonType> confirm = Alerts.showConfirmation("Comfirmation", "Are you sure to delete?");
		
		if(confirm.get() == ButtonType.OK) {
			if(service == null) {
				throw new IllegalStateException("No service found");
			}
			try {
				service.remove(obj);
				updateTableView();
			} catch(DBIntegrityException e) {
				Alerts.showAlert("Deleting Error", e.getMessage(), null, AlertType.ERROR);
			}
		}
	}
}
