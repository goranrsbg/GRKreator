package goran.rs.bg.grkreator.controller;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.UnaryOperator;

import javax.persistence.EntityManager;

import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXTextField;

import goran.rs.bg.grkreator.DB;
import goran.rs.bg.grkreator.etc.PutItem;
import goran.rs.bg.grkreator.model.Document;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.TextFormatter.Change;
import javafx.stage.Window;

public class DocumentController implements Initializable, PutItem {

    @FXML
    private JFXTextField titleTextField;

    @FXML
    private JFXTextField documentNumberTextField;

    @FXML
    private JFXTextField documentSettlementTextField;

    @FXML
    private JFXCheckBox inPdvCheckBox;

    private Document documentDetails;

    private Window window;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
	Platform.runLater(() -> {
	    this.window = titleTextField.getScene().getWindow();
	});

	UnaryOperator<Change> filter = change -> {
	    if (change.getText().matches("\\d*")) {
		return change;
	    }
	    return null;
	};
	documentNumberTextField.setTextFormatter(new TextFormatter<String>(filter));
    }

    @FXML
    void onCancelButtonAction(ActionEvent event) {
	window.hide();
    }

    @FXML
    void onSaveButtonAction(ActionEvent event) {
	if (isValid()) {
	    documentDetails.setTitle(titleTextField.getText());
	    documentDetails.setValue(Integer.parseInt(documentNumberTextField.getText()));
	    documentDetails.setSettlement(documentSettlementTextField.getText());
	    documentDetails.setInPdv(inPdvCheckBox.isSelected());
	    EntityManager em = DB.getNewEntityManager();
	    em.getTransaction().begin();
	    em.merge(documentDetails);
	    em.getTransaction().commit();
	    em.close();
	}
	window.hide();
    }

    private boolean isValid() {
	return !titleTextField.getText().trim().isEmpty() && !documentNumberTextField.getText().trim().isEmpty()
		&& !documentSettlementTextField.getText().trim().isEmpty();
    }

    @Override
    public void putItem(Object item) {
	this.documentDetails = (Document) item;
	titleTextField.setText(documentDetails.getTitle());
	documentNumberTextField.setText(Integer.toString(documentDetails.getValue()));
	documentSettlementTextField.setText(documentDetails.getSettlement());
	inPdvCheckBox.setSelected(documentDetails.isInPdvSystem());
    }

}
