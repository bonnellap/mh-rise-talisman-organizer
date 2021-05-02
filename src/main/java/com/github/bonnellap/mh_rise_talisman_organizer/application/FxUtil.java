package com.github.bonnellap.mh_rise_talisman_organizer.application;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.control.ComboBox;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;

public class FxUtil {
	public interface AutoCompleteComparator<T> {
        boolean matches(String typedText, T objectToCompare);
    }

    public static<T> void autoCompleteComboBoxPlus(ComboBox<T> comboBox, boolean dataChangeable, AutoCompleteComparator<T> comparatorMethod) {
    	ObservableList<T> data = comboBox.getItems();
    	boolean changeable = dataChangeable;
    	
        comboBox.setEditable(true);
        comboBox.getEditor().focusedProperty().addListener(observable -> {
            if (comboBox.getSelectionModel().getSelectedIndex() < 0) {
                comboBox.getEditor().setText(null);
            }
        });
        comboBox.addEventHandler(KeyEvent.KEY_PRESSED, t -> comboBox.hide());
        comboBox.addEventHandler(KeyEvent.KEY_RELEASED, new EventHandler<KeyEvent>() {

            private boolean moveCaretToPos = false;
            private int caretPos;

            @Override
            public void handle(KeyEvent event) {
            	ObservableList<T> modifiedData = comboBox.getItems();
                if (event.getCode() == KeyCode.UP) {
                    caretPos = -1;
                    if (comboBox.getEditor().getText() != null) {
                        moveCaret(comboBox.getEditor().getText().length());
                    }
                    return;
                } else if (event.getCode() == KeyCode.DOWN) {
                    if (!comboBox.isShowing()) {
                        comboBox.show();
                    }
                    caretPos = -1;
                    if (comboBox.getEditor().getText() != null) {
                        moveCaret(comboBox.getEditor().getText().length());
                    }
                    return;
                } else if (event.getCode() == KeyCode.BACK_SPACE) {
                    if (comboBox.getEditor().getText() != null) {
                        moveCaretToPos = true;
                        caretPos = comboBox.getEditor().getCaretPosition();
                    }
                } else if (event.getCode() == KeyCode.DELETE) {
                    if (comboBox.getEditor().getText() != null) {
                        moveCaretToPos = true;
                        caretPos = comboBox.getEditor().getCaretPosition();
                    }
                } else if (event.getCode() == KeyCode.TAB) {
                	/*if (!comboBox.isShowing()) {
                        comboBox.show();
                    }
                	if (comboBox.getEditor().getText() != null) {
                        moveCaret(comboBox.getEditor().getText().length());
                    }*/
                	return;
                } else if (event.getCode() == KeyCode.ENTER) {
                    return;
                }

                if (event.getCode() == KeyCode.RIGHT || event.getCode() == KeyCode.LEFT || event.getCode().equals(KeyCode.SHIFT) || event.getCode().equals(KeyCode.CONTROL)
                        || event.isControlDown() || event.getCode() == KeyCode.HOME
                        || event.getCode() == KeyCode.END) {
                    return;
                }

                ObservableList<T> list = FXCollections.observableArrayList();
                if (!changeable) {
	                for (T aData : data) {
	                    if (aData != null && comboBox.getEditor().getText() != null && comparatorMethod.matches(comboBox.getEditor().getText(), aData)) {
	                        list.add(aData);
	                    } else if (comboBox.getEditor().getText() == null) {
	                    	list.add(aData);
	                    }
	                }
                } else {
                	for (T aData : modifiedData) {
	                    if (aData != null && comboBox.getEditor().getText() != null && comparatorMethod.matches(comboBox.getEditor().getText(), aData)) {
	                        list.add(aData);
	                    } else if (comboBox.getEditor().getText() == null) {
	                    	list.add(aData);
	                    }
	                }
                }
                String t = "";
                if (comboBox.getEditor().getText() != null) {
                    t = comboBox.getEditor().getText();
                }

                comboBox.setItems(list);
                comboBox.getEditor().setText(t);
                if (!moveCaretToPos) {
                    caretPos = -1;
                }
                moveCaret(t.length());
                if (!list.isEmpty()) {
                    comboBox.show();
                }
            }

            private void moveCaret(int textLength) {
                if (caretPos == -1) {
                    comboBox.getEditor().positionCaret(textLength);
                } else {
                    comboBox.getEditor().positionCaret(caretPos);
                }
                moveCaretToPos = false;
            }
        });
        
        comboBox.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				ObservableList<T> modifiedData = comboBox.getItems();
				ObservableList<T> list = FXCollections.observableArrayList();
                if (!changeable) {
                	if (comboBox.getEditor().getText() != null && data.contains(getComboBoxValue(comboBox))) {
                		list.addAll(data);
                	} else {
		                for (T aData : data) {
		                    if (aData != null && comboBox.getEditor().getText() != null && comparatorMethod.matches(comboBox.getEditor().getText(), aData)) {
		                        list.add(aData);
		                    } else if (comboBox.getEditor().getText() == null) {
		                    	list.add(aData);
		                    }
		                }
                	}
                } else {
                	if (comboBox.getEditor().getText() != null && modifiedData.contains(getComboBoxValue(comboBox))) {
                		list.addAll(modifiedData);
                	} else {
		                for (T aData : modifiedData) {
		                    if (aData != null && comboBox.getEditor().getText() != null && comparatorMethod.matches(comboBox.getEditor().getText(), aData)) {
		                        list.add(aData);
		                    } else if (comboBox.getEditor().getText() == null) {
		                    	list.add(aData);
		                    }
		                }
                	}
                }
                String t = "";
                if (comboBox.getEditor().getText() != null) {
                    t = comboBox.getEditor().getText();
                }

                comboBox.setItems(list);
                comboBox.getEditor().setText(t);
                if (!list.isEmpty()) {
                    comboBox.show();
                }
			}
        });
    }

    public static<T> T getComboBoxValue(ComboBox<T> comboBox){
        if (comboBox.getSelectionModel().getSelectedIndex() < 0) {
            return null;
        } else {
            return comboBox.getItems().get(comboBox.getSelectionModel().getSelectedIndex());
        }
    }
}
