package gui;


import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.Consumer;

import application.Main;
import gui.util.Alerts;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import model.services.DepartmentService;
import model.services.SellerService;

public class MainViewController implements Initializable{
	
	@FXML
	private MenuItem miSeller;
	
	@FXML
	private MenuItem miDepartment;
	
	@FXML
	private MenuItem miAbout;
	
	@FXML
	public void onMISellerAction() {
		loadView("/gui/SellerList.fxml", (SellerController controller) -> {
			controller.setSellerService(new SellerService());
			controller.updateTableView();
		});
	}
	
	@FXML
	public void onMIDepartmentAction() {
		loadView("/gui/DepartmentList.fxml", (DepartmentController controller) -> {
			controller.setDepartmentService(new DepartmentService());
			controller.updateTableView();
		});
	}
	
	@FXML
	public void onMIAboutAction() {
		loadView("/gui/AboutView.fxml", x -> {});
	}
	
	@Override
	public void initialize(URL url, ResourceBundle rb) {
		// TODO Auto-generated method stub
		
	}
	
	private synchronized <T> void loadView(String absoluteName, Consumer<T> initializingAction) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
			VBox newVBox = loader.load();
			
			Scene mainScene = Main.getMainScene();
			VBox mainVBox = (VBox)((ScrollPane)mainScene.getRoot()).getContent();
			
			Node mainMenu = mainVBox.getChildren().get(0);
			mainVBox.getChildren().clear();
			mainVBox.getChildren().add(mainMenu);
			mainVBox.getChildren().addAll(newVBox);
			
			T controller = loader.getController();
			initializingAction.accept(controller);
			
		} catch(IOException e) {
			Alerts.showAlert("IO Exception", e.getMessage(), null, AlertType.ERROR);
		}
		catch(IllegalStateException e) {
			Alerts.showAlert("Illegal State Exception", "Couldn't load view", e.getMessage(), AlertType.ERROR);
		}
		catch(RuntimeException e) {
			Alerts.showAlert("Ops, something wrong", e.getMessage(), null, AlertType.ERROR);
			e.printStackTrace();

		}
	}

}
