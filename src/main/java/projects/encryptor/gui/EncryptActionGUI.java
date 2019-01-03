package projects.encryptor.gui;

import java.security.NoSuchAlgorithmException;
import javax.crypto.NoSuchPaddingException;
import javafx.application.Platform;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import projects.encryptor.BasicCryptosystem;
import projects.encryptor.Cryptographer;

public class EncryptActionGUI {

	private Stage crptographerGUI;
	private final VBox root = new VBox();
	private BasicCryptosystem encrpytable;
	private TextArea taPlainText;
	private TextArea taEncryptedResult;
	private final static int DEFAULT_BORDER_INSET_SPACING = 20;
	
	public EncryptActionGUI(Stage stage) {
		try {
			encrpytable = new Cryptographer();
		} catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
			e.printStackTrace();
		}
		this.crptographerGUI = stage;
	}
	
	public void start() {
		Stage stage = new Stage();
		root.setPrefSize(900, 700);
		
		root.getChildren().add(loadMenubar(stage));
		root.getChildren().add(loadTextArea());
		root.getChildren().add(loadButtonBar());

		Scene scene = new Scene(root);
		stage.setTitle("Encryptor");
		stage.setScene(scene);
		stage.initOwner(crptographerGUI);
		stage.initModality(Modality.WINDOW_MODAL);
		stage.setResizable(false);
		stage.sizeToScene();
		stage.show();
	}
	
	private MenuBar loadMenubar(Stage stage) {
		final MenuBar menuBar = new MenuBar();
		final Menu menu1 = new Menu("File");
		final MenuItem returnToMenu = new MenuItem("Return to Menu");
		final MenuItem fileExit = new MenuItem("Exit All");
		menuBar.getMenus().addAll(menu1);
		// Adds the MenuItem's to all Menu's
		menu1.getItems().addAll(returnToMenu, fileExit);
		// Adds the menuBar to the VBox root
		returnToMenu.setOnAction(event -> stage.close());
		fileExit.setOnAction(event -> {
			Platform.exit();
			System.exit(0);
		});
		return menuBar;
	}
	
	private VBox loadTextArea() {
		taPlainText = new TextArea("Enter Text:");
		taPlainText.setPrefSize(800, 300);
		taPlainText.setFocusTraversable(false);
		taPlainText.setWrapText(true);
		taPlainText.setStyle("-fx-font-size: 22");
		taEncryptedResult = new TextArea("Result:");
		taEncryptedResult.setPrefSize(800, 250);
		taEncryptedResult.setEditable(false);
		taEncryptedResult.setFocusTraversable(false);
		taEncryptedResult.setWrapText(true);
		taEncryptedResult.setStyle("-fx-font-size: 22");
		
		VBox textContent = new VBox(10);
		textContent.setPadding(new Insets(DEFAULT_BORDER_INSET_SPACING));
		textContent.getChildren().addAll(taPlainText, taEncryptedResult);
	
		taPlainText.setOnMouseClicked(event -> {
			if(taPlainText.getText().equals("Enter Text:")) taPlainText.setText("");
		});
		return textContent;
	}
	
	private BorderPane loadButtonBar() {
		BorderPane buttonBar = new BorderPane();
		buttonBar.setPrefWidth(800);
		buttonBar.setPadding(new Insets(DEFAULT_BORDER_INSET_SPACING));
		
		HBox relaventButtons = new HBox(5);	
		Button encrypt = new Button("Encrypt");
		encrypt.setPrefSize(100, 50);
		relaventButtons.getChildren().add(encrypt);
		buttonBar.setRight(relaventButtons);
		
		HBox buttonUtils = new HBox(5);
		Button copy = new Button("Copy Result To Clipboard");
		Button paste = new Button("Paste From Clipboard");
		Button reset = new Button("Reset");
		reset.setPrefSize(100, 50);
		copy.setPrefSize(175, 50);
		paste.setPrefSize(175, 50);
		buttonUtils.getChildren().addAll(copy, paste, reset);
		buttonBar.setLeft(buttonUtils);	
		
		final Clipboard clipboard = Clipboard.getSystemClipboard();
	    final ClipboardContent content = new ClipboardContent();
		copy.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				content.putString(taEncryptedResult.getText());
			    clipboard.setContent(content);
			    copy.setText("Copied!");
			}
		});
		
		paste.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
			    taPlainText.setText(clipboard.getString());
			    paste.setText("Pasted!");
			}
		});
			
		reset.setOnAction(event -> {
			 taPlainText.setText("Enter Text:");
			 taEncryptedResult.setText("Result:");
			 copy.setText("Copy Result To Clipboard");
			 paste.setText("Paste From Clipboard");
		});
		
		encrypt.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				try {
					String encryptedResult = encrpytable.encrypt(taPlainText.getText());
					taEncryptedResult.setText(encryptedResult);
					copy.setText("Copy Result To Clipboard");
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});	
		return buttonBar;
	}
	
	private void alert() {
		
	}
}
