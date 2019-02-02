package projects.encryptor.view;

import projects.encryptor.controller.*;

import java.io.IOException;
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;

public class PortableCryptographerView extends Application {
	
	@Override
	public void start(Stage primaryStage) throws IOException {	
		FXMLLoader fxml_loader = new FXMLLoader(getClass().getResource("CryptographerGUI.fxml"));
		//System.out.println(getClass().getResource("CryptographerGUI.fxml"));
		Parent root = (Parent) fxml_loader.load();
		CryptographerGUIController cgc = (CryptographerGUIController) fxml_loader.getController();
		cgc.setStageAndSetupListeners(primaryStage);
		Scene scene = new Scene(root);
		primaryStage.setScene(scene);
		primaryStage.setTitle("Portable Cryptographer");
		primaryStage.setResizable(false);
		primaryStage.getIcons().add(new Image(
				"/projects/encryptor/view/Application_Icons/mainApplicationIcon.png"));
		primaryStage.show();
	}
	
	public static void main(String[] args) {
		Application.launch(args);
	}
}
