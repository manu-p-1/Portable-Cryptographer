package projects.encryptor.view;

import projects.encryptor.controller.*;
import projects.encryptor.model.BasicCryptosystem;
import projects.encryptor.model.Cryptographer;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;

/**
 * Loads the FXML for the Controllers and launches the program.
 * 
 * @author Manu Puduvalli
 *
 */
public class PortableCryptographerView extends Application {
	
	@Override
	public void start(Stage primaryStage) throws IOException, NoSuchAlgorithmException, NoSuchPaddingException {	
		FXMLLoader fxml_loader = new FXMLLoader(getClass().getResource("CryptographerGUI.fxml"));
		Parent root = (Parent) fxml_loader.load();		
		CryptographerGUIController cgc = fxml_loader.getController();
		
		cgc.setupStageAndListeners(primaryStage);
		Scene scene = new Scene(root);
		primaryStage.setScene(scene);
		primaryStage.setTitle("Portable Cryptographer");
		primaryStage.setResizable(false);
		primaryStage.getIcons().add(new Image(
				"/projects/encryptor/view/Application_Icons/mainApplicationIcon.png"));
		
		FXMLLoader fxml_loader_key = new FXMLLoader(getClass().getResource("KeyGeneratorGUI.fxml"));
		Parent root2 = (Parent) fxml_loader_key.load();
		KeyGeneratorGUIController kgc = fxml_loader_key.getController();		
		Stage keyStage = kgc.generateDialog(primaryStage, root2);
		
		primaryStage.show();	
		keyStage.showAndWait(); //Show and wait is important so we can receive the user's input!
		
		Cryptographer cryptographer = new Cryptographer();
		cryptographer.setCipher(Cipher.getInstance(BasicCryptosystem.ALGORITHM_AES));
		cryptographer.setKey(kgc.getStoredSecureKey());
		cryptographer.setSecretKeyFromString(cryptographer.getKey(), 
				BasicCryptosystem.ALGORITHM_AES, 
				BasicCryptosystem.MESSAGE_DIGEST_SHA256,
				BasicCryptosystem.CHARSET_UTF8);
		cgc.setupBasicCryptoSystem(cryptographer);
	}
	
	public static void main(String[] args) {
		Application.launch(args);
	}
}
