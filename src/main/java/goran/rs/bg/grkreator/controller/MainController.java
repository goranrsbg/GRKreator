package goran.rs.bg.grkreator.controller;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;
import javax.persistence.TypedQuery;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextField;

import goran.rs.bg.grkreator.DB;
import goran.rs.bg.grkreator.NTWC;
import goran.rs.bg.grkreator.etc.PutItem;
import goran.rs.bg.grkreator.etc.Store;
import goran.rs.bg.grkreator.etc.StoreData;
import goran.rs.bg.grkreator.format.FirmListFormatCell;
import goran.rs.bg.grkreator.itemtable.ItemTable;
import goran.rs.bg.grkreator.model.Document;
import goran.rs.bg.grkreator.model.Firm;
import goran.rs.bg.grkreator.model.Item;
import goran.rs.bg.grkreator.thread.SearchFirmsService;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.DoubleBinding;
import javafx.beans.binding.NumberBinding;
import javafx.beans.binding.StringBinding;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.print.PageLayout;
import javafx.print.PageOrientation;
import javafx.print.Paper;
import javafx.print.Printer.MarginType;
import javafx.print.PrinterJob;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TablePosition;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.scene.transform.Scale;
import javafx.scene.transform.Transform;
import javafx.scene.transform.Translate;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.util.Callback;
import javafx.util.StringConverter;

public class MainController implements Initializable {

    @FXML
    private AnchorPane centerAncorPane;

    @FXML
    private Label sellerLabel;

    @FXML
    private ImageView sellerFirmLogoImageView;

    @FXML
    private GridPane sellerFirmDataGridPane;

    @FXML
    private Label buyerLabel;

    @FXML
    private ImageView buyerFirmLogoImageView;

    @FXML
    private GridPane buyerFirmDataGridPane;

    @FXML
    private VBox searchBox;

    @FXML
    private JFXTextField searchTextField;

    @FXML
    private ListView<Firm> searchListView;

    @FXML
    private HBox buyerBox;

    @FXML
    private Label labelTitle;

    @FXML
    private Label labelAccoundNumber;

    @FXML
    private Label labelSettlement;

    @FXML
    private JFXDatePicker sellingDatePicker;

    @FXML
    private JFXDatePicker recivingDatePicker;

    @FXML
    private JFXDatePicker lastPayDayDatePicker;

    @FXML
    private JFXButton removeRowButton;

    @FXML
    private TableView<ItemTable> itemsTableView;

    @FXML
    private TableColumn<ItemTable, String> rbCol;

    @FXML
    private TableColumn<ItemTable, String> nameCol;

    @FXML
    private TableColumn<ItemTable, String> uomCol;

    @FXML
    private TableColumn<ItemTable, String> qtyCol;

    @FXML
    private TableColumn<ItemTable, String> priceCol;

    @FXML
    private TableColumn<ItemTable, String> semiPriceCol;

    @FXML
    private TableColumn<ItemTable, String> pdvCol;

    @FXML
    private TableColumn<ItemTable, String> pdvPriceCol;

    @FXML
    private TableColumn<ItemTable, String> totalCol;

    @FXML
    private ListView<Item> searchItemListView;

    @FXML
    private Label labelSemiPrice;

    @FXML
    private Label labelPdvNumber;

    @FXML
    private Label labelTotalNumber;

    @FXML
    private Label labelNumberWords;

    @FXML
    private HBox pdvHbox;

    private NumberBinding semiPrice;
    private NumberBinding pdvPrice;
    private NumberBinding totalPrice;

    private ObservableList<ItemTable> tableItems;
    private ObservableList<Item> searchListItems;
    private ItemTable selectedItem;
    private int row;
    /**
     * Indicator for displaying firms data.
     */
    public static final char SHOW = '1';

    /**
     * Indicator for hiding firms data.
     */
    public static final char HIDE = '0';

    /**
     * The Seller firm must be the first record in the database.
     */
    private final int SELLER_FIRM_ID = 1;

    /**
     * One and only record in Account Number table
     */
    private final int DOCUMENT_DETAILS_ID = 1;

