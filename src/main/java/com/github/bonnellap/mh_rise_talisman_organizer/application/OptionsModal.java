package com.github.bonnellap.mh_rise_talisman_organizer.application;

import java.io.File;

import com.github.bonnellap.mh_rise_talisman_organizer.App;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class OptionsModal extends Stage {

	public OptionsModal() {
		// Create Panes
		GridPane gridPane = new GridPane();
		gridPane.setHgap(10);
		gridPane.setVgap(10);
		gridPane.setPadding(new Insets(10, 10, 10, 10));
		HBox hbox1 = new HBox(10);
		HBox hbox2 = new HBox(10);
		
		// Check boxes
		CheckBox checkAutoImport = new CheckBox("Auto Import Talismans");
		checkAutoImport.setIndeterminate(false);
		checkAutoImport.setSelected(App.prefs.getBoolean("autoLoadTalismans", false));
		
		// Label fields
		Label labelFilePath = new Label("Default file path:");
		labelFilePath.setMinWidth(100);
		
		// Text fields
		TextField textFilePath = new TextField(App.prefs.get("talismanFilePath", ""));
		textFilePath.setPrefWidth(400);
		textFilePath.setEditable(false);
		
		EventHandler<ActionEvent> checkAutoImportActionEvent = new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				// Set preference
				App.prefs.putBoolean("autoLoadTalismans", checkAutoImport.isSelected());
				if (checkAutoImport.isSelected()) {
					String filePath = fileChooserHandler();
					App.prefs.put("talismanFilePath", filePath);
					textFilePath.setText(filePath);
				} else {
					App.prefs.put("talismanFilePath", "");
					textFilePath.setText("");
				}
			}
		};
		checkAutoImport.setOnAction(checkAutoImportActionEvent);
		
		EventHandler<ActionEvent> textFilePathAction = new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				String filePath = fileChooserHandler();
				App.prefs.put("talismanFilePath", filePath);
				textFilePath.setText(filePath);
			}
		};
		EventHandler<MouseEvent> textFilePathClick = new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				String filePath = fileChooserHandler();
				App.prefs.put("talismanFilePath", filePath);
				textFilePath.setText(filePath);
			}
		};
		textFilePath.setOnAction(textFilePathAction);
		textFilePath.setOnMouseClicked(textFilePathClick);
		
		hbox1.getChildren().add(checkAutoImport);
		hbox2.getChildren().addAll(labelFilePath, textFilePath);
		gridPane.addRow(0, hbox1);
		gridPane.addRow(1, hbox2);
		Scene scene = new Scene(gridPane,400,200);
		this.setScene(scene);
		this.setTitle("Options");
		this.initModality(Modality.WINDOW_MODAL);
	}
	
	/**
	 * Opens the file chooser and sets the user preferences based on the user choice
	 * @return - The file path chosen, or an empty string if nothing was chosen.
	 */
	private String fileChooserHandler() {
		// Open file chooser
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Import Talisman File");
		fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Talisman File", "*.csv"));
		File initialDir = new File(App.prefs.get("talismanFilePath", ""));
		if (initialDir != null && initialDir.getParentFile() != null && initialDir.getParentFile().exists()) {
			fileChooser.setInitialDirectory(initialDir.getParentFile());
		}
		
		// Set file path preference
		File file = fileChooser.showOpenDialog(OptionsModal.this);
		if (file != null && file.exists()) {
			return file.getPath();
		}
		return "";
	}
}
