package goran.rs.bg.grkreator.controller;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.UnaryOperator;

import javax.persistence.EntityManager;

import com.jfoenix.controls.JFXTextField;

import goran.rs.bg.grkreator.DB;
import goran.rs.bg.grkreator.etc.PutItem;
import goran.rs.bg.grkreator.model.Item;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.TextFormatter.Change;
import javafx.stage.Window;

public class ItemController implements Initializable, PutItem {

	@FXML
	private JFXTextField nameTextField;

	@FXML
	private JFXTextField unitOfMeasureTextField;

	@FXML
	private JFXTextField priceTextField;

	@FXML
	private JFXTextField pdvTextField;

	private Item item;
	
	private Window window;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		Platform.runLater(()->{
			window = nameTextField.getScene().getWindow();
		});
		UnaryOperator<Change> filterDouble = change -> {
			if(change.getText().matches("^\\d*\\.?\\d*")) {
				return change;
			} else {
				return null;
			}
		};
		priceTextField.setTextFormatter(new TextFormatter<String>(filterDouble));
		UnaryOperator<Change> filterInt = change -> {
			if(change.getText().matches("\\d*")) {
				return change;
			} else {
				return null;
			}
		};
		pdvTextField.setTextFormatter(new TextFormatter<>(filterInt));
	}

	@FXML
	void onCancelButtonAction(ActionEvent event) {
		window.hide();
	}

	@FXML
	void onSaveButtonAction(ActionEvent event) {
		if(validFields()) {
			updateItem();
			EntityManager em = DB.getNewEntityManager();
			em.getTransaction().begin();
			if(item.isValid()) {
				em.merge(item);
			} else {
				em.persist(item);
			}
			em.getTransaction().commit();
			em.close();
			window.hide();
		}
	}

	public void putItem(Object item) {
		this.item = (Item) item;
		displayItem();
	}

	private void displayItem() {
		nameTextField.setText(item.getName());
		unitOfMeasureTextField.setText(item.getUnitOfMeasure());
		priceTextField.setText(item.getPrice() + "");
		pdvTextField.setText(item.getPdv() + "");
	}

	private boolean validFields() {
		return !nameTextField.getText().trim().isEmpty() && !unitOfMeasureTextField.getText().trim().isEmpty() && !priceTextField.getText().trim().isEmpty() && !pdvTextField.getText().trim().isEmpty();
	}
	
	private void updateItem() {
		item.setName(nameTextField.getText().trim());
		item.setUnitOfMeasure(unitOfMeasureTextField.getText().trim());
		item.setPrice(Double.parseDouble(priceTextField.getText()));
		item.setPdv(Integer.parseInt(pdvTextField.getText()));
	}
	
}