    /**
     * Search types
     */
    private final String[] SEARCH_PROMPT_TEXT = { "Traži po imenu", "Traži po PIB-u", "Traži po ID-u" };
    private IntegerProperty searchTypeIndex = new SimpleIntegerProperty();
    private SearchFirmsService searchFirmsService;
    private DateTimeFormatter dateTimeFormatter;

    private Firm sellerFirm;
    private Firm buyerFirm;
    private Document documentDetails;

    private final String firmFxml = "/fxml/firm.fxml";
    private final String documentFxml = "/fxml/document.fxml";
    private final String itemsFxml = "/fxml/items.fxml";
    private final String printFxml = "/fxml/print.fxml";
    private final String documentTitle = "Detalji računa";
    private final String itemsTitle = "Spisak Artikala";

    private Window window;

    private EntityManager em;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
	em = DB.getNewEntityManager();
	Platform.runLater(() -> {
	    window = buyerLabel.getScene().getWindow();
	    buyerBox.getChildren().remove(searchBox);
	    searchFirmsService = new SearchFirmsService(searchListView.getItems(), searchTextField, searchTypeIndex,
		    em);
	});
	sellerFirm = selectFirm(SELLER_FIRM_ID);
	if (sellerFirm == null) {
	    sellerFirm = new Firm();
	    persist(sellerFirm);
	}

	searchListView.setCellFactory(new Callback<ListView<Firm>, ListCell<Firm>>() {
	    @Override
	    public ListCell<Firm> call(ListView<Firm> param) {
		return new FirmListFormatCell();
	    }
	});

