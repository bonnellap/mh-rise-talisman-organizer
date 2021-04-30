package application;
	
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Properties;

import org.javatuples.Pair;

import javafx.application.Application;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.stage.WindowEvent;
import javafx.util.Callback;
import javafx.util.converter.IntegerStringConverter;
import skill.Skill;
import skill.SkillTable;
import talisman.Talisman;
import talisman.TalismanComparison;
import talisman.TalismanTable;


public class Main extends Application {
	
	private TalismanTable talismans = new TalismanTable();
	private boolean isTalismanFileUpdated = true;
	
	private static Stage pStage = null;
	static Properties props = new Properties();
	public static final String SETTINGS_PATH = "resources/app.properties";
	
	private static final TableView<Talisman> table = new TableView<>();
	
	@Override
	public void init() {
		// Read settings file
		FileInputStream in = null;
		try {
			File settingsFile = new File(SETTINGS_PATH);
			settingsFile.createNewFile(); // Create a settings file if it doesn't already exist
			in = new FileInputStream(settingsFile);
			props.load(in);
			
			if (props.getProperty("autoLoadTalismans") != null && props.getProperty("autoLoadTalismans").equals("true") && props.getProperty("talismanFilePath") != null) {
				try {
					talismans = new TalismanTable(new File(props.getProperty("talismanFilePath")));
					refreshTable(table);
				} catch (Exception e) {
					// Do nothing
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			showExceptionMsg("Unable to load or create settings file. Your settings may not be saved.", e);
		} finally {
			try {
				if (in != null) {
					in.close();
				}
			} catch (Exception e) {
				// Do nothing
			}
		}
	}
	
	@Override
	public void start(Stage primaryStage) {
		try {
			SkillTable.setSkills("resources/skills.csv");
			
			// Create Panes
			BorderPane root = new BorderPane();
			GridPane skillAndTablePane = new GridPane();
			GridPane addSkillPane = new GridPane();
			addSkillPane.setHgap(10);
			addSkillPane.setVgap(10);
			addSkillPane.setPadding(new Insets(10, 10, 10, 10));
			GridPane buttonPane = new GridPane();
			buttonPane.setHgap(10);
			buttonPane.setVgap(10);
			buttonPane.setPadding(new Insets(10, 10, 10, 10));
			GridPane tablePane = new GridPane();
			tablePane.setPadding(new Insets(10, 10, 10, 10));
			
			
			// Create Menus
			MenuBar menuBar = new MenuBar();
			Menu menuFile = new Menu("File");
			MenuItem menuItemImport = new MenuItem("Import...");
			MenuItem menuItemExport = new MenuItem("Export...");
			MenuItem menuItemOptions = new MenuItem("Options");
			MenuItem menuItemExit = new MenuItem("Exit");
			
			// Add Menus to MenuBar
			menuFile.getItems().add(menuItemImport);
			menuFile.getItems().add(menuItemExport);
			menuFile.getItems().add(menuItemOptions);
			menuFile.getItems().add(menuItemExit);
			menuBar.getMenus().add(menuFile);
			
			// Create FileChooser
			FileChooser fileChooser = new FileChooser();
			
			// Create Table elements
			TableColumn<Talisman, String> skill1Col = new TableColumn<>("Skill 1");
			TableColumn<Talisman, String> skill1NameCol = new TableColumn<>("Name");
			TableColumn<Talisman, Integer> skill1LevelCol = new TableColumn<>("Level");
			TableColumn<Talisman, String> skill2Col = new TableColumn<>("Skill 2");
			TableColumn<Talisman, String> skill2NameCol = new TableColumn<>("Name");
			TableColumn<Talisman, Integer> skill2LevelCol = new TableColumn<>("Level");
			TableColumn<Talisman, Integer> slot1Col = new TableColumn<>("Slot 1");
			TableColumn<Talisman, Integer> slot2Col = new TableColumn<>("Slot 2");
			TableColumn<Talisman, Integer> slot3Col = new TableColumn<>("Slot 3");
			
			skill1NameCol.setCellValueFactory(new Callback<CellDataFeatures<Talisman, String>, ObservableValue<String>>() {
				@Override
				public ObservableValue<String> call(CellDataFeatures<Talisman, String> param) {
					// TODO Auto-generated method stub
					if (param.getValue() != null) {
						Skill skill = param.getValue().getSkill(0);
						if (skill != null) {
							return new ReadOnlyObjectWrapper<String>(skill.name);
						} else {
							return new ReadOnlyObjectWrapper<String>("");
						}
					}
					return null;
				}
			});
			
			skill1LevelCol.setCellValueFactory(new Callback<CellDataFeatures<Talisman, Integer>, ObservableValue<Integer>>() {
				@Override
				public ObservableValue<Integer> call(CellDataFeatures<Talisman, Integer> param) {
					// TODO Auto-generated method stub
					if (param.getValue() != null) {
						int level = param.getValue().getSkillLevel(0);
						if (level > 0) {
							return new ReadOnlyObjectWrapper<Integer>(level);
						} else {
							return new ReadOnlyObjectWrapper<Integer>(null);
						}
					}
					return null;
				}
			});
			
			skill2NameCol.setCellValueFactory(new Callback<CellDataFeatures<Talisman, String>, ObservableValue<String>>() {
				@Override
				public ObservableValue<String> call(CellDataFeatures<Talisman, String> param) {
					// TODO Auto-generated method stub
					if (param.getValue() != null) {
						Skill skill = param.getValue().getSkill(1);
						if (skill != null) {
							return new ReadOnlyObjectWrapper<String>(skill.name);
						} else {
							return new ReadOnlyObjectWrapper<String>("");
						}
					}
					return null;
				}
			});
			
			skill2LevelCol.setCellValueFactory(new Callback<CellDataFeatures<Talisman, Integer>, ObservableValue<Integer>>() {
				@Override
				public ObservableValue<Integer> call(CellDataFeatures<Talisman, Integer> param) {
					if (param.getValue() != null) {
						int level = param.getValue().getSkillLevel(1);
						if (level > 0) {
							return new ReadOnlyObjectWrapper<Integer>(level);
						} else {
							return new ReadOnlyObjectWrapper<Integer>(null);
						}
					}
					return null;
				}
			});
			
			slot1Col.setCellValueFactory(new Callback<CellDataFeatures<Talisman, Integer>, ObservableValue<Integer>>() {
				@Override
				public ObservableValue<Integer> call(CellDataFeatures<Talisman, Integer> param) {
					if (param.getValue() != null) {
						int slot = param.getValue().getSlot(0);
						if (slot > 0) {
							return new ReadOnlyObjectWrapper<Integer>(slot);
						} else {
							return new ReadOnlyObjectWrapper<Integer>(null);
						}
					}
					return null;
				}
			});
			
			slot2Col.setCellValueFactory(new Callback<CellDataFeatures<Talisman, Integer>, ObservableValue<Integer>>() {
				@Override
				public ObservableValue<Integer> call(CellDataFeatures<Talisman, Integer> param) {
					if (param.getValue() != null) {
						int slot = param.getValue().getSlot(1);
						if (slot > 0) {
							return new ReadOnlyObjectWrapper<Integer>(slot);
						} else {
							return new ReadOnlyObjectWrapper<Integer>(null);
						}
					}
					return null;
				}
			});
			
			slot3Col.setCellValueFactory(new Callback<CellDataFeatures<Talisman, Integer>, ObservableValue<Integer>>() {
				@Override
				public ObservableValue<Integer> call(CellDataFeatures<Talisman, Integer> param) {
					if (param.getValue() != null) {
						int slot = param.getValue().getSlot(2);
						if (slot > 0) {
							return new ReadOnlyObjectWrapper<Integer>(slot);
						} else {
							return new ReadOnlyObjectWrapper<Integer>(null);
						}
					}
					return null;
				}
			});
			
			skill1Col.getColumns().add(skill1NameCol);
			skill1Col.getColumns().add(skill1LevelCol);
			skill2Col.getColumns().add(skill2NameCol);
			skill2Col.getColumns().add(skill2LevelCol);
			table.getColumns().add(skill1Col);
			table.getColumns().add(skill2Col);
			table.getColumns().add(slot1Col);
			table.getColumns().add(slot2Col);
			table.getColumns().add(slot3Col);
			table.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
			table.setPlaceholder(new Label("No talismans in table"));
			
			// Create Buttons
			Button btnAddTalisman = new Button("Add Talisman");
			Button btnEditTalisman = new Button("Edit Talisman");
			Button btnRemoveTalisman = new Button("Remove Talisman");
			Button btnFilterTalisman = new Button("Filter Talismans");
			Button btnShowObsolete = new Button("Show Obsolete Talismans");
			Button btnResetFields = new Button("Reset Fields");
			
			// Create Labels
			Label labelSkill1 = new Label("Skill 1 Name");
			Label labelLevel1 = new Label("Skill 1 Level");
			Label labelSkill2 = new Label("Skill 2 Name");
			Label labelLevel2 = new Label("Skill 2 Level");
			Label labelSlot1 = new Label("Slot 1");
			Label labelSlot2 = new Label("Slot 2");
			Label labelSlot3 = new Label("Slot 3");
			
			// Create ComboBoxes
			List<Skill> skillList = SkillTable.getSkillList();
			List<Integer> slotList1 = Arrays.asList(1, 2, 3);
			List<Integer> slotList2 = Arrays.asList(1, 2);
			List<Integer> slotList3 = Arrays.asList(1);
			ComboBox<Skill> cbSkill1 = new ComboBox<Skill>(FXCollections.observableArrayList(skillList));
			ComboBox<Integer> cbLevel1 = new ComboBox<Integer>(FXCollections.observableArrayList());
			ComboBox<Skill> cbSkill2 = new ComboBox<Skill>(FXCollections.observableArrayList(skillList));
			ComboBox<Integer> cbLevel2 = new ComboBox<Integer>(FXCollections.observableArrayList());
			ComboBox<Integer> cbSlot1 = new ComboBox<Integer>(FXCollections.observableArrayList(slotList1));
			ComboBox<Integer> cbSlot2 = new ComboBox<Integer>(FXCollections.observableArrayList(slotList2));
			ComboBox<Integer> cbSlot3 = new ComboBox<Integer>(FXCollections.observableArrayList(slotList3));
			cbSkill1.setPromptText("Skill 1 Name");
			cbLevel1.setPromptText("Skill 1 Level");
			cbSkill2.setPromptText("Skill 2 Name");
			cbLevel2.setPromptText("Skill 2 Level");
			cbSlot1.setPromptText("Slot 1");
			cbSlot2.setPromptText("Slot 2");
			cbSlot3.setPromptText("Slot 3");
			
			// Add ComboBox autosuggest
			FxUtil.autoCompleteComboBoxPlus(cbSkill1, false, (typedText, itemToCompare) -> itemToCompare.name.toLowerCase().contains(typedText.toLowerCase()) || itemToCompare.name.equals(typedText));
			FxUtil.autoCompleteComboBoxPlus(cbLevel1, true,  (typedText, itemToCompare) -> itemToCompare.toString().contains(typedText.toLowerCase()));
			FxUtil.autoCompleteComboBoxPlus(cbSkill2, false, (typedText, itemToCompare) -> itemToCompare.name.toLowerCase().contains(typedText.toLowerCase()) || itemToCompare.name.equals(typedText));
			FxUtil.autoCompleteComboBoxPlus(cbLevel2, true, (typedText, itemToCompare) -> itemToCompare.toString().contains(typedText.toLowerCase()));
			FxUtil.autoCompleteComboBoxPlus(cbSlot1, false, (typedText, itemToCompare) -> itemToCompare.toString().contains(typedText.toLowerCase()));
			FxUtil.autoCompleteComboBoxPlus(cbSlot2, false, (typedText, itemToCompare) -> itemToCompare.toString().contains(typedText.toLowerCase()));
			FxUtil.autoCompleteComboBoxPlus(cbSlot3, false, (typedText, itemToCompare) -> itemToCompare.toString().contains(typedText.toLowerCase()));
			
			// Add StringConverters
			cbSkill1.setConverter(new SkillConverter());
			cbSkill2.setConverter(new SkillConverter());
			cbLevel1.setConverter(new IntegerStringConverter());
			cbLevel2.setConverter(new IntegerStringConverter());
			cbSlot1.setConverter(new IntegerStringConverter());
			cbSlot2.setConverter(new IntegerStringConverter());
			cbSlot3.setConverter(new IntegerStringConverter());
			
			// Add event to skill ComboBoxes to clear out / change the level ComboBoxes
			EventHandler<ActionEvent> cbSkill1Event = new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent arg0) {
					int maxSkillLevel = FxUtil.getComboBoxValue(cbSkill1) != null ? FxUtil.getComboBoxValue(cbSkill1).level : 0;
					Integer[] levelArray = new Integer[maxSkillLevel];
					for (int i = 0; i < maxSkillLevel; i++) {
						levelArray[i] = i + 1;
					}
					cbLevel1.getItems().clear();
					cbLevel1.setItems(FXCollections.observableArrayList(levelArray));
				}
			};
			cbSkill1.setOnAction(cbSkill1Event);
			EventHandler<ActionEvent> cbSkill2Event = new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent arg0) {
					int maxSkillLevel = FxUtil.getComboBoxValue(cbSkill2) != null ? FxUtil.getComboBoxValue(cbSkill2).level : 0;
					Integer[] levelArray = new Integer[maxSkillLevel];
					for (int i = 0; i < maxSkillLevel; i++) {
						levelArray[i] = i + 1;
					}
					cbLevel2.getItems().clear();
					cbLevel2.setItems(FXCollections.observableArrayList(levelArray));
				}
			};
			cbSkill2.setOnAction(cbSkill2Event);
			/*cbLevel1.itemsProperty().addListener(new ChangeListener<ObservableList<Integer>>() {
				@Override
				public void changed(ObservableValue<? extends ObservableList<Integer>> observable,
						ObservableList<Integer> oldValue, ObservableList<Integer> newValue) {
					System.out.println("here");
					
				}
			});*/
			
