package projects.encryptor.gui;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXButton.ButtonType;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

import java.security.NoSuchAlgorithmException;
import javax.crypto.NoSuchPaddingException;

import projects.encryptor.Cryptographer;

public class CryptographerGUIController {

	@FXML private StackPane wrapStackPane;
	@FXML private CheckMenuItem modeEncrypt, modeDecrypt;
	@FXML private JFXTextArea taPlainText, taCrypticResult;
	@FXML private JFXButton copyBtn, pasteBtn, showHideTextBtn, resetBtn, cryptosystemVariableBtn;
	@FXML private Text charCounter;
	private Cryptographer cryptographer;
	private Stage stage;
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

	public void setStageAndSetupListeners(Stage stage) {
		this.stage = stage;
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

	public void showAboutMenuOnMouseClicked() {
		JFXDialog jfxd = DialogInformationContext.generateDialog(wrapStackPane, "About", DialogInformationContext.ABOUT,"");
		jfxd.show();
	}

	public void saveResultThroughSystem() {
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Save Result to File");
        fileChooser.getExtensionFilters().addAll(
        		new ExtensionFilter("Text Files", "*.txt"),
        		new ExtensionFilter("Microsoft Word Document", "*.docx"));
        File file = fileChooser.showSaveDialog(stage);
        if (file != null) {saveTextToFile(taCrypticResult.getText(), file);}
	}

	private void saveTextToFile(String content, File file) {
      try {
          PrintWriter writer = new PrintWriter(file);
          writer.println(content);
          writer.close();
      } catch (IOException IOe) {
          System.err.println(IOe);
      }
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
		JFXDialog jfxd = DialogInformationContext.generateDialog(wrapStackPane, "Error!", DialogInformationContext.ERROR, expression);
		jfxd.show();
	}
	
	private static class DialogInformationContext{
		
		static final String ABOUT = "A simple utility to encrypt any text using AES (Advanced Encryption Standard).\n"
				+ "Use the Mode menu to swap between encryption and decryption.\n"
				+ "Decrypted text needs to be properly formatted in order to decrypt properly.\n"
				+ "Use the File menu to exit the application or save the encrypted or decrypted result as a text file.\n"
				+ "Copy Result copies the encrypted or decrypted result to the systems clipboard.\n"
				+ "Paste to Field pastes any text on the systems clipboard to the text box.\n\n"
				+ "WARNING! This application is not meant to be used in any serious programs as the key used for encryption\n"
				+ "is public. This tool is meant for personal and insignificant purposes.\n"
				+ "Please take the aforementioned message into account when utilizing this tool."
				+ "\n\n Manu Puduvalli v1.0-Alpha";
		
		static final String ERROR = " is not a valid decrypted text.";
		
		static JFXDialog generateDialog(StackPane root, String heading, String staticContent, String optionalAlertExpression) {
			JFXDialogLayout content = new JFXDialogLayout();
			content.setHeading(new Text(heading));		
			if(staticContent == DialogInformationContext.ERROR) {
				content.setBody(new Text(optionalAlertExpression + staticContent));
				content.setStyle("-fx-background-color: #ef5350");
			} else {
				content.setBody(new Text(staticContent));
			}	
			JFXDialog dialog = new JFXDialog(root, content, JFXDialog.DialogTransition.CENTER);
			JFXButton button = new JFXButton("Close");
			button.setButtonType(ButtonType.RAISED);
			button.setOnAction(e -> dialog.close());
			content.setActions(button);
			
			return dialog;
		}	
	}
}
