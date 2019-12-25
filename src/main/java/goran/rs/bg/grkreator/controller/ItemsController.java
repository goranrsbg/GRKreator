package goran.rs.bg.grkreator.controller;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import goran.rs.bg.grkreator.DB;
import goran.rs.bg.grkreator.etc.PutItem;
import goran.rs.bg.grkreator.model.Item;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.util.Callback;

public class ItemsController implements Initializable {

    @FXML
    private AnchorPane rootPane;

    @FXML
    private ListView<Item> listOfItems;

    private ObservableList<Item> items;

    private final String itemFxml = "/fxml/item.fxml";
    private final String itemTitle = "Detalji Robe";

    private final ButtonType yesButtonType = new ButtonType("Da", ButtonData.YES);
    private final ButtonType noButtonType = new ButtonType("Ne", ButtonData.NO);
    private final String TITLE = "Potvrda O Brisanju Atrikla";
    private final String QUESTION = "Da li ste sigurni da želite da obrišete artil?";

    private EntityManager em;

    private Window window;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
	em = DB.getNewEntityManager();
	Platform.runLater(() -> {
	    window = rootPane.getScene().getWindow();
	});
	initList();
    }

    private void initList() {
	items = FXCollections.<Item>observableArrayList();
	listOfItems.setItems(items);
	listOfItems.setCellFactory(new Callback<ListView<Item>, ListCell<Item>>() {

	    @Override
	    public ListCell<Item> call(ListView<Item> param) {
		return new ListCell<Item>() {

		    @Override
		    protected void updateItem(Item item, boolean empty) {
			super.updateItem(item, empty);
			if (item == null) {
			    setText("");
			} else {
			    setText(String.format("%s %n(%s, %2d%%, %.2f din.)", item.getName(),
				    item.getUnitOfMeasure(), item.getPdv(), item.getPrice()));
			}
		    }

		};
	    }

	});
    }

    private void initItems() {
	em.getTransaction().begin();
	TypedQuery<Item> query = em.createNamedQuery("Item.findAll", Item.class);
	List<Item> list = query.getResultList();
	items.addAll(list);
	em.getTransaction().commit();
    }

    @FXML
    void onCreateNewItemAction(ActionEvent event) {
	Item item = new Item("", "kom", 0d, 0);
	loadItemWindow(item);
	if (item.isValid()) {
	    items.add(item);
	}
    }

    @FXML
    void onDeleteSelectedItemAction(ActionEvent event) {
	Item item = listOfItems.getSelectionModel().getSelectedItem();
	if (item != null) {
	    createAlert(item).showAndWait().ifPresent(type -> {
		if (type == yesButtonType) {
		    em.getTransaction().begin();
		    em.remove(item);
		    em.getTransaction().commit();
		    items.remove(item);
		}
	    });
	    ;
	}
    }

    @FXML
    void onUpdateSelectedItemAction(ActionEvent event) {
	Item item = listOfItems.getSelectionModel().getSelectedItem();
	loadItemWindow(item);
	listOfItems.refresh();
    }

    private void loadItemWindow(Item item) {
	FXMLLoader loader = new FXMLLoader();
	try {
	    Parent root = loader.<Parent>load(getClass().getResourceAsStream(itemFxml));
	    PutItem controller = loader.<PutItem>getController();
	    controller.putItem(item);
	    Stage stage = new Stage();
	    stage.setScene(new Scene(root));
	    stage.initOwner(window);
	    stage.initModality(Modality.WINDOW_MODAL);
	    stage.setTitle(itemTitle);
	    stage.showAndWait();
	} catch (IOException e) {
	    e.printStackTrace();
	}
    }

    private Alert createAlert(Item item) {
	Alert alert = new Alert(AlertType.CONFIRMATION);
	alert.setTitle(TITLE);
	alert.setHeaderText(null);
	alert.setContentText(QUESTION + System.lineSeparator() + item.toShortString());
	alert.getButtonTypes().clear();
	alert.getButtonTypes().addAll(noButtonType, yesButtonType);
	return alert;
    }

    public void setEntityManager(EntityManager em) {
	this.em = em;
	initItems();
    }

}
