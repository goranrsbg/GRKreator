package goran.rs.bg.grkreator.controller;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;
import javax.persistence.TypedQuery;

import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextField;

import goran.rs.bg.grkreator.DB;
import goran.rs.bg.grkreator.format.FirmListFormatCell;
import goran.rs.bg.grkreator.itemtable.ItemTable;
import goran.rs.bg.grkreator.model.Document;
import goran.rs.bg.grkreator.model.Firm;
import goran.rs.bg.grkreator.model.Item;
import goran.rs.bg.grkreator.thread.SearchFirmsService;
import javafx.application.Platform;
import javafx.beans.binding.StringBinding;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
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
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
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
import javafx.scene.text.Text;
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
	private IntegerProperty searchTypeIndex = new SimpleIntegerProperty();;
	private SearchFirmsService searchFirmsService;

	private Firm sellerFirm;
	private Firm buyerFirm;

	private final String firmFxml = "/fxml/firm.fxml";
	private final String documentFxml = "/fxml/document.fxml";
	private final String itemFxml = "/fxml/item.fxml";
	private final String documentTitle = "Detalji računa";
	private final String itemTitle = "Detalji Robe";

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
		displayDocumentDetails(findDocumenDetails());
		showFirmData(sellerFirmLogoImageView, sellerFirmDataGridPane, sellerFirm);
		DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy", new Locale("sr"));
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
		Platform.runLater(() -> {
			sellingDatePicker.setValue(LocalDate.now());
			recivingDatePicker.setValue(LocalDate.now());
		});
		searchListItems = FXCollections.observableArrayList();
		searchItemListView.setItems(searchListItems);
		searchItemListView.focusedProperty().addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				if(!newValue) {
					searchItemListView.setVisible(false);
				}
			}
		});
		initTable();
	}

	private void initTable() {
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
						return param.getValue().priceProperty().asString("%,.2f din.");
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
								if(quantityProperty.get() == Math.floor(quantityProperty.get())) {
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
		tableAddNewEmptyRow();
		searchItemListView.setCellFactory(new Callback<ListView<Item>, ListCell<Item>>() {
			@Override
			public ListCell<Item> call(ListView<Item> param) {
				ListCell<Item> cell = new ListCell<Item>() {
					@Override
					protected void updateItem(Item item, boolean empty) {
						super.updateItem(item, empty);
						if (item != null) {
							setText(String.format("%s (%.2f din.)[%d%%]",item.getName(), item.getPrice(), item.getPdv()));
						} else {
							setText("");
						}
					}
				};
				return cell;
			}
		});
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
		Document document = findDocumenDetails();
		showDetailsDialog(documentTitle, documentFxml, document);
		displayDocumentDetails(document);
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
			showDetailsDialog(buyerLabel.getText(),firmFxml, buyerFirm);
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
				Item item = selectedItem.getItem();
				item.setName(event.getNewValue());
				showDetailsDialog(itemTitle, itemFxml, item);
				selectedItem.refreshItem();
				if (item.isValid()) {
					updateRowNoValues();
					editQuantity(row);
				}
			} else if (foundItems.size() == 1) {
				selectedItem.setItem(foundItems.get(0));
				updateRowNoValues();
				editQuantity(row);
			} else {
				for(Item i : foundItems) {
					System.err.println(i);
				}
				this.selectedItem = selectedItem;
				this.row = row;
				searchListItems.addAll(foundItems);
				
				double topAnchorTable = itemsTableView.localToScene(itemsTableView.getBoundsInLocal()).getMinY();
				double leftMargin = rbCol.getWidth() + nameCol.getWidth();
				double topMargin = topAnchorTable + (row + 1) * 26d - 27.5;
				AnchorPane.setTopAnchor(searchItemListView, topMargin);
				AnchorPane.setLeftAnchor(searchItemListView, leftMargin);
				searchItemListView.setVisible(true);
				
				Platform.runLater(()-> {
					searchItemListView.requestFocus();
				});
			}
		} else {
			itemsTableView.refresh();
		}
		event.consume();
	}

	@FXML
	void onQuantityEditCommit(TableColumn.CellEditEvent<ItemTable, String> event) {
		try {
			double value = Double.parseDouble(event.getNewValue());
			ItemTable selectedItem = event.getRowValue();
			selectedItem.setQuantity(value);
			ItemTable lastItemTable = tableItems.get(tableItems.size() - 1);
			if (value > 0d && lastItemTable.getItem().isValid() && lastItemTable.getQuantity() > 0d) {
				tableAddNewEmptyRow();
			}
		} catch (Exception e) {
			itemsTableView.refresh();
		}
	}

	@FXML
	void onSearchItemListViewKeyPressed(KeyEvent event) {
		if (event.getCode() == KeyCode.ESCAPE) {
			searchItemListView.setVisible(false);
		} else if(event.getCode() == KeyCode.ENTER) {
			selectedItem.setItem(searchItemListView.getSelectionModel().getSelectedItem());
			goEditNextRow();
		}
	}

	@FXML
	void onSearchItemListViewMouseClicked(MouseEvent event) {
		if (event.getClickCount() == 2) {
			selectedItem.setItem(searchItemListView.getSelectionModel().getSelectedItem());
			goEditNextRow();
		}
	}
	
	@FXML
    void onAddOrUpdateItemButtonAction(ActionEvent event) {
		ItemTable itemTable = itemsTableView.getSelectionModel().getSelectedItem();
		Item item = null;
		if(itemTable == null) {
			item = tableItems.get(tableItems.size() - 1).getItem();
		} else {
			item = itemTable.getItem();
		}
		showDetailsDialog(itemTitle, itemFxml, item);
		if(itemTable != null) {
			itemTable.refreshItem();
		}
    }
	
	@FXML
    void onRemoveRowButtonAction(ActionEvent event) {
		ItemTable itemTable = itemsTableView.getSelectionModel().getSelectedItem();
		if(itemTable != null && itemTable.getItem().isValid() && tableItems.size() > 1) {
			tableItems.remove(itemTable);
			updateRowNoValues();
		}
	}
	
	private void goEditNextRow() {
		updateRowNoValues();
		editQuantity(row);
		searchItemListHide();
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
		nQuery.setParameter("name", "%" + value + "%");
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
		tableItems.add(new ItemTable(new Item("", "kom", 0d, 0)));
		editName(tableItems.size() - 1);
	}

	private Document findDocumenDetails() {
		Document document = null;
		try {
			em.getTransaction().begin();
			document = em.find(Document.class, DOCUMENT_DETAILS_ID);
			if (document == null) {
				document = new Document(DOCUMENT_DETAILS_ID, 1, "Račun", "Bez naselja");
				em.persist(document);
			}
			em.getTransaction().commit();
		} catch (PersistenceException e) {
			em.getTransaction().rollback();
		}
		return document;
	}

	private void displayDocumentDetails(Document document) {
		labelAccoundNumber
				.setText(String.format("Rb: %05d/%d", document.getValue(), LocalDate.now().getYear() % 100));
		labelTitle.setText(document.getTitle());
		labelSettlement.setText(document.getSettlement());
	}
	
	private void updateRowNoValues() {
		for (int i = 0; i < tableItems.size(); i++) {
			tableItems.get(i).setRowNo(i+1);
		}
	}
	
	private void searchItemListHide() {
		selectedItem = null;
		searchItemListView.setVisible(false);
		searchListItems.clear();
	}

	private void showAlertMessage(AlertType type, String message) {
		Alert alert = new Alert(type);
		alert.setContentText(message);
		alert.showAndWait();
	}

}
