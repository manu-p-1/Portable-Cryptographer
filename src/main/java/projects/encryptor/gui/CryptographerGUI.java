package projects.encryptor.gui;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class CryptographerGUI extends Application {
	
	private Stage stage;
	private final VBox root = new VBox();						
	
	@Override
	public void start(Stage stage) {
		this.stage = stage;
		root.setPrefSize(500, 300);
		root.getChildren().addAll(loadMenubar());
		root.getChildren().add(loadContents());
		Scene scene = new Scene(root);
		stage.setResizable(false);
		stage.setTitle("Portable Cryptographer");
		stage.setScene(scene);
		stage.sizeToScene();
		stage.show();		
	}

	private MenuBar loadMenubar() {
		final MenuBar menuBar = new MenuBar();
		final Menu menu1 = new Menu("File");
		final MenuItem fileExit = new MenuItem("Exit");
		final Menu menu4 = new Menu("Help");
		final MenuItem helpAbout = new MenuItem("About");
		menuBar.getMenus().addAll(menu1, menu4);
		// Adds the MenuItem's to all Menu's
		menu1.getItems().add(fileExit);
		menu4.getItems().add(helpAbout);
		// Adds the menuBar to the VBox root
		fileExit.setOnAction(event -> {
			Platform.exit();
			System.exit(0);
		});	
		return menuBar;
//		helpAbout.setOnAction(event -> about());
	}
	
	private HBox loadContents() {
		HBox contentPane = new HBox(10);
		Button encrypt = new Button("Encrpyt");
		encrypt.setStyle("-fx-font-size: 30");
		encrypt.setPrefSize(200, 200);
		Button decrypt = new Button("Decrypt");
		decrypt.setStyle("-fx-font-size: 30");
		decrypt.setPrefSize(200, 200);
		contentPane.getChildren().addAll(encrypt, decrypt);
		contentPane.setAlignment(Pos.CENTER);
		contentPane.setPadding(new Insets(30));
		buttonActions(encrypt, decrypt); // Initiates the button actions for the games
		return contentPane;
	}
	
	private void buttonActions(Button encrypt, Button decrypt) {
		encrypt.setOnAction(e -> new EncryptActionGUI(stage).start());
		decrypt.setOnAction(e -> new DecryptActionGUI(stage).start());
	}

	public static void main(String[] args) {
		try {
			Application.launch(args);
		} catch (UnsupportedOperationException e) {
			System.out.println(e);
			System.exit(1);
		} // try
	}
}
