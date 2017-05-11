package com.vibridi.fxu.input;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import javafx.scene.control.DatePicker;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.util.Callback;

/**
 * 
 * @author gabriele.vaccari
 *
 * @param <S> - The type of the TableView generic type (i.e. S == TableView&lt;S&gt;). This should also match with the first generic type in TableColumn.
 */
public class DatePickerTableCell<S> extends TableCell<S,String> {

    /**
     * Provides a {@link TextField} that allows editing of the cell content when
     * the cell is double-clicked, or when {@link TableView#edit} is called. 
     * This method will work  on <code>String</code> {@link TableColumn} instances.
     * 
     * @return A {@link Callback} that can be inserted into the 
     *      {@link TableColumn#cellFactoryProperty() cell factory property} of a 
     *      TableColumn, that enables textual editing of the content.
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
    

    @Override public void startEdit() {
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
        setText(getDate().toString());	// sets the current DatePicker value as the field text 
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
                setText(item);
                setGraphic(null);
            }
        }
    }

    private void createDatePicker() {
        datePicker = new DatePicker(getDate());
        FXInput.setDateFormat(datePicker, datePattern);
       // datePicker.setMinWidth(this.getWidth() - this.getGraphicTextGap() * 2);
        datePicker.setOnKeyReleased(keyEvent -> {
        	if (keyEvent.getCode() == KeyCode.ENTER) {
        		commitEdit(datePicker.getValue().format(dateFormatter));
        	} else if (keyEvent.getCode() == KeyCode.ESCAPE) {
        		cancelEdit();
        	}
        });
    }

    /**
     * Transforms the current <code>String</code> value of this table cell in a <code>LocalDate</code> 
     * 
     * @return
     */
    private LocalDate getDate() {
        return getItem() == null ? null : LocalDate.parse(getItem(), dateFormatter);
    }
}