			// Add Skill event handler
			EventHandler<ActionEvent> btnAddTalismanActionEvent = new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent event) {
					// Check for talisman errors
					String errorMsg = "";
					if (cbSkill1.getValue() == null) {
						errorMsg += "Skill 1 Name must have a value.\n";
					}
					if (cbLevel1.getValue() == null) {
						errorMsg += "Skill 1 Level must have a value.\n";
					}
					if (cbSkill2.getValue() != null && cbLevel2.getValue() == null) {
						errorMsg += "Skill 2 Level must have a value.\n";
					}
					if (cbSkill1.getValue() != null && cbSkill2.getValue() != null && cbSkill1.getValue().equals(cbSkill2.getValue())) {
						errorMsg += "Skill 1 Name cannot be the same as Skill 2 Name.\n";
					}
					if (!errorMsg.equals("")) {
						showErrorMsg("Error", "The talisman you entered cannot be added.", errorMsg);
						return;
					}
					
					// Add new talisman to talisman list
					talismans.add(createTalisman(cbSkill1.getValue(), cbLevel1.getValue(), cbSkill2.getValue(), cbLevel2.getValue(), cbSlot1.getValue(), cbSlot2.getValue(), cbSlot3.getValue()));
					isTalismanFileUpdated = false;
					refreshTable(table);
					
					// Clear out combo lists
					cbSkill1.setValue(null);
					cbLevel1.setValue(null);
					cbSkill2.setValue(null);
					cbLevel2.setValue(null);
					cbSlot1.setValue(null);
					cbSlot2.setValue(null);
					cbSlot3.setValue(null);
				}
			};
			btnAddTalisman.setOnAction(btnAddTalismanActionEvent);
			
			EventHandler<ActionEvent> btnEditTalismanActionEvent = new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent event) {
					Talisman selectedTalisman = table.getSelectionModel().getSelectedItem();
					if (selectedTalisman == null) {
						showErrorMsg("Error", "No selected talisman", "You must select a talisman from the table to edit.");
						return;
					}
					int index = talismans.indexOf(selectedTalisman);
					if (index != -1) {
						talismans.set(index, createTalisman(cbSkill1.getValue(), cbLevel1.getValue(), cbSkill2.getValue(), cbLevel2.getValue(), cbSlot1.getValue(), cbSlot2.getValue(), cbSlot3.getValue()));
						isTalismanFileUpdated = false;
						refreshTable(table);
					}
				}
			};
			btnEditTalisman.setOnAction(btnEditTalismanActionEvent);
			
			EventHandler<ActionEvent> btnFilterTalismanActionEvent = new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent event) {
					TalismanTable foundTalismans = new TalismanTable();
					for (Talisman t : talismans) {
						// Find skill 1
						if (cbSkill1.getValue() != null) {
							Pair<Skill, Integer> tSkill1 = t.getSkillPair(cbSkill1.getValue());
							if (tSkill1 == null || (cbLevel1.getValue() != null && cbLevel1.getValue() > tSkill1.getValue1())) {
								continue;
							}
						}
						// Find skill 2
						if (cbSkill2.getValue() != null) {
							Pair<Skill, Integer> tSkill2 = t.getSkillPair(cbSkill2.getValue());
							if (tSkill2 == null || (cbLevel2.getValue() != null && cbLevel2.getValue() > tSkill2.getValue1())) {
								continue;
							}
						}
						// Find slots
						List<Integer> slotList = new ArrayList<>();
						slotList.add(cbSlot1.getValue());
						slotList.add(cbSlot2.getValue());
						slotList.add(cbSlot3.getValue());
						slotList.removeAll(Collections.singleton(null));
						Collections.sort(slotList);
						List<Integer> tSlotList = t.getSlotList();
						
						if (TalismanComparison.compareSlotLists(slotList, tSlotList) <= 0) {
							foundTalismans.add(t);
						}
					}
					table.setItems(FXCollections.observableArrayList(foundTalismans));
				}
			};
			btnFilterTalisman.setOnAction(btnFilterTalismanActionEvent);
			
			EventHandler<ActionEvent> btnRemoveTalismanActionEvent = new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent event) {
					ObservableList<Talisman> tList = table.getSelectionModel().getSelectedItems();
					if (tList != null && tList.size() > 0) {
						for (Talisman t : tList) {
							talismans.remove(t);
						}
						isTalismanFileUpdated = false;
						refreshTable(table);
					}
				}
			};
			btnRemoveTalisman.setOnAction(btnRemoveTalismanActionEvent);
			
			EventHandler<ActionEvent> btnShowObsoleteTalismanActionEvent = new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent event) {
					if (btnShowObsolete.getText() == "Show Obsolete Talismans") {
						TalismanTable obsolete = talismans.optimizeTalismans();
						table.setItems(FXCollections.observableArrayList(obsolete));
						btnShowObsolete.setText("Show All Talismans");
					} else {
						refreshTable(table);
						btnShowObsolete.setText("Show Obsolete Talismans");
					}
				}
			};
			btnShowObsolete.setOnAction(btnShowObsoleteTalismanActionEvent);
			
			EventHandler<ActionEvent> btnResetFieldsActionEvent = new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent event) {
					cbSkill1.setValue(null);
					cbLevel1.setValue(null);
					cbSkill2.setValue(null);
					cbLevel2.setValue(null);
					cbSlot1.setValue(null);
					cbSlot2.setValue(null);
					cbSlot3.setValue(null);
					refreshTable(table);
				}
			};
			btnResetFields.setOnAction(btnResetFieldsActionEvent);
			
			// Add import menu event handler
			EventHandler<ActionEvent> menuItemImportActionEvent = new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent event) {
					fileChooser.setTitle("Import Talisman File");
					fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Talisman File", "*.csv"));
					if (props.getProperty("talismanFilePath") != null) {
						try {
							File intialFile = new File(props.getProperty("talismanFilePath"));
							fileChooser.setInitialDirectory(intialFile.getParentFile());
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
					File file = null;
					try {
						file = fileChooser.showOpenDialog(primaryStage);
					} catch (Exception e) {
						fileChooser.setInitialDirectory(null);
						file = fileChooser.showOpenDialog(primaryStage);
					}
					if (file != null) {
						try {
							props.setProperty("talismanFilePath", file.getAbsolutePath());
							talismans = new TalismanTable(file);
							isTalismanFileUpdated = true;
							refreshTable(table);
						} catch (Exception e) {
							showExceptionMsg("An exception occurred when reading talismans from file.", e);
						}
					}
				}
			};
			menuItemImport.setOnAction(menuItemImportActionEvent);
			
			// Add export menu event handler
			EventHandler<ActionEvent> menuItemExportActionEvent = new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent event) {
					fileChooser.setTitle("Export Talisman File");
					fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Talisman File", "*.csv"));
					if (props.getProperty("talismanFilePath") != null) {
						try {
							File intialFile = new File(props.getProperty("talismanFilePath"));
							fileChooser.setInitialDirectory(intialFile.getParentFile());
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
					File file = null;
					try {
						file = fileChooser.showOpenDialog(primaryStage);
					} catch (Exception e) {
						fileChooser.setInitialDirectory(null);
						file = fileChooser.showOpenDialog(primaryStage);
					}
					if (file != null) {
						try {
							props.setProperty("talismanFilePath", file.getAbsolutePath());
							talismans.writeTalismansToFile(file);
							isTalismanFileUpdated = true;
						} catch (Exception e) {
							showExceptionMsg("An exception occurred when writing talismans to file.", e);
						}
					}
				}
			};
			menuItemExport.setOnAction(menuItemExportActionEvent);
			
			// Add options menu event handler
			EventHandler<ActionEvent> menuItemOptionsActionEvent = new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent event) {
					OptionsModal options = new OptionsModal();
					options.initOwner(primaryStage);
					options.show();
				}
			};
			menuItemOptions.setOnAction(menuItemOptionsActionEvent);
			
			// Add exit menu event handler
			EventHandler<ActionEvent> menuItemExitActionEvent = new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent event) {
					Window window = pStage.getScene().getWindow();
					window.fireEvent(new WindowEvent(window, WindowEvent.WINDOW_CLOSE_REQUEST));
				}
			};
			menuItemExit.setOnAction(menuItemExitActionEvent);
			
			// Add table row double-click edit
			table.setRowFactory(tv -> {
			    TableRow<Talisman> row = new TableRow<>();
			    row.setOnMouseClicked(event -> {
			        if (!row.isEmpty() && event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2) {
			        	Talisman clickedRow = row.getItem();
			        	cbSkill1.setValue(clickedRow.getSkill(0));
						cbLevel1.setValue(convert0ToNull(clickedRow.getSkillLevel(0)));
						cbSkill2.setValue(clickedRow.getSkill(1));
						cbLevel2.setValue(convert0ToNull(clickedRow.getSkillLevel(1)));
						cbSlot1.setValue(convert0ToNull(clickedRow.getSlot(0)));
						cbSlot2.setValue(convert0ToNull(clickedRow.getSlot(1)));
						cbSlot3.setValue(convert0ToNull(clickedRow.getSlot(2)));
			        }
			    });
			    return row;
			});
			
			// When a user doesn't click on an element
			root.setOnMousePressed(e -> {
				table.getSelectionModel().clearSelection();
				root.requestFocus(); // All elements will lose focus
			}); 
			
			// Add nodes to the Pane
			addSkillPane.addRow(0, labelSkill1, labelLevel1, labelSkill2, labelLevel2, labelSlot1, labelSlot2, labelSlot3);
			addSkillPane.addRow(1, cbSkill1, cbLevel1, cbSkill2, cbLevel2, cbSlot1, cbSlot2, cbSlot3);
			
			buttonPane.addRow(0, btnAddTalisman, btnEditTalisman, btnRemoveTalisman, btnFilterTalisman, btnShowObsolete, btnResetFields);
			
			tablePane.addRow(0, table);
			
			skillAndTablePane.addRow(0, addSkillPane);
			skillAndTablePane.addRow(1, buttonPane);
			skillAndTablePane.addRow(2, tablePane);
			
			root.setTop(menuBar);
			root.setCenter(skillAndTablePane);
			Scene scene = new Scene(root,800,400);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			pStage = primaryStage;
			primaryStage.setScene(scene);
			primaryStage.setTitle("MH Rise Talisman Organizer");
			primaryStage.getScene().getWindow().addEventFilter(WindowEvent.WINDOW_CLOSE_REQUEST, this::closeWindowEvent);
			primaryStage.show();
		} catch(Exception e) {
			showExceptionMsg("An exception occurred while the application was open.", e);
		}
	}
	
	@Override
	public void stop() {
		// Save the properties
		FileOutputStream out = null;
		try {
			File settingsFile = new File(SETTINGS_PATH);
			settingsFile.createNewFile(); // Create a settings file if it doesn't already exist
			out = new FileOutputStream(settingsFile);
			props.store(out, "MH Rise Talisman Organizer Settings");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (out != null) {
					out.close();
				}
			} catch (Exception e) {
				// Do nothing
			}
		}
	}
	
	private void closeWindowEvent(WindowEvent event) {
		// Show popup if there is unsaved data
        if(!isTalismanFileUpdated) {
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.getButtonTypes().remove(ButtonType.OK);
            alert.getButtonTypes().add(ButtonType.CANCEL);
            alert.getButtonTypes().add(ButtonType.YES);
            alert.setTitle("Quit application");
            alert.setContentText(String.format("You have unsaved changes to your talisman list.\nClose without saving?"));
            alert.initOwner(pStage.getOwner());
            Optional<ButtonType> res = alert.showAndWait();

            if(res.isPresent()) {
                if(res.get().equals(ButtonType.CANCEL))
                    event.consume();
            }
        }
    }
	
	/**
	 * Shows an error message alert
	 * @param title - The title of the alert
	 * @param header - The header of the alert
	 * @param errorMsg - The main message of the alert
	 */
	public static void showErrorMsg(String title, String header, String errorMsg) {
		Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle(title);
		alert.setHeaderText(header);
		alert.setContentText(errorMsg);

		alert.showAndWait();
	}
	
	public static void showExceptionMsg(String errorMsg, Exception ex) {
		Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle("Exception");
		alert.setHeaderText("An exception occurred.");
		alert.setContentText(errorMsg);

		// Create expandable Exception.
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		ex.printStackTrace(pw);
		String exceptionText = sw.toString();

		Label label = new Label("The exception stacktrace was:");

		TextArea textArea = new TextArea(exceptionText);
		textArea.setEditable(false);
		textArea.setWrapText(true);

		textArea.setMaxWidth(Double.MAX_VALUE);
		textArea.setMaxHeight(Double.MAX_VALUE);
		GridPane.setVgrow(textArea, Priority.ALWAYS);
		GridPane.setHgrow(textArea, Priority.ALWAYS);

		GridPane expContent = new GridPane();
		expContent.setMaxWidth(Double.MAX_VALUE);
		expContent.add(label, 0, 0);
		expContent.add(textArea, 0, 1);

		// Set expandable Exception into the dialog pane.
		alert.getDialogPane().setExpandableContent(expContent);

		alert.showAndWait();
	}
	
	public void refreshTable(TableView<Talisman> table) {
		table.setItems(FXCollections.observableArrayList(talismans));
	}
	
	public static Integer convert0ToNull(int n) {
		return n == 0 ? null : n;
	}
	
	public static Talisman createTalisman(Skill skill1, Integer level1, Skill skill2, Integer level2, Integer slot1, Integer slot2, Integer slot3) {
		List<Pair<Skill, Integer>> skills = new ArrayList<>();
		List<Integer> slots = new ArrayList<>();
		skills.add(new Pair<>(skill1, level1));
		if (skill2 != null) {
			skills.add(new Pair<>(skill2, level2));
		}
		if (slot1 != null) {
			slots.add(slot1);
		}
		if (slot2 != null) {
			slots.add(slot2);
		}
		if (slot3 != null) {
			slots.add(slot3);
		}
		return new Talisman(skills, slots);
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
