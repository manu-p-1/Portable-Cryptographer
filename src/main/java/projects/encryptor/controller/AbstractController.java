package projects.encryptor.controller;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

import javafx.application.Platform;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.TextInputControl;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;

/**
 * The base class of all GUI Controllers.
 * 
 * @author Manu Puduvalli
 *
 */
public abstract class AbstractController {
	
	protected final Clipboard clipboard = Clipboard.getSystemClipboard();
	protected final ClipboardContent content = new ClipboardContent();
	
	/**
	 * Exits the program.
	 */
	public void baseExit() {
		Platform.exit();
		System.exit(0);
	}
	
	/**
	 * Exists the program.
	 */
	public static void staticBaseExit() {
		Platform.exit();
		System.exit(0);
	}
	
	/**
	 * Displays the file explorer and the text to save.
	 * 
	 * @param pane the pane which the FileChooser is initialized
	 * @param tic the TextInputControl instance from which the text is loaded to save
	 */
	public void commonFileSystem(Parent pane, TextInputControl tic) {
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Save Result to File");
        fileChooser.getExtensionFilters().addAll(
        		new ExtensionFilter("Text Files", "*.txt"),
        		new ExtensionFilter("Microsoft Word Document", "*.docx"));
        File file = fileChooser.showSaveDialog(pane.getScene().getWindow());
        if (file != null) {saveTextToFile(tic.getText(), file);}
	}
	
	/**
	 * A helper for {@link #commonFileSystem(Parent, TextInputControl)} 
	 */
	private void saveTextToFile(String content, File file) {
	    try {
		    PrintWriter writer = new PrintWriter(file);
		    writer.println(content);
		    writer.close();
	    } catch (IOException IOe) {
	    	System.err.println(IOe);
	    }
    }
	
	/**
	 * Times a button's text to last 2 seconds. The text is changed and then
	 * reset back to the default.
	 * 
	 * @param b the Button
	 * @param textb4 the default text
	 * @param textAft the text to change to when the thread begins
	 */
	public synchronized void buttonTextTimer(Button b, String textb4, String textAft) {
		 Thread t = new Thread(() -> {
		    	Platform.runLater(()->b.setText(textAft));
				try {
					Thread.sleep(2000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
		    	Platform.runLater(()->b.setText(textb4));
		    });
	    t.start();
	}
	
	/**
	 * Sets any Stage or listener for a controller.
	 * 
	 * @param p the Stage to be set
	 */
	public abstract void setupStageAndListeners(Stage p);
}
