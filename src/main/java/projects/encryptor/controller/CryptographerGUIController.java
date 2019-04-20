package projects.encryptor.controller;

import com.jfoenix.animation.alert.JFXAlertAnimation;
import com.jfoenix.controls.JFXAlert;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXButton.ButtonType;

import javafx.application.Platform;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import projects.encryptor.model.Cryptographer;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

import java.security.NoSuchAlgorithmException;
import javax.crypto.NoSuchPaddingException;

public class CryptographerGUIController {

	@FXML private StackPane wrapStackPane;
	@FXML private CheckMenuItem modeEncrypt, modeDecrypt;
	@FXML private JFXTextArea taPlainText, taCrypticResult;
	@FXML private JFXButton copyBtn, pasteBtn, showHideTextBtn, resetBtn, cryptosystemVariableBtn;
	@FXML private Text charCounter, wordCounter;
	private Cryptographer cryptographer;
	private Stage stage;
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

	public void setStageAndSetupListeners(Stage stage) {
		this.stage = stage;
	}

	@FXML protected void closeMenuItemOnAction() {
		Platform.exit();
		System.exit(0);
	}

	@FXML protected void checkMenuItemEncryptOnAction() {
		modeDecrypt.setSelected(false);
		resetBtnOnMouseClicked();
		taPlainText.setPromptText("Enter Text:");
		cryptosystemVariableBtn.setText("Encrypt");
	}

	@FXML protected void checkMenuItemDecryptOnAction() {
		modeEncrypt.setSelected(false);
		resetBtnOnMouseClicked();
		taPlainText.setPromptText("Enter Encrypted Text:");
		cryptosystemVariableBtn.setText("Decrypt");
	}

	@FXML protected void copyBtnOnMouseClicked() {
		content.putString(taCrypticResult.getText());
	    clipboard.setContent(content);
		copyBtn.setText("Copied!");
	}

	@FXML protected void pasteBtnOnMouseClicked() {
		taPlainText.setText(clipboard.getString());
		counterUpdateInspector();
		pasteBtn.setText("Pasted!");
	}

	@FXML protected void resetBtnOnMouseClicked() {
		clearTextAreas();
		resetCopyPasteBtnText();
		clearCounters();
	}

	private void resetCopyPasteBtnText() {
		copyBtn.setText("Copy Result");
		pasteBtn.setText("Paste to Field");
	}

	private void clearTextAreas() {
		taPlainText.clear();
		taCrypticResult.clear();
	}

	private void clearCounters() {
		clearCharacterCount();
		clearWordCount();
	}

	private void clearCharacterCount() {
		Platform.runLater(() -> charCounter.setText("Character Count:  0"));
	}

	private void clearWordCount() {
		Platform.runLater(() -> wordCounter.setText("Word Count:  0"));
	}

	@FXML protected void counterUpdateInspector() {
		Platform.runLater(() -> charCounter.setText("Character Count:  " + taPlainText.getText().length()));
		Platform.runLater(() -> {
			if(taPlainText.getText().isEmpty()) {
				clearWordCount();
			} else {
				wordCounter.setText("Word Count:  " + taPlainText.getText().trim().split("\\s+").length);
			}
		});
	}

	@FXML protected void taPlainTextEventFilterContextMenuBlocker() {
		taPlainText.addEventFilter(ContextMenuEvent.CONTEXT_MENU_REQUESTED, Event::consume);
	}

	@FXML protected void showHideTextBtnOnMouseClicked() {
		if(showHideTextBtn.getText().equals("Hide Text")) { //To hide text
			showHideTextBtn.setText("Show Text");
			taPlainText.setStyle("-fx-background-color: #7c899c; -fx-text-fill: #78909C;");
		}
		else {
			showHideTextBtn.setText("Hide Text"); //To show text
			taPlainText.setStyle("-fx-background-color: #7c899c; -fx-text-fill: #424242");
		}
	}

	@FXML protected void showAboutMenuOnMouseClicked() {
		DialogExtension
		.generateDialog(wrapStackPane, DialogExtension.ABOUT_HEADING, DialogExtension.ABOUT_MESSAGE);
	}

	@FXML protected void saveResultThroughSystem() {
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

	@FXML protected void cryptosystemVariableBtnOnMouseClicked() {
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
			DialogExtension
			.generateAlert(DialogExtension.ERROR_HEADING, DialogExtension.DECRYPTION_ERROR_MESSAGE);
		}
	}

	protected static class DialogExtension{

		static final String ERROR_HEADING = "ERROR!";

		static final String ABOUT_HEADING = "ABOUT!";

		static final String ABOUT_MESSAGE = "A simple utility to encrypt any text using AES (Advanced Encryption Standard).\n"
				+ "Use the Mode menu to swap between encryption and decryption.\n"
				+ "Decrypted text needs to be properly formatted in order to decrypt properly.\n"
				+ "Use the File menu to exit the application or save the encrypted or decrypted result as a text file.\n"
				+ "Copy Result copies the encrypted or decrypted result to the systems clipboard.\n"
				+ "Paste to Field pastes any text on the systems clipboard to the text box.\n\n"
				+ "WARNING! This application is not meant to be used in any serious programs as the key used for encryption\n"
				+ "is public. This tool is meant for personal and insignificant purposes.\n"
				+ "Please take the aforementioned message into account when utilizing this tool."
				+ "\n\n Manu Puduvalli v1.0.0-beta.1";

		static final String DECRYPTION_ERROR_MESSAGE = "The value entered is not a valid decrypted text";

		static void generateDialog(StackPane root, String heading, String staticContent) {
			JFXDialogLayout content = new JFXDialogLayout();
			content.setHeading(new Text(heading));
			content.setBody(new Text(staticContent));
			JFXDialog dialog = new JFXDialog(root, content, JFXDialog.DialogTransition.CENTER);
			JFXButton button = new JFXButton("Close");
			button.setButtonType(ButtonType.RAISED);
			button.setOnAction(e -> dialog.close());
			content.setActions(button);
			dialog.show();
		}

		static void generateAlert(String heading, String staticContent){
			JFXDialogLayout content = new JFXDialogLayout();
			content.setHeading(new Text(heading));
			content.setBody(new Text(staticContent));
			content.setStyle("-fx-background-color: #ef5350");
			JFXAlert<Void> alert = new JFXAlert<>();
			alert.setAnimation(JFXAlertAnimation.NO_ANIMATION);
			alert.setTitle(heading);
			alert.setContent(content);
			JFXButton button = new JFXButton("Close");
			button.setButtonType(ButtonType.RAISED);
			button.setOnAction(e -> alert.hideWithAnimation());
			content.setActions(button);
			alert.showAndWait();
		}
	}
}
