package projects.encryptor.gui;

import java.security.NoSuchAlgorithmException;
import javax.crypto.NoSuchPaddingException;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import projects.encryptor.BasicCryptosystem;
import projects.encryptor.Cryptographer;

public class CryptographerGUI extends Application {

	private final VBox root = new VBox();
	private BasicCryptosystem basicCryptosystem;
	private TextArea taPlainText;
	private TextArea taCrypticResult;
	private Button cryptosystemVariable;
	private final static int DEFAULT_BORDER_INSET_SPACING = 20;
	private boolean isEncryptMode = true;
	
	@Override
	public void start(Stage stage) {
		try {
			basicCryptosystem = new Cryptographer();
		} catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
			System.err.println(e + " occured.");
			Platform.exit();
			System.exit(1);
		}
		root.setPrefSize(900, 700);
		root.getChildren().add(loadMenubar(stage));
		root.getChildren().add(loadTextArea());
		root.getChildren().add(loadButtonBar());
		root.setStyle("-fx-background-color: linear-gradient(#999999, #595959, #999999);");
		Scene scene = new Scene(root);
		stage.setTitle("Portable Cryptographer");
		stage.setScene(scene);
		stage.setResizable(false);
		stage.sizeToScene();
		stage.show();	
	}
	
	private MenuBar loadMenubar(Stage stage) {
		final MenuBar menuBar = new MenuBar();
		final Menu $fileMenu = new Menu("File");
		final MenuItem fileExit = new MenuItem("Exit");
		final Menu $modeMenu = new Menu("Mode");
		final CheckMenuItem encrypt = new CheckMenuItem("Encrypt");
		final CheckMenuItem decrypt = new CheckMenuItem("Decrypt");	
		encrypt.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				isEncryptMode = true;
				decrypt.setSelected(false);
				Platform.runLater(() -> {
					taPlainText.setText("Enter Text:");
					taCrypticResult.setText("Result:");
					cryptosystemVariable.setText("Encrypt");
				});
			}
		});
		decrypt.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				isEncryptMode = false;
				encrypt.setSelected(false);
				Platform.runLater(() -> {
					taPlainText.setText("Enter Encrypted Text:");
					taCrypticResult.setText("Result:");
					cryptosystemVariable.setText("Decrypt");
				});
			}
		});	
		encrypt.setSelected(true);
		final Menu $helpMenu = new Menu("Help");
		final MenuItem helpAbout = new MenuItem("About");
		menuBar.getMenus().addAll($fileMenu, $modeMenu, $helpMenu);
		// Adds the MenuItem's to all Menu's
		$fileMenu.getItems().add(fileExit);
		$modeMenu.getItems().addAll(encrypt, decrypt);
		$helpMenu.getItems().add(helpAbout);
		fileExit.setOnAction(event -> {
			Platform.exit();
			System.exit(0);
		});	
		menuBar.setStyle(
				"-fx-background-color: linear-gradient(to bottom, #808080, #999999)");
		return menuBar;
	}
	
	private VBox loadTextArea() {
		taPlainText = new TextArea("Enter Text:");
		taPlainText.setPrefSize(800, 300);
		taPlainText.setFocusTraversable(false);
		taPlainText.setWrapText(true);
		taPlainText.setStyle("-fx-font-size: 22; "
				+ "-fx-control-inner-background: #cccccc");
		taCrypticResult = new TextArea("Result:");
		taCrypticResult.setPrefSize(800, 250);
		taCrypticResult.setEditable(false);
		taCrypticResult.setFocusTraversable(false);
		taCrypticResult.setWrapText(true);
		taCrypticResult.setStyle("-fx-font-size: 22; "
				+ "-fx-control-inner-background: #cccccc");
		VBox textContent = new VBox(10);
		textContent.setPadding(new Insets(DEFAULT_BORDER_INSET_SPACING));
		textContent.getChildren().addAll(taPlainText, taCrypticResult);
		taPlainText.setOnMouseClicked(event -> {
			if(taPlainText.getText().equals("Enter Text:") || 
					taPlainText.getText().equals("Enter Encrypted Text:")) {
				taPlainText.setText("");
			}
		});
		return textContent;
	}
	
	private BorderPane loadButtonBar() {
		BorderPane buttonBar = new BorderPane();
		buttonBar.setPrefWidth(800);
		buttonBar.setPadding(new Insets(DEFAULT_BORDER_INSET_SPACING));
		HBox relaventButtons = new HBox(5);	
		cryptosystemVariable = new Button("Encrypt");
		cryptosystemVariable.setPrefSize(100, 50);
		relaventButtons.getChildren().add(cryptosystemVariable);
		buttonBar.setRight(relaventButtons);		
		HBox buttonUtils = new HBox(5);
		Button copy = new Button("Copy Result");
		Button paste = new Button("Paste From Clipboard");
		Button reset = new Button("Reset");
		reset.setPrefSize(100, 50);
		copy.setPrefSize(175, 50);
		paste.setPrefSize(175, 50);
		buttonUtils.getChildren().addAll(copy, paste, reset);
		buttonBar.setLeft(buttonUtils);		
		final Clipboard clipboard = Clipboard.getSystemClipboard();
	    final ClipboardContent content = new ClipboardContent();
		copy.setOnAction(event -> {
				content.putString(taCrypticResult.getText());
			    clipboard.setContent(content);
			    copy.setText("Copied!");			
		});		
		paste.setOnAction(event -> {
			    taPlainText.setText(clipboard.getString());
			    paste.setText("Pasted!");
		});		
		reset.setOnAction(event -> {
			 taPlainText.setText("Enter Text:");
			 taCrypticResult.setText("Result:");
			 copy.setText("Copy Result");
			 paste.setText("Paste From Clipboard");
		});
		cryptosystemVariable.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				try {
					if(isEncryptMode) {
						String encryptedResult = basicCryptosystem.encrypt(taPlainText.getText());
						taCrypticResult.setText(encryptedResult);
					} else {
						String decryptedResult = basicCryptosystem.decrypt(taPlainText.getText());
						taCrypticResult.setText(decryptedResult);
					}
					copy.setText("Copy Result");
					paste.setText("Paste From Clipboard");
				} catch (Exception e) {
					alert(taPlainText.getText());
				}
			}
		});	
		return buttonBar;
	}
	
	private void alert(String expression) {
		Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle("Error");
		alert.setHeaderText("Invalid Entry");
		alert.setContentText("\"" + expression + "\""
				+ " is not a valid entry.");
		alert.showAndWait();
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