package projects.encryptor.gui;

import java.io.IOException;
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

public class Driver extends Application {
	
	@Override
	public void start(Stage primaryStage) throws IOException {
	
		FXMLLoader loader = new FXMLLoader(getClass().getResource("CryptographerGUI.fxml"));
		Parent root = (Parent)loader.load();
		CryptographerGUIController cgc = (CryptographerGUIController)loader.getController();
		cgc.setStageAndSetupListeners(primaryStage);
		Scene scene = new Scene(root);
		primaryStage.setScene(scene);
		primaryStage.setTitle("Portable Cryptographer");
		primaryStage.setResizable(false);
		primaryStage.show();
	}
	
	public static void main(String[] args) {
		Application.launch(args);
	}
}
