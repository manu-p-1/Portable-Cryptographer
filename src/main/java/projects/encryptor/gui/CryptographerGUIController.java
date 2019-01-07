package projects.encryptor.gui;

import java.security.NoSuchAlgorithmException;
import javax.crypto.NoSuchPaddingException;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import projects.encryptor.*;

public class CryptographerGUIController {
	
	@FXML private CheckMenuItem modeEncrypt, modeDecrypt;
	@FXML private TextArea taPlainText, taCrypticResult;
	@FXML private Button copyBtn, pasteBtn, showHideTextBtn, resetBtn, cryptosystemVariableBtn;
	private Cryptographer cryptographer;
	private final Clipboard clipboard = Clipboard.getSystemClipboard();
	private final ClipboardContent content = new ClipboardContent();

	
	public CryptographerGUIController() {
		try {
			cryptographer = new Cryptographer();
		} catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
			System.err.println(e + " occured.");
			System.exit(1);
		}
	}
	
	public void closeMenuItemOnAction() {
		Platform.exit();
		System.exit(0);
	}
	
	public void checkMenuItemEncryptOnAction() {
		modeDecrypt.setSelected(false);
		taPlainText.setEditable(true);
		resetBtnOnMouseClicked();
		taPlainText.setPromptText("Enter Text:");
		cryptosystemVariableBtn.setText("Encrypt");
	}
	
	public void checkMenuItemDecryptOnAction() {
		modeEncrypt.setSelected(false);
		taPlainText.setEditable(false);
		resetBtnOnMouseClicked();
		taPlainText.setPromptText("Enter Encrypted Text:");
		cryptosystemVariableBtn.setText("Decrypt");
	}
	
	public void copyBtnOnMouseClicked() {
		content.putString(taCrypticResult.getText());
	    clipboard.setContent(content);
		copyBtn.setText("Copied!");
	}
	
	public void pasteBtnOnMouseClicked() {
		taPlainText.setText(clipboard.getString());
		pasteBtn.setText("Pasted!");
	}
	
	public void resetBtnOnMouseClicked() {
		clearTextAreas();
		resetCopyPasteBtnText();
	}
	
	private void resetCopyPasteBtnText() {
		copyBtn.setText("Copy Result");
		pasteBtn.setText("Paste from Clipboard");
	}
	
	private void clearTextAreas() {
		taPlainText.clear();
		taCrypticResult.clear();
	}	
	
	public void cryptosystemVariableBtnOnMouseClicked() {
		try {
			if(modeEncrypt.isSelected()) {
				String encryptedResult = cryptographer.encrypt(taPlainText.getText());
				taCrypticResult.setText(encryptedResult);
			} else {
				String decryptedResult = cryptographer.decrypt(taPlainText.getText());
				taCrypticResult.setText(decryptedResult);
			}
			resetCopyPasteBtnText();
		} catch (Exception e) {
			alert(taPlainText.getText());
		}
	}
	
	private void alert(String expression) {
		Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle("Error");
		alert.setHeaderText("Invalid Entry");
		alert.setContentText("\"" + expression + "\""
				+ " is not a valid entry.");
		alert.showAndWait();
	}
}