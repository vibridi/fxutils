package com.vibridi.fxu.input;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import javafx.scene.control.DatePicker;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.input.KeyCode;
import javafx.util.Callback;

/**
 * Provides a date picker implementation of a {@link TableCell}. When cell editing begins, this shows 
 * a date picker instead of a normal text field. 
 *
 * @param <S> - The type of the TableView generic type (TableView&lt;S&gt;). 
 * This should also match the first generic type in TableColumn (TableColumn&lt;S,T&gt;).
 */
public class DatePickerTableCell<S> extends TableCell<S,String> {

    /**
     * Provides a table cell containing a date picker that show up when the cell is double-clicked 
     * or {@link TableView#edit} is called. The callback works on string-type {@link TableColumn} instances.
     * 
     * @param datePattern The date pattern format used to parse the content of this cell
     * @return A {@link Callback} for a {@link TableColumn#cellFactoryProperty} of a TableColumn, that 
     * will allow editing the content with a DatePicker widget.
     * @param <S> Type of the TableColumn
     */
    public static <S> Callback<TableColumn<S,String>, TableCell<S,String>> forTableColumn(String datePattern) {
    	return (TableColumn<S,String> col) -> { return new DatePickerTableCell<S>(datePattern); };
    }
	
    private DatePicker datePicker;
    private String datePattern;
    private DateTimeFormatter dateFormatter;
    
    public DatePickerTableCell(String datePattern) {
        this.getStyleClass().add("text-field-table-cell");
        this.datePattern = datePattern;
        this.dateFormatter = DateTimeFormatter.ofPattern(datePattern);
    }
    

    @Override 
    public void startEdit() {
        if (!isEditable() 
                || !getTableView().isEditable() 
                || !getTableColumn().isEditable()) {
            return;
        }
        super.startEdit();

        if(isEditing()) {
            if(datePicker == null) 
            	createDatePicker();

            datePicker.setValue(getDate());
        	setText(null);
        	setGraphic(datePicker);
        	datePicker.requestFocus();
        }
    }

    @Override
    public void cancelEdit() {
        super.cancelEdit();        
        setText(getItem() == null ? "" : getItem());
        setGraphic(null);
    }

    @Override
    public void updateItem(String item, boolean empty) {
        super.updateItem(item, empty);

        if(empty) {
            setText(null);
            setGraphic(null);
        } else {
            if (isEditing()) {
                if (datePicker != null) {
                    datePicker.setValue(getDate());
                }
                setText(null);
                setGraphic(datePicker);
            } else {
                setText(getItem() == null ? "" : getItem());
                setGraphic(null);
            }
        }
    }

    private void createDatePicker() {
        datePicker = new DatePicker(getDate());
        FXInput.setDateFormat(datePicker, datePattern);
       // datePicker.setMinWidth(this.getWidth() - this.getGraphicTextGap() * 2);
        datePicker.setOnKeyReleased(keyEvent -> {
        	if(keyEvent.getCode() == KeyCode.ENTER) {
        		commitEdit(datePicker.getValue().format(dateFormatter)); // TODO check for empty/null values
        	} else if(keyEvent.getCode() == KeyCode.ESCAPE) {
        		cancelEdit();
        	}
        });
        
        datePicker.focusedProperty().addListener((obsValue, oldValue, newValue) -> {
        	if(oldValue == true && newValue == false)
        		commitEdit(datePicker.getValue().format(dateFormatter));
        });
    }

    /**
     * Transforms the current <code>String</code> value of this table cell in a <code>LocalDate</code> 
     * 
     * @return The LocalDate representation of the cell value.
     */
    private LocalDate getDate() {
        return getItem() == null ? LocalDate.now() : LocalDate.parse(getItem(), dateFormatter);
    }
}


