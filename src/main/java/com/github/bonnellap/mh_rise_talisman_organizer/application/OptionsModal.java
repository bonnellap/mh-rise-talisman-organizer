package com.github.bonnellap.mh_rise_talisman_organizer.application;

import com.github.bonnellap.mh_rise_talisman_organizer.App;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class OptionsModal extends Stage {

	public OptionsModal() {
		// Create Pane
		GridPane gridPane = new GridPane();
		gridPane.setHgap(10);
		gridPane.setVgap(10);
		gridPane.setPadding(new Insets(10, 10, 10, 10));
		
		
		// TODO finish options
		
		// Get properties
		String autoImport = App.props.getProperty("autoLoadTalismans");
		String importPath = App.props.getProperty("talismanFilePath");
		
		// Checkboxes
		CheckBox checkAutoImport = new CheckBox("Auto Import Talismans");
		if (autoImport != null && autoImport.equals("true")) {
			checkAutoImport.setSelected(true);
		}
		
		// File Chooser
		FileChooser fileChooser = new FileChooser();
		
		
		gridPane.addRow(0, checkAutoImport);
		Scene scene = new Scene(gridPane,400,200);
		this.setScene(scene);
		this.setTitle("Options");
		this.initModality(Modality.WINDOW_MODAL);
	}
}
