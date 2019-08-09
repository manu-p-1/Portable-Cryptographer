package projects.encryptor.controller;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;

import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import projects.encryptor.model.BasicCryptosystem;

public class KeyGeneratorGUIController extends AbstractBaseController{

	@FXML private AnchorPane root;
	@FXML private JFXButton generateBtn;
	@FXML private JFXButton cancelBtn;
	@FXML private JFXButton saveBtn;
	@FXML private JFXButton enterBtn;
	@FXML private JFXPasswordField keyTf;
	@FXML private JFXButton cpyClipBtn;
	@FXML private JFXButton pstClipBtn;
	private Stage stage;
	private String storedSecureKey;
	private final Clipboard clipboard = Clipboard.getSystemClipboard();
	private final ClipboardContent content = new ClipboardContent();
	
	public KeyGeneratorGUIController() {
		System.out.println("Key Gen Ctor");
	}
	
	public Stage generateDialog(Stage owner, Parent p) throws IOException {
		this.stage = new Stage();
		Scene scene = new Scene(p);
		stage.setScene(scene);
		stage.setTitle("Portable Cryptographer Key Generator");
		stage.setResizable(false);
		stage.initOwner(owner);
		stage.initModality(Modality.WINDOW_MODAL);
		stage.requestFocus();
		stage.setAlwaysOnTop(true);
		stage.setOnCloseRequest(e -> baseExit());
		stage.getIcons().add(new Image(
				"/projects/encryptor/view/Application_Icons/mainApplicationIcon.png"));
		return this.stage;
	}
		
	@FXML protected void generateBtnOnAction() throws NoSuchAlgorithmException {
		String key = BasicCryptosystem.generateSecureKey(BasicCryptosystem.ALGORITHM_AES);
		System.out.println(key);
		keyTf.setText(key);
	}
	
	@FXML protected void cpyClipBtnOnAction() {
		content.putString(keyTf.getText());
	    clipboard.setContent(content);
	    buttonTextTimer(cpyClipBtn, "Copy to Clipboard", "Copied!");
	}
	
	@FXML protected void pstClipBtnOnAction() {
		keyTf.setText(clipboard.getString());
	    buttonTextTimer(pstClipBtn, "Paste to Clipboard", "Pasted!");
	}
	
	@FXML protected void enterBtnOnAction() {
		storedSecureKey = keyTf.getText();
		stage.close();
	}
	
	@FXML protected void saveBtnOnAction() {
		commonFileSystem(root, keyTf);
	}

	@Override
	public void setupStageAndListeners(Stage p) {
		this.stage = p;
	}
	
	public String getStoredSecureKey() {
		return storedSecureKey;
	}
}//Key Gui;