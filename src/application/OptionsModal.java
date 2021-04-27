package application;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class OptionsModal extends Stage {

	OptionsModal() {
		// Create Pane
		GridPane gridPane = new GridPane();
		gridPane.setHgap(10);
		gridPane.setVgap(10);
		gridPane.setPadding(new Insets(10, 10, 10, 10));
		
		// TODO create auto load talismans option
		
		Main.props.getProperty("autoLoadTalismans");
		
		Scene scene = new Scene(gridPane,400,200);
		this.setScene(scene);
		this.setTitle("Options");
		this.initModality(Modality.WINDOW_MODAL);
	}
}
