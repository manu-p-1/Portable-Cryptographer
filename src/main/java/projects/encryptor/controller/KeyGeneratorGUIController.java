package projects.encryptor.controller;

import java.security.NoSuchAlgorithmException;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;

import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import projects.encryptor.model.BasicCryptosystem;

/**
 * The Controller for the Key generator GUI on startup.
 * 
 * @author Manu Puduvalli
 *
 */
public class KeyGeneratorGUIController extends AbstractController{

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
	
	public KeyGeneratorGUIController() {
		System.out.println("Key Gen Ctor");
	}
	
	/**
	 * Generates the dialog
	 * 
	 * @param owner the owner of the key generator 
	 * @param p the root of the this Controller
	 * @return a new Stage containing the necessary elements
	 */
	public Stage generateDialog(Stage owner, Parent p) {
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
		String key = BasicCryptosystem.generateSecretKey(BasicCryptosystem.ALGORITHM_AES);
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
	
	/**
	 * Returns the secret key that was generated by this Controller.
	 * 
	 * @return a secret key as a String
	 */
	public String getStoredSecureKey() {
		return storedSecureKey;
	}
}//Key Gui;