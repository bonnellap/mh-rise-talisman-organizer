package com.github.bonnellap.mh_rise_talisman_organizer.application;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.skin.ComboBoxListViewSkin;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

public class FilteredComboBox<T> extends ComboBox<T> {

	//ObservableList<T> items;
	private String typedString = "";
	
	public FilteredComboBox(ObservableList<T> items) {
		super(items);
		//this.items = items;
		
		// Set key handler
		ComboBoxKeyEvent keyEventHandler = new ComboBoxKeyEvent();
		setOnKeyReleased(keyEventHandler);
		
		
		// Prevents the space bar from closing the combo box
		ComboBoxListViewSkin<T> comboBoxListViewSkin = new ComboBoxListViewSkin<T>(this);
		comboBoxListViewSkin.getPopupContent().addEventFilter(KeyEvent.ANY, new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent event) {
				if (event.getCode() == KeyCode.SPACE) {
					if (event.getEventType() == KeyEvent.KEY_RELEASED) {
						// Run the keyEventHandler code for space bar
						keyEventHandler.handle(event);
					}
					// Prevent the combo box from closing
					event.consume();
				} else if (event.getCode() == KeyCode.TAB && event.getEventType() == KeyEvent.KEY_PRESSED) {
					// Select option and move focus to the next box
					KeyEvent enterPress = new KeyEvent(
							null,
							null,
							KeyEvent.KEY_PRESSED,
							"",
							"",
							KeyCode.ENTER,
							false,
							false,
							false,
							false
							);
					KeyEvent enterRelease = new KeyEvent(
							null,
							null,
							KeyEvent.KEY_RELEASED,
							"",
							"",
							KeyCode.ENTER,
							false,
							false,
							false,
							false
							);
					fireEvent(enterPress);
					fireEvent(enterRelease);
					fireEvent(event);
				}
			}
		});
		setSkin(comboBoxListViewSkin);
		
		// Clear typedString whenever focus changes
		focusedProperty().addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				typedString = "";
			}
		});
		
	}
	
	private class ComboBoxKeyEvent implements EventHandler<KeyEvent> {
		@Override
		public void handle(KeyEvent event) {
			if (event.getCode() == KeyCode.UP || event.getCode() == KeyCode.DOWN || event.getCode() == KeyCode.LEFT || event.getCode() == KeyCode.RIGHT) {
				typedString = "";
				return;
			} else if (event.getCode() == KeyCode.TAB) {
				show();
				return;
			} else if (event.getCode() == KeyCode.ESCAPE || event.getCode() == KeyCode.ENTER) {
				// Clear typedString
				typedString = "";
				return;
			} else if (event.getCode() == KeyCode.BACK_SPACE && typedString.length() > 0) {
				// Remove last character from typedString
				typedString = typedString.substring(0, typedString.length() - 1);
			} else if (event.getText().matches("[\\S ]")) {
				typedString += event.getText().toLowerCase();
			}
			
			for (T item : getItems()) {
				if (getConverter().toString(item).toLowerCase().startsWith(typedString.toLowerCase())) {
					// Select correct item
					getSelectionModel().select(item);
					
					// Scroll to correct item
					ComboBoxListViewSkin<?> skin = (ComboBoxListViewSkin<?>) getSkin();
					((ListView<?>) skin.getPopupContent()).scrollTo(getSelectionModel().getSelectedIndex());
					break;
				}
			}
		}
	}
	
	public T getComboBoxValue(){
        if (getSelectionModel().getSelectedIndex() < 0) {
            return null;
        } else {
            return getItems().get(getSelectionModel().getSelectedIndex());
        }
	}
	
}
