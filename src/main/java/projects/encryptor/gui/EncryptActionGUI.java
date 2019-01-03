package projects.encryptor.gui;

import java.security.NoSuchAlgorithmException;
import javax.crypto.NoSuchPaddingException;
import javafx.application.Platform;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import projects.encrpytor.BasicCryptosystem;
import projects.encrpytor.Cryptographer;

public class EncryptActionGUI {

	private Stage crptographerGUI;
	private VBox root;
	private BasicCryptosystem encrpytable;
	
	public EncryptActionGUI(Stage stage) {
		try {
			encrpytable = new Cryptographer();
		} catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
			e.printStackTrace();
		}
		this.crptographerGUI = stage;
		root = new VBox();
	}
	
	public void start() {
		Stage stage = new Stage();
		root.setPrefSize(900, 700);
		
		loadMenubar(stage);
		loadTextArea();

		Scene scene = new Scene(root);
		stage.setTitle("Encryptor");
		stage.setScene(scene);
		stage.initOwner(crptographerGUI);
		stage.initModality(Modality.WINDOW_MODAL);
		stage.setResizable(false);
		stage.sizeToScene();
		stage.show();
	}
	
	private void loadMenubar(Stage stage) {
		final MenuBar menuBar = new MenuBar();
		final Menu menu1 = new Menu("File");
		final MenuItem returnToMenu = new MenuItem("Return to Menu");
		final MenuItem fileExit = new MenuItem("Exit All");
		menuBar.getMenus().addAll(menu1);
		// Adds the MenuItem's to all Menu's
		menu1.getItems().addAll(returnToMenu, fileExit);
		// Adds the menuBar to the VBox root
		root.getChildren().addAll(menuBar);
		returnToMenu.setOnAction(event -> stage.close());
		fileExit.setOnAction(event -> {
			Platform.exit();
			System.exit(0);
		});
	}
	
	private void loadTextArea() {
		TextArea taPlainText = new TextArea("Enter Text:");
		taPlainText.setPrefSize(800, 300);
		taPlainText.setFocusTraversable(false);
		taPlainText.setWrapText(true);
		taPlainText.setStyle("-fx-font-size: 22");
		TextArea taEncrpytedResult = new TextArea("Result:");
		taEncrpytedResult.setPrefSize(800, 250);
		taEncrpytedResult.setEditable(false);
		taEncrpytedResult.setFocusTraversable(false);
		taEncrpytedResult.setWrapText(true);
		taEncrpytedResult.setStyle("-fx-font-size: 22");
		
		VBox textContent = new VBox(10);
		textContent.setPadding(new Insets(20));
		textContent.getChildren().addAll(taPlainText, taEncrpytedResult, loadButtonBar(taPlainText, taEncrpytedResult));
		root.getChildren().add(textContent);
	
		taPlainText.setOnMouseClicked(event -> {
			if(taPlainText.getText().equals("Enter Text:")) taPlainText.setText("");
		});
	}
	
	private HBox loadButtonBar(TextArea taPlainText, TextArea taEncrpytedResult) {
		HBox buttonBar = new HBox(5);
		buttonBar.setPrefWidth(800);
		HBox relaventButtons = new HBox(5);	
		HBox buttonUtils = new HBox(5);

		Button encrypt = new Button("Encrypt");
		encrypt.setPrefSize(100, 50);
		relaventButtons.setAlignment(Pos.CENTER_RIGHT);
		
		Button copy = new Button("Copy Result To Clipboard");
		Button reset = new Button("Reset");
		reset.setPrefSize(100, 50);
		copy.setPrefSize(200, 50);
		buttonUtils.setAlignment(Pos.CENTER_LEFT);
		
		buttonUtils.getChildren().addAll(copy, reset);
		relaventButtons.getChildren().addAll(encrypt);
		buttonBar.getChildren().addAll(buttonUtils, relaventButtons);

		copy.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				final Clipboard clipboard = Clipboard.getSystemClipboard();
			    final ClipboardContent content = new ClipboardContent();
			    content.putString(taEncrpytedResult.getText());
			    clipboard.setContent(content);
			    copy.setText("Copied!");
			}
		});
			
		reset.setOnAction(event -> {
			 taPlainText.setText("Enter Text:");
			 taEncrpytedResult.setText("Result:");
			 copy.setText("Copy Result To Clipboard");
		});
		
		encrypt.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				try {
					String encryptedResult = encrpytable.encrypt(taPlainText.getText());
					taEncrpytedResult.setText(encryptedResult);
					copy.setText("Copy Result To Clipboard");
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});	
		return buttonBar;
	}
}