	showFirmData(sellerFirmLogoImageView, sellerFirmDataGridPane, sellerFirm);
	dateTimeFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy.", new Locale("sr"));
	StringConverter<LocalDate> dateConverter = new StringConverter<LocalDate>() {
	    @Override
	    public String toString(LocalDate object) {
		return object == null ? null : object.format(dateTimeFormatter);
	    }

	    @Override
	    public LocalDate fromString(String string) {
		return (string == null || string.trim().isEmpty()) ? null : LocalDate.parse(string, dateTimeFormatter);
	    }
	};
	recivingDatePicker.setConverter(dateConverter);
	sellingDatePicker.setConverter(dateConverter);
	lastPayDayDatePicker.setConverter(dateConverter);
	Platform.runLater(() -> {
	    sellingDatePicker.setValue(LocalDate.now());
	    recivingDatePicker.setValue(LocalDate.now());
	    lastPayDayDatePicker.setValue(LocalDate.now().plusDays(7L));
	});
	searchListItems = FXCollections.observableArrayList();
	searchItemListView.setItems(searchListItems);
	searchItemListView.focusedProperty().addListener(new ChangeListener<Boolean>() {
	    @Override
	    public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
		if (!newValue) {
		    searchItemListView.setVisible(false);
		}
	    }
	});
	findDocumenDetails();
	displayDocumentDetails();
	initSums();
	initTable();
    }

    private void initTable() {
	initTotalSums();
	tableItems = FXCollections.observableArrayList();
	itemsTableView.setItems(tableItems);
	rbCol.setCellValueFactory(new PropertyValueFactory<>("rowNo"));
	nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
	nameCol.setCellFactory(TextFieldTableCell.forTableColumn());
	uomCol.setCellValueFactory(new PropertyValueFactory<>("unitOfMeasure"));
	priceCol.setCellValueFactory(
		new Callback<TableColumn.CellDataFeatures<ItemTable, String>, ObservableValue<String>>() {
		    @Override
		    public ObservableValue<String> call(CellDataFeatures<ItemTable, String> param) {
			DoubleBinding binding = new DoubleBinding() {
			    {
				super.bind(param.getValue().priceProperty(), param.getValue().pdvProperty());
			    }

			    @Override
			    protected double computeValue() {
				return param.getValue().getRealPrice();
			    }
			};
			return binding.asString("%,.2f din.");
		    }
		});
	qtyCol.setCellFactory(TextFieldTableCell.forTableColumn());
	qtyCol.setCellValueFactory(
		new Callback<TableColumn.CellDataFeatures<ItemTable, String>, ObservableValue<String>>() {
		    @Override
		    public ObservableValue<String> call(CellDataFeatures<ItemTable, String> param) {
			DoubleProperty quantityProperty = param.getValue().quantityProperty();
			StringBinding sb = new StringBinding() {
			    {
				super.bind(quantityProperty);
			    }

			    @Override
			    protected String computeValue() {
				String value = null;
				if (quantityProperty.get() == Math.floor(quantityProperty.get())) {
				    value = String.valueOf((long) quantityProperty.get());
				} else {
				    value = String.format("%,.2f", quantityProperty.get());
				}
				return value;
			    }
			};
			return sb;
		    }
		});
	semiPriceCol.setCellValueFactory(
		new Callback<TableColumn.CellDataFeatures<ItemTable, String>, ObservableValue<String>>() {
		    @Override
		    public ObservableValue<String> call(CellDataFeatures<ItemTable, String> param) {
			return param.getValue().semiPriceProperty().asString("%,.2f din.");
		    }
		});
	pdvCol.setCellValueFactory(
		new Callback<TableColumn.CellDataFeatures<ItemTable, String>, ObservableValue<String>>() {
		    @Override
		    public ObservableValue<String> call(CellDataFeatures<ItemTable, String> param) {
			return param.getValue().pdvProperty().asString("%d%%");
		    }
		});
	pdvPriceCol.setCellValueFactory(
		new Callback<TableColumn.CellDataFeatures<ItemTable, String>, ObservableValue<String>>() {
		    @Override
		    public ObservableValue<String> call(CellDataFeatures<ItemTable, String> param) {
			return param.getValue().pdvPriceProperty().asString("%,.2f din.");
		    }
		});
	totalCol.setCellValueFactory(
		new Callback<TableColumn.CellDataFeatures<ItemTable, String>, ObservableValue<String>>() {
		    @Override
		    public ObservableValue<String> call(CellDataFeatures<ItemTable, String> param) {
			return param.getValue().totalPriceProperty().asString("%,.2f din.");
		    }
		});
	setPdvVisibility();
	tableAddNewEmptyRow();
	editName(tableItems.size() - 1);
	searchItemListView.setCellFactory(new Callback<ListView<Item>, ListCell<Item>>() {
	    @Override
	    public ListCell<Item> call(ListView<Item> param) {
		ListCell<Item> cell = new ListCell<Item>() {
		    @Override
		    protected void updateItem(Item item, boolean empty) {
			super.updateItem(item, empty);
			if (item != null) {
			    setText(String.format("%s (%.2f din.)[%d%%]", item.getName(), item.getPrice(),
				    item.getPdv()));
			} else {
			    setText("");
			}
		    }
		};
		return cell;
	    }
	});
    }

    private void setPdvVisibility() {
	semiPriceCol.setVisible(documentDetails.isInPdvSystem());
	pdvCol.setVisible(documentDetails.isInPdvSystem());
	pdvPriceCol.setVisible(documentDetails.isInPdvSystem());
	pdvHbox.setVisible(documentDetails.isInPdvSystem());
    }

    private void initSums() {
	initTotalSums();
	bindSumLabels();
    }

    private void initTotalSums() {
	semiPrice = Bindings.add(new SimpleDoubleProperty(0d), new SimpleDoubleProperty(0d));
	pdvPrice = Bindings.add(new SimpleDoubleProperty(0d), new SimpleDoubleProperty(0d));
	totalPrice = Bindings.add(new SimpleDoubleProperty(0d), new SimpleDoubleProperty(0d));
    }

    /**
     * Loads firm editor view. When view is closed the database gets updated.
     * 
     * @param event
     * @throws IOException
     * @throws Exception
     */
    @FXML
    void onLeftButtonAction(ActionEvent event) throws IOException {
	showDetailsDialog(sellerLabel.getText(), firmFxml, sellerFirm);
	merge(sellerFirm);
	showFirmData(sellerFirmLogoImageView, sellerFirmDataGridPane, sellerFirm);
    }

    @FXML
    void onCenterButtonAction(ActionEvent event) {
	showDetailsDialog(documentTitle, documentFxml, documentDetails);
	displayDocumentDetails();
	setPdvVisibility();
    }

    @FXML
    void onAddBuyerButtonAction(ActionEvent event) {
	Firm firm = new Firm();
	showDetailsDialog(buyerLabel.getText(), firmFxml, firm);
	// update the database
	if (firm.isFirmValid()) {
	    persist(firm);
	    // update buyer firm shown details
	    if (buyerFirm == null) {
		buyerFirm = firm;
		showByerData();
	    }
	}
    }

    @FXML
    void onChangeBuyerButtonAction(ActionEvent event) {
	if (buyerFirm != null) {
	    showDetailsDialog(buyerLabel.getText(), firmFxml, buyerFirm);
	    merge(buyerFirm);
	    showByerData();
	}
    }

    @FXML
    void onSearchBuyerButtonAction(ActionEvent event) {
	// toggle search view
	if (buyerBox.getChildren().contains(searchBox)) {
	    buyerBox.getChildren().remove(searchBox);
	    searchFirmsService.cancel();
	    searchFirmsService.reset();
	} else {
	    buyerBox.getChildren().add(searchBox);
	    searchFirmsService.start();
	}
    }

    @FXML
    void onSearchUpAction(ActionEvent event) {
	searchTypeIndex.set((searchTypeIndex.get() + 1) % SEARCH_PROMPT_TEXT.length);
	searchTextField.setPromptText(SEARCH_PROMPT_TEXT[searchTypeIndex.get()]);
	searchTextField.setText("");
	searchFirmsService.resetSearch();
    }

    @FXML
    void onSearchDownAction(ActionEvent event) {
	searchTypeIndex.set((searchTypeIndex.get() + SEARCH_PROMPT_TEXT.length - 1) % SEARCH_PROMPT_TEXT.length);
	searchTextField.setPromptText(SEARCH_PROMPT_TEXT[searchTypeIndex.get()]);
	searchTextField.setText("");
	searchFirmsService.resetSearch();
    }

    @FXML
    void onSearchListViewKeyPressed(KeyEvent event) {
	if (event.getCode() == KeyCode.ENTER) {
	    showSelectedFirm();
	}
    }

    @FXML
    void onSearchListViewMouseClicked(MouseEvent event) {
	if (event.getClickCount() == 2) {
	    showSelectedFirm();
	}
    }

    @FXML
    void onNameEditCommit(TableColumn.CellEditEvent<ItemTable, String> event) {
	ItemTable selectedItem = event.getRowValue();
	if (!event.getNewValue().isEmpty()) {
	    List<Item> foundItems = findItems(event.getNewValue());
	    int row = event.getTablePosition().getRow();
	    if (foundItems.isEmpty()) {
		showAlertMessage(AlertType.INFORMATION,
			"Ne postoji artikl koji sadrži slova: *" + event.getNewValue() + "*");
		selectRow(row);
	    } else if (foundItems.size() == 1) {
		selectedItem.setItem(foundItems.get(0));
		selectRow(row);
	    } else {
		this.selectedItem = selectedItem;
		this.row = row;
		searchListItems.addAll(foundItems);

		double topAnchorTable = itemsTableView.localToScene(itemsTableView.getBoundsInLocal()).getMinY();
		double leftMargin = rbCol.getWidth() + nameCol.getWidth();

		double topMargin = topAnchorTable + 37d;
		AnchorPane.setTopAnchor(searchItemListView, topMargin);
		AnchorPane.setLeftAnchor(searchItemListView, leftMargin);
		searchItemListView.setVisible(true);

		Platform.runLater(() -> {
		    searchItemListView.requestFocus();
		});
	    }
	}
	itemsTableView.refresh();
	event.consume();
    }

    @FXML
    void onQuantityEditCommit(TableColumn.CellEditEvent<ItemTable, String> event) {
	event.consume();
	try {
	    double value = Double.parseDouble(event.getNewValue());
	    ItemTable selectedItem = event.getRowValue();
	    int cRow = event.getTablePosition().getRow();
	    selectedItem.setQuantity(value);
	    ItemTable lastItemTable = tableItems.get(tableItems.size() - 1);
	    if (value > 0d && lastItemTable.getItem().isValid() && lastItemTable.getQuantity() > 0d) {
		tableAddNewEmptyRow();
		updateRowNoValues();
	    }
	    selectRow(cRow + 1);
	} catch (Exception e) {
	    itemsTableView.refresh();
	}
    }

    @FXML
    void onTableKeyPressed(KeyEvent event) {
	@SuppressWarnings("rawtypes")
	ObservableList<TablePosition> selectedCells = itemsTableView.getSelectionModel().getSelectedCells();
	if (selectedCells.size() == 1) {
	    if (event.getCode() == KeyCode.ENTER) {
		editName(selectedCells.get(0).getRow());
	    } else if (event.getCode() == KeyCode.DELETE) {
		removeRowButton.fire();
	    } else if (event.getCode() == KeyCode.DIGIT1 || event.getCode() == KeyCode.DIGIT2
		    || event.getCode() == KeyCode.DIGIT3 || event.getCode() == KeyCode.DIGIT4
		    || event.getCode() == KeyCode.DIGIT5 || event.getCode() == KeyCode.DIGIT6
		    || event.getCode() == KeyCode.DIGIT7 || event.getCode() == KeyCode.DIGIT8
		    || event.getCode() == KeyCode.DIGIT9 || event.getCode() == KeyCode.NUMPAD1
		    || event.getCode() == KeyCode.NUMPAD2 || event.getCode() == KeyCode.NUMPAD3
		    || event.getCode() == KeyCode.NUMPAD4 || event.getCode() == KeyCode.NUMPAD5
		    || event.getCode() == KeyCode.NUMPAD6 || event.getCode() == KeyCode.NUMPAD7
		    || event.getCode() == KeyCode.NUMPAD8 || event.getCode() == KeyCode.NUMPAD9) {
		int sRow = selectedCells.get(0).getRow();
		if (tableItems.get(sRow).getItem().isValid()) {
		    editQuantity(sRow);
		}
	    }
	}
    }

    @FXML
    void onSearchItemListViewKeyPressed(KeyEvent event) {
	if (event.getCode() == KeyCode.ESCAPE) {
	    searchItemListView.setVisible(false);
	} else if (event.getCode() == KeyCode.ENTER) {
	    Item item = searchItemListView.getSelectionModel().getSelectedItem();
	    if (item != null) {
		selectedItem.setItem(searchItemListView.getSelectionModel().getSelectedItem());
		goEditQty();
	    }
	}
    }

    @FXML
    void onSearchItemListViewMouseClicked(MouseEvent event) {
	if (event.getClickCount() == 2) {
	    selectedItem.setItem(searchItemListView.getSelectionModel().getSelectedItem());
	    goEditQty();
	}
    }

    @FXML
    void onRemoveRowButtonAction(ActionEvent event) {
	ItemTable itemTable = itemsTableView.getSelectionModel().getSelectedItem();
	if (itemTable != null && itemTable.getItem().isValid() && tableItems.size() > 1) {
	    tableItems.remove(itemTable);
	    updateSumBindings();
	    updateRowNoValues();
	}
    }

    @FXML
    void onShowAllArticlesButtonAction(ActionEvent event) {
	FXMLLoader loader = new FXMLLoader();
	Stage stage = new Stage();
	Parent root;
	try {
	    root = loader.<Parent>load(getClass().getResourceAsStream(itemsFxml));
	    ItemsController controller = loader.<ItemsController>getController();
	    controller.setEntityManager(em);
	    Scene scene = new Scene(root);
	    stage.setScene(scene);
	    stage.setTitle(itemsTitle);
	    stage.initOwner(window);
	    stage.initModality(Modality.WINDOW_MODAL);
	    stage.showAndWait();
	} catch (IOException e) {
	    e.printStackTrace();
	    showAlertMessage(AlertType.ERROR, "Error loading fxml file. (" + itemsFxml + ")" + " " + e.getMessage());
	}
    }

    @FXML
    void onPrintButtonAction(ActionEvent event) {
	if (doPrint()) {
	    documentDetails.setValue(documentDetails.getValue() + 1);
	    em.getTransaction().begin();
	    em.merge(documentDetails);
	    em.getTransaction().commit();
	}
    }

    private void updateSumBindings() {
	initTotalSums();
	for (ItemTable item : tableItems) {
	    addTotalSumValue(item);
	}
    }

    private void selectRow(int rowNo) {
	itemsTableView.getSelectionModel().clearAndSelect(rowNo);
	itemsTableView.requestFocus();
    }

    private void goEditQty() {
	searchItemListHide();
	editQuantity(row);
    }

    private void editQuantity(int row) {
	Platform.runLater(() -> {
	    itemsTableView.edit(row, qtyCol);
	});
    }

    private void editName(int row) {
	Platform.runLater(() -> {
	    itemsTableView.edit(row, nameCol);
	});
    }

    private List<Item> findItems(String value) {
	em.getTransaction().begin();
	TypedQuery<Item> nQuery = em.createNamedQuery("Item.findByName", Item.class);
	nQuery.setParameter("name", "%" + value.toLowerCase() + "%");
	List<Item> itemList = nQuery.getResultList();
	em.getTransaction().commit();
	return itemList;
    }

    private void showSelectedFirm() {
	Firm selectedItem = searchListView.getSelectionModel().getSelectedItem();
	if (selectedItem != null) {
	    buyerFirm = selectedItem;
	    showByerData();
	}
    }

    private void showByerData() {
	showFirmData(buyerFirmLogoImageView, buyerFirmDataGridPane, buyerFirm);
    }

    /**
     * Dialog for updating firms details.
     * 
     * @param title Window title
     * @param firm  The firm to be updated.
     * @throws IOException
     */
    private void showDetailsDialog(String title, String fxmlPath, Object item) {
	FXMLLoader loader = new FXMLLoader();
	Stage stage = new Stage();
	Parent root;
	try {
	    root = loader.<Parent>load(getClass().getResourceAsStream(fxmlPath));
	    Scene scene = new Scene(root);
	    stage.setTitle(title);
	    stage.setScene(scene);
	    stage.initOwner(window);
	    stage.initModality(Modality.WINDOW_MODAL);
	    PutItem controller = loader.<PutItem>getController();
	    controller.putItem(item);
	    stage.showAndWait();
	} catch (IOException e) {
	    showAlertMessage(AlertType.ERROR, "Error loading fxml file. (" + fxmlPath + ")");
	}
    }

    /**
     * Display firms data.
     * 
     * @param imageView Stores firms logo image.
     * @param gridPane
     * @param firm
     */
    private void showFirmData(ImageView imageView, GridPane gridPane, Firm firm) {
	char[] displayData = firm.getDisplayData().toCharArray();
	int nextRow = 0;
	Object[] nodes = gridPane.getChildren().toArray();
	// set data
	for (int i = 0; i < displayData.length - 1; i++) {
	    if (displayData[i] == MainController.SHOW) {
		Text leftCol = (Text) nodes[nextRow * 2];
		Text rightCol = (Text) nodes[nextRow * 2 + 1];
		leftCol.setText(firm.getDataNameString(i));
		rightCol.setText(firm.getDataString(i));
		nextRow++;
	    }
	}
	// clear rest of the rows
	for (int i = nextRow * 2; i < nodes.length; i++) {
	    Text text = (Text) nodes[i];
	    text.setText(null);
	}
	// set image
	if (displayData[displayData.length - 1] == MainController.SHOW) {
	    Image logoImage = firm.getLogoImage();
	    imageView.setImage(logoImage);
	} else {
	    imageView.setImage(null);
	}
    }

    /**
     * Select the firm from the database.
     * 
     * @param ID The firms ID.
     * @return The firm with given ID or null if record with ID is not present in
     *         database.
     */
    private Firm selectFirm(final int ID) {
	Firm f = null;
	try {
	    em.getTransaction().begin();
	    f = em.find(Firm.class, ID);
	    em.getTransaction().commit();
	} catch (PersistenceException e) {
	    System.err.println(e.getMessage());
	    em.getTransaction().rollback();
	}
	return f;
    }

    /**
     * Persist the firm to the database.
     * 
     * @param firm The firm to be persistent.
     */
    private void persist(final Firm firm) {
	try {
	    em.getTransaction().begin();
	    em.persist(firm);
	    em.getTransaction().commit();
	} catch (PersistenceException e) {
	    em.getTransaction().rollback();
	}
    }

    /**
     * Update the firm to the database.
     * 
     * @param firm The firm to be updated.
     */
    private void merge(final Firm firm) {
	try {
	    em.getTransaction().begin();
	    em.merge(firm);
	    em.getTransaction().commit();
	} catch (PersistenceException e) {
	    em.getTransaction().rollback();
	}
    }

    /**
     * Adds new empty row to the items table view.
     */
    private void tableAddNewEmptyRow() {
	ItemTable item = new ItemTable(new Item("", "kom", 0d, 0));
	tableItems.add(item);
	addTotalSumValue(item);
    }

    private void addTotalSumValue(ItemTable item) {
	semiPrice = Bindings.add(semiPrice, new DoubleBinding() {
	    {
		super.bind(item.semiPriceProperty());
	    }

	    @Override
	    protected double computeValue() {
		return Double.parseDouble(item.semiPriceProperty().asString("%.2f").get());
	    }
	});
	pdvPrice = Bindings.add(pdvPrice, new DoubleBinding() {
	    {
		super.bind(item.pdvPriceProperty());
	    }

	    @Override
	    protected double computeValue() {
		return Double.parseDouble(item.pdvPriceProperty().asString("%.2f").get());
	    }
	});
	totalPrice = Bindings.add(totalPrice, item.totalPriceProperty());
	bindSumLabels();
    }

    private void bindSumLabels() {
	labelSemiPrice.textProperty().bind(semiPrice.asString("%,.2f din."));
	labelPdvNumber.textProperty().bind(pdvPrice.asString("%,.2f din."));
	labelTotalNumber.textProperty().bind(totalPrice.asString("%,.2f din."));
	labelNumberWords.textProperty().bind(new StringBinding() {
	    {
		super.bind(totalPrice);
	    }

	    @Override
	    protected String computeValue() {
		String convert = "";
		try {
		    convert = NTWC.convert(totalPrice.doubleValue());
		} catch (Exception e) {
		    convert = e.getMessage();
		}
		return convert;
	    }
	});
    }

    private void findDocumenDetails() {
	try {
	    em.getTransaction().begin();
	    documentDetails = em.find(Document.class, DOCUMENT_DETAILS_ID);
	    if (documentDetails == null) {
		documentDetails = new Document(DOCUMENT_DETAILS_ID, 1, "Račun", "Bez naselja", true);
		em.persist(documentDetails);
	    }
	    Store.put(StoreData.DOCUMENT_DETAILS.ID, documentDetails);
	    em.getTransaction().commit();
	} catch (PersistenceException e) {
	    em.getTransaction().rollback();
	}
    }

    private void displayDocumentDetails() {
	labelAccoundNumber
		.setText(String.format("Rb: %04d/%d", documentDetails.getValue(), LocalDate.now().getYear() % 100));
	labelTitle.setText(documentDetails.getTitle());
	labelSettlement.setText(documentDetails.getSettlement());
    }

    private void updateRowNoValues() {
	for (int i = 0; i < tableItems.size() - 1; i++) {
	    tableItems.get(i).setRowNo(i + 1);
	}
    }

    private void searchItemListHide() {
	selectedItem = null;
	searchItemListView.setVisible(false);
	searchListItems.clear();
    }

    private boolean doPrint() {

	PrinterJob job = PrinterJob.createPrinterJob();
	if (job == null) {
	    showAlertMessage(AlertType.ERROR, "Štampač nije pronađen.");
	    return false;
	}
	job.getJobSettings().copiesProperty().set(1);
	if (!job.showPrintDialog(window)) {
	    return false;
	}

	PageLayout pageLayout = job.getPrinter().createPageLayout(Paper.A4, PageOrientation.PORTRAIT,
		MarginType.DEFAULT);

	final double printableHeight = pageLayout.getPrintableHeight();
	final double printableWidth = pageLayout.getPrintableWidth();

	AnchorPane root = null;
	ArrayList<HBox> firstCells = null;
	try {
	    FXMLLoader loader = new FXMLLoader();
	    root = loader.<AnchorPane>load(getClass().getResourceAsStream(printFxml));
	    PrintController controller = loader.<PrintController>getController();
	    controller.setDocumentDetails(documentDetails);
	    controller.setTableItems(tableItems);
	    controller.setSellerFirm(sellerFirm);
	    controller.setBuyerFirm(buyerFirm);
	    controller.setDates(sellingDatePicker.getValue().format(dateTimeFormatter) + " godine",
		    recivingDatePicker.getValue().format(dateTimeFormatter) + " godine",
		    lastPayDayDatePicker.getValue().format(dateTimeFormatter) + " godine");
	    controller.setTotalPrices(labelSemiPrice.getText(), labelPdvNumber.getText(), labelTotalNumber.getText(),
		    labelNumberWords.getText());
	    firstCells = controller.getFirstCells();
	    root.autosize();
	    Scene scene = new Scene(root);
	    Stage stage = new Stage();
	    scene.addEventHandler(KeyEvent.KEY_PRESSED, event -> {
		if (event.getCode() == KeyCode.ENTER) {
		    stage.hide();
		}
	    });
	    stage.setScene(scene);
	    stage.setResizable(false);
	    stage.showAndWait();
	} catch (IOException e) {
	    e.printStackTrace();
	    showAlertMessage(AlertType.ERROR, e.getMessage());
	    return false;
	}
	double width = root.getLayoutBounds().getWidth();
	double height = root.getBoundsInLocal().getHeight();
	double scaleFactor = printableWidth / width;
	double translateY = 0d;
	double numberOfPages = Math.ceil(height * scaleFactor / printableHeight);
	double slicePoint = 0d;

	// System.err.println("scaleX: " + scaleFactor);
	// System.err.println("Printable height: " + printableHeight);
	// System.err.println("Real document hight: " + height);
	// System.err.println("Number of pages: " + numberOfPages);
	// System.err.println("Root height: " +
	// root.getBoundsInLocal().getHeight());

	Translate prinTranslate = new Translate(translateY, translateY);
	root.getTransforms().add(new Scale(scaleFactor, scaleFactor));
	root.getTransforms().add(prinTranslate);

	Node clip = root.getClip();
	List<Transform> transforms = new ArrayList<>(root.getTransforms());

	for (int i = 0; i < numberOfPages; i++) {

	    if (firstCells != null && (i + 1) < numberOfPages) {
		// System.err.println("Calculate row line to splip page... :");

		slicePoint = findSlicePoint(firstCells, printableHeight);
		// System.err.println("Slice point: " + slicePoint);
		// System.err.println("TranslateY: " + translateY);
		root.setClip(new Rectangle(0d, 0d, width, (slicePoint - translateY) / scaleFactor));

		//
		// for (int j = 0; j < firstCells.size(); j++) {
		// HBox hBox = firstCells.get(j);
		// System.err.println("Row " + j + " : " +
		// hBox.localToScene(hBox.getBoundsInLocal()).getMinY());
		// }
	    }
	    translateY -= slicePoint;

	    job.printPage(pageLayout, root);

	    root.getTransforms().clear();
	    root.getTransforms().addAll(transforms);
	    root.setClip(clip);
	    prinTranslate.setY(translateY / scaleFactor);
	}
	return job.endJob();
    }

    private double findSlicePoint(ArrayList<HBox> firstCells, final double printableHeight) {
	double slicePoint = 0d;
	for (int i = 0; i < firstCells.size(); i++) {
	    HBox hBox = firstCells.get(i);
	    double minY = hBox.localToScene(hBox.getBoundsInLocal()).getMinY();
	    if (minY >= printableHeight) {
		if (i > 0) {
		    hBox = firstCells.get(i - 1);
		    slicePoint = hBox.localToScene(hBox.getBoundsInLocal()).getMinY();
		} else {
		    slicePoint = minY;
		}
		break;
	    }
	}
	return slicePoint != 0d ? slicePoint
		: firstCells.get(firstCells.size() - 1)
			.localToScene(firstCells.get(firstCells.size() - 1).getBoundsInLocal()).getMinY();
    }

    private void showAlertMessage(AlertType type, String message) {
	Alert alert = new Alert(type);
	alert.setContentText(message);
	alert.showAndWait();
    }

}
