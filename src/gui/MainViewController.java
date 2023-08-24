package gui;


import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

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

public class MainViewController implements Initializable{
	
	@FXML
	private MenuItem miSeller;
	
	@FXML
	private MenuItem miDepartment;
	
	@FXML
	private MenuItem miAbout;
	
	@FXML
	public void onMISellerAction() {
		System.out.println("MI Seller");
	}
	
	@FXML
	public void onMIDepartmentAction() {
		loadView("/gui/DepartmentList.fxml");
	}
	
	@FXML
	public void onMIAboutAction() {
		loadView("/gui/AboutView.fxml");
	}
	
	@Override
	public void initialize(URL url, ResourceBundle rb) {
		// TODO Auto-generated method stub
		
	}
	
	private synchronized void loadView(String absoluteName) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
			VBox newVBox = loader.load();
			
			Scene mainScene = Main.getMainScene();
			VBox mainVBox = (VBox)((ScrollPane)mainScene.getRoot()).getContent();
			
			Node mainMenu = mainVBox.getChildren().get(0);
			mainVBox.getChildren().clear();
			mainVBox.getChildren().add(mainMenu);
			mainVBox.getChildren().addAll(newVBox);
			
		} catch(IOException e) {
			Alerts.showAlert("IO Exception", e.getMessage(), null, AlertType.ERROR);
		}
		catch(IllegalStateException e) {
			Alerts.showAlert("Illegal State Exception", "Couldn't load view", e.getMessage(), AlertType.ERROR);
		}
	}

}
