package projects.encryptor.gui;

import java.security.NoSuchAlgorithmException;
import javax.crypto.NoSuchPaddingException;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXButton.ButtonType;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import projects.encryptor.*;

public class CryptographerGUIController {
	
	@FXML private StackPane wrapStackPane;
	@FXML private CheckMenuItem modeEncrypt, modeDecrypt;
	@FXML private JFXTextArea taPlainText, taCrypticResult;
	@FXML private JFXButton copyBtn, pasteBtn, showHideTextBtn, resetBtn, cryptosystemVariableBtn;
    @FXML private Text charCounter;
	private Cryptographer cryptographer;
	private final Clipboard clipboard = Clipboard.getSystemClipboard();
	private final ClipboardContent content = new ClipboardContent();
	private static final int MAXIMUM_ALLOWED_CHARACTERS = 1000;

	
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
		resetBtnOnMouseClicked();
		taPlainText.setPromptText("Enter Text:");
		cryptosystemVariableBtn.setText("Encrypt");
	}
	
	public void checkMenuItemDecryptOnAction() {
		modeEncrypt.setSelected(false);
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
		Platform.runLater(() -> charCounter.setText("Character Count:  " + taPlainText.getText().length() + "/1000"));
		pasteBtn.setText("Pasted!");
	}
	
	public void resetBtnOnMouseClicked() {
		clearTextAreas();
		resetCopyPasteBtnText();
		Platform.runLater(() -> charCounter.setText("Character Count:  "));
	}
	
	private void resetCopyPasteBtnText() {
		copyBtn.setText("Copy Result");
		pasteBtn.setText("Paste to Field");
	}
	
	private void clearTextAreas() {
		taPlainText.clear();
		taCrypticResult.clear();
	}	
	
	public void charCountInspector() {
		Platform.runLater(() -> charCounter.setText("Character Count:  " + taPlainText.getText().length() + "/1000"));
		taPlainText.textProperty().addListener(new ChangeListener<String>() {
	        @Override
	        public void changed(final ObservableValue<? extends String> ov, final String oldValue, final String newValue) {
	            if (taPlainText.getText().length() >= MAXIMUM_ALLOWED_CHARACTERS) {
	            	taPlainText.setText(taPlainText.getText().substring(0, MAXIMUM_ALLOWED_CHARACTERS));  
	            }
	        }
	    });
	}
	
	public void showHideTextBtnOnMouseClicked() {	
		if(showHideTextBtn.getText().equals("Hide Text")) { //To hide text
			showHideTextBtn.setText("Show Text");
			taPlainText.setStyle("-fx-background-color: #78909C; -fx-text-fill: #78909C;");
		}
		else {
			showHideTextBtn.setText("Hide Text"); //To show text
			taPlainText.setStyle("-fx-background-color: #78909C; -fx-text-fill: #424242");
		}
	}
	
	public void showAboutMenuOnClicked() {
		JFXDialogLayout content = new JFXDialogLayout();
		content.setHeading(new Text("About"));
		content.setBody(new Text("A simple utility to encrypt any text using AES (Advanced Encryption Standard).\n"
				+ "Use the Mode menu to swap between encryption and decryption."
				+ "Decrypted text needs to be properly formatted in order to decrypt properly.\n"
				+ "Use the File menu to exit the application or save the encrypted or decrypted result as a text file.\n"
				+ "\n\n Manu Puduvalli V2.6"));
		JFXDialog dialog = new JFXDialog(wrapStackPane, content, JFXDialog.DialogTransition.CENTER);
		JFXButton button = new JFXButton("Close");
		button.setButtonType(ButtonType.RAISED);
		button.setOnAction(e -> dialog.close());
		content.setActions(button);
		dialog.show();	
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