package gui;


import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.MenuItem;

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
		System.out.println("MI Department");
	}
	
	@FXML
	public void onMIAboutAction() {
		System.out.println("MI About");
	}
	
	
	

	@Override
	public void initialize(URL uri, ResourceBundle rb) {
		// TODO Auto-generated method stub
		
	}

}
