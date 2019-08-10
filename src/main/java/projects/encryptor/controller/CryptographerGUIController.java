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
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import projects.encryptor.model.BasicCryptosystem;
import projects.encryptor.model.CryptographyImplementor;

/**
 * The main Controller for the Portable Cryptographer including the GUI
 * elements for input, encrpytion, and decryption.
 * 
 * @author Manu Puduvalli
 *
 */
public class CryptographerGUIController extends AbstractController implements CryptographyImplementor {	

	@FXML private StackPane wrapStackPane;
	@FXML private CheckMenuItem modeEncrypt, modeDecrypt;
	@FXML private JFXTextArea taPlainText, taCrypticResult;
	@FXML private JFXButton copyBtn, pasteBtn, showHideTextBtn, resetBtn, cryptosystemVariableBtn;
	@FXML private Text charCounter, wordCounter;
	@SuppressWarnings("unused") private Stage stage; 
	private BasicCryptosystem cpt;
	
	@Override
	public void setupStageAndListeners(Stage p) {
		this.stage = p;
	}
	
	@Override
	public void setupBasicCryptoSystem(BasicCryptosystem bc) {
		this.cpt = bc;
	}
	
	@FXML protected void closeMenuItemOnAction() {
		baseExit();
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
		buttonTextTimer(copyBtn, "Copy Result", "Copied!");
	}

	@FXML protected void pasteBtnOnMouseClicked() {
		taPlainText.setText(clipboard.getString());
		counterUpdateInspector();
		buttonTextTimer(pasteBtn, "Paste to Field", "Pasted!");
	}

	@FXML protected void resetBtnOnMouseClicked() {
		clearTextAreas();
		clearCounters();
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
		/*
		 * If you read this method, let me stop you there. I know what you're thinking.
		 * Did he just mask text using CSS?! What a charlatan!!! I know. I know. It's a 
		 * cardinal sin of programming security. The main problem is that TextAreas have 
		 * no inbuilt way mask text. I've tried implementing a changelistner but that
		 * really doesn't work either. With a changelistener, it listens to any change, so,
		 * if you were to append a bullet character to the TextArea, it would fire the listener.
		 * Don't understand what I'm saying? That's ok. It's a really difficult problem that
		 * I don't feel like doing for this project specifically. Regardless, if you found
		 * a solution, let me know in the comments for this GitHub page.
		 */
		if(showHideTextBtn.getText().equals("Hide Text")) { //To hide text
			showHideTextBtn.setText("Show Text");					
			taPlainText.setStyle("-fx-background-color: #7c899c; -fx-text-fill: #78909C;");
		}
		else {
			showHideTextBtn.setText("Hide Text"); //To show text
			taPlainText.setStyle("-fx-background-color: #7c899c; -fx-text-fill: #78909C;");
		}
	}

	@FXML protected void showAboutMenuOnMouseClicked() {
		DialogExtension
		.generateDialog(wrapStackPane, DialogExtension.ABOUT_HEADING, DialogExtension.ABOUT_MESSAGE);
	}

	@FXML protected void saveResultThroughSystem() {
		commonFileSystem(wrapStackPane, taCrypticResult);
	}

	@FXML protected void cryptosystemVariableBtnOnMouseClicked() {
		try {
			if(modeEncrypt.isSelected()) {
				String encryptedResult = cpt.encrypt(taPlainText.getText(), 
															    cpt.getSecretKeySpec(),
														   	    cpt.getCipher());
				taCrypticResult.setText(encryptedResult);
			} else {
				String decryptedResult = cpt.decrypt(taPlainText.getText(),
												   				cpt.getSecretKeySpec(),
											   					cpt.getCipher());
				taCrypticResult.setText(decryptedResult);
			}
		} catch (Exception e) {
			e.printStackTrace();
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
				+ "WARNING! Although the program uses a 256 bit AES key with an ECB block cipher mode,\n"
				+ "this application is not meant to be used in any serious programs. It does not take into consideration\n"
				+ "hardware integration, initialization vectors, or Message Authentication (MAC & HMAC) This tool is meant\n"
				+ "for personal and insignificant purposes. Please take the aforementioned message into account when utilizing this tool."
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
