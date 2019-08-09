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

public abstract class AbstractBaseController {
	
	protected final Clipboard clipboard = Clipboard.getSystemClipboard();
	protected final ClipboardContent content = new ClipboardContent();
	
	public void baseExit() {
		Platform.exit();
		System.exit(0);
	}
	
	public static void staticBaseExit() {
		Platform.exit();
		System.exit(0);
	}
	
	public void commonFileSystem(Parent pane, TextInputControl tic) {
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Save Result to File");
        fileChooser.getExtensionFilters().addAll(
        		new ExtensionFilter("Text Files", "*.txt"),
        		new ExtensionFilter("Microsoft Word Document", "*.docx"));
        File file = fileChooser.showSaveDialog(pane.getScene().getWindow());
        if (file != null) {saveTextToFile(tic.getText(), file);}
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
	
	public void buttonTextTimer(Button b, String textb4, String textAft) {
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
	
	public abstract void setupStageAndListeners(Stage p);
}
