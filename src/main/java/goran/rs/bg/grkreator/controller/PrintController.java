package goran.rs.bg.grkreator.controller;

import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import goran.rs.bg.grkreator.etc.FirmDetails;
import goran.rs.bg.grkreator.etc.TableStyle;
import goran.rs.bg.grkreator.itemtable.ItemTable;
import goran.rs.bg.grkreator.model.Document;
import goran.rs.bg.grkreator.model.Firm;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

public class PrintController implements Initializable {

	@FXML
	private ImageView logoImageView;

	@FXML
	private VBox selllerBoxDetails;

	@FXML
	private HBox sellerNameBox;

	@FXML
	private Text sellerName;

	@FXML
	private HBox sellerAdressBox;

	@FXML
	private Text sellerAddress;

	@FXML
	private HBox sellerEmailBox;

	@FXML
	private Text sellerEmail;

	@FXML
	private HBox sellerWebAddressBox;

	@FXML
	private Text sellerWebAddress;

	@FXML
	private HBox sellerPhoneBox;

	@FXML
	private Text sellerTelNameText;

	@FXML
	private Text sellerPhone;

	@FXML
	private HBox sellerFaxBox;

	@FXML
	private Text sellerFax;

	@FXML
	private HBox sellerMobBox;

	@FXML
	private Text sellerMob;

	@FXML
	private HBox sellerPIBbox;

	@FXML
	private Text sellerIDNumber;

	@FXML
	private Text sellerChechingAccount;

	@FXML
	private VBox buyerBoxDetails;

	@FXML
	private HBox buyerNameBox;

	@FXML
	private Text buyerName;

	@FXML
	private HBox buyerAddressBox;

	@FXML
	private Text buyerAddress;

	@FXML
	private HBox buyerPhoneBox;

	@FXML
	private Text buyerTelNameText;

	@FXML
	private Text buyerPhone;

	@FXML
	private HBox buyerMobBox;

	@FXML
	private Text buyerMob;

	@FXML
	private HBox buyerPIBbox;

	@FXML
	private Text buyerIDNumber;

	@FXML
	private Text buyerChechingAccount;

	@FXML
	private Text documentTitle;

	@FXML
	private Text ducumentNumber;

	@FXML
	private Text sellingPlaceDate;

	@FXML
	private Text recivingDate;

	@FXML
	private Text lastPayDay;

	@FXML
	private GridPane gridTable;

	@FXML
	private Label words;

	@FXML
	private Text totalSemi;

	@FXML
	private Text totalPdv;

	@FXML
	private Text total;
	
	private ArrayList<HBox> rowFirstCells;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		rowFirstCells = new ArrayList<HBox>();
		// GridPane header
		String lineSeparator = System.lineSeparator();
		HBox firstBox = createCell("Rb.", Pos.CENTER, TextAlignment.CENTER, TableStyle.HEADER_FIRST);
		rowFirstCells.add(firstBox);
		gridTable.add(firstBox, 0, 0);
		gridTable.add(createCell("Naziv" + lineSeparator + "dobra/usluge", Pos.CENTER, TextAlignment.CENTER,
				TableStyle.HEADER_MID), 1, 0);
		gridTable.add(createCell("Jm.", Pos.CENTER, TextAlignment.CENTER, TableStyle.HEADER_MID), 2, 0);
		gridTable.add(
				createCell("Koli-" + lineSeparator + "čina", Pos.CENTER, TextAlignment.CENTER, TableStyle.HEADER_MID),
				3, 0);
		gridTable.add(createCell("Cena", Pos.CENTER, TextAlignment.CENTER, TableStyle.HEADER_MID), 4, 0);
		gridTable.add(createCell("Poreska" + lineSeparator + "osnovica", Pos.CENTER, TextAlignment.CENTER,
				TableStyle.HEADER_MID), 5, 0);
		gridTable.add(createCell("PDV", Pos.CENTER, TextAlignment.CENTER, TableStyle.HEADER_MID), 6, 0);
		gridTable.add(
				createCell("Iznos" + lineSeparator + "PDV", Pos.CENTER, TextAlignment.CENTER, TableStyle.HEADER_MID), 7,
				0);
		gridTable.add(createCell("Ukupna" + lineSeparator + "naknada", Pos.CENTER, TextAlignment.CENTER,
				TableStyle.HEADER_LAST), 8, 0);
	}

	private HBox createCell(String text, Pos pos, TextAlignment textAlignment, TableStyle borderStyle) {
		HBox box = new HBox();
		Text textField = new Text(text);
		textField.setTextAlignment(textAlignment);
		box.getChildren().add(textField);
		box.setAlignment(pos);
		box.getStyleClass().add(borderStyle.VALUE);
		return box;
	}

	public void setSellerFirm(Firm sellerFirm) {
		if (sellerFirm.isLogoExistent()) {
			logoImageView.setImage(sellerFirm.getLogoImage());
		}
		if (sellerFirm.IsDataForDisplay(FirmDetails.NAME)) {
			sellerName.setText(sellerFirm.getName());
		}
		if (sellerFirm.IsDataForDisplay(FirmDetails.STREET)) {
			sellerAddress.setText(sellerFirm.getStreet());
		}
		if (sellerFirm.IsDataForDisplay(FirmDetails.SETTLEMENT)) {
			sellerAddress.setText(sellerAddress.getText() + ", " + sellerFirm.getSettlement());
		}
		if (sellerFirm.IsDataForDisplay(FirmDetails.EMAIL)) {
			sellerEmail.setText(sellerFirm.getMail());
		} else {
			selllerBoxDetails.getChildren().remove(sellerEmailBox);
		}
		if (sellerFirm.IsDataForDisplay(FirmDetails.WEB_ADDRESS)) {
			sellerWebAddress.setText(sellerFirm.getSite());
		} else {
			selllerBoxDetails.getChildren().remove(sellerWebAddressBox);
		}
		if (sellerFirm.IsDataForDisplay(FirmDetails.PHONE_HOME)) {
			sellerPhone.setText(sellerFirm.getPhoneHome());
		} else {
			selllerBoxDetails.getChildren().remove(sellerPhoneBox);
		}
		if (sellerFirm.IsDataForDisplay(FirmDetails.PHONE_FAX)) {
			if (sellerPhone.getText().equals(sellerFirm.getFax())) {
				sellerTelNameText.setText("Tel/Fax");
				selllerBoxDetails.getChildren().remove(sellerFaxBox);
			} else {
				sellerFax.setText(sellerFirm.getFax());
			}
		} else {
			selllerBoxDetails.getChildren().remove(sellerFaxBox);
		}
		if (sellerFirm.IsDataForDisplay(FirmDetails.PHONE_MOB)) {
			sellerMob.setText(sellerFirm.getPhoneMob());
		} else {
			selllerBoxDetails.getChildren().remove(sellerMobBox);
		}
		if (sellerFirm.IsDataForDisplay(FirmDetails.PIB)) {
			ObservableList<Node> children = sellerPIBbox.getChildren();
			for (int i = 1; i < children.size(); i++) {
				((Text) ((HBox) (children.get(i))).getChildren().get(0))
						.setText(sellerFirm.getPib().charAt(i - 1) + "");
			}
		}
		if (sellerFirm.IsDataForDisplay(FirmDetails.MBR)) {
			sellerIDNumber.setText(sellerFirm.getIdentificationNumber());
		}
		if (sellerFirm.IsDataForDisplay(FirmDetails.CAC)) {
			sellerChechingAccount.setText(sellerFirm.getCheckingAccount());
		}
	}

	public void setBuyerFirm(Firm buyerFirm) {
		if (buyerFirm == null) {
			return;
		}
		if (buyerFirm.IsDataForDisplay(FirmDetails.NAME)) {
			buyerName.setText(buyerFirm.getName());
		}
		if (buyerFirm.IsDataForDisplay(FirmDetails.STREET)) {
			buyerAddress.setText(buyerFirm.getStreet());
		}
		if (buyerFirm.IsDataForDisplay(FirmDetails.SETTLEMENT)) {
			buyerAddress.setText(buyerAddress.getText() + ", " + buyerFirm.getSettlement());
		}
		if (buyerFirm.IsDataForDisplay(FirmDetails.PHONE_HOME)) {
			buyerPhone.setText(buyerFirm.getPhoneHome());
		} else {
			buyerBoxDetails.getChildren().remove(buyerPhoneBox);
		}
		if (buyerFirm.IsDataForDisplay(FirmDetails.PHONE_MOB)) {
			buyerMob.setText(buyerFirm.getPhoneMob());
		} else {
			buyerBoxDetails.getChildren().remove(buyerMobBox);
		}
		if (buyerFirm.IsDataForDisplay(FirmDetails.PIB)) {
			ObservableList<Node> children = buyerPIBbox.getChildren();
			for (int i = 1; i < children.size(); i++) {
				((Text) ((HBox) children.get(i)).getChildren().get(0)).setText(buyerFirm.getPib().charAt(i - 1) + "");
			}
		}
		if (buyerFirm.IsDataForDisplay(FirmDetails.MBR)) {
			buyerIDNumber.setText(buyerFirm.getIdentificationNumber());
		}
		if (buyerFirm.IsDataForDisplay(FirmDetails.CAC)) {
			buyerChechingAccount.setText(buyerFirm.getCheckingAccount());
		}
	}

	public void setDocumentDetails(Document document) {
		documentTitle.setText(document.getTitle());
		ducumentNumber.setText(String.format("%04d/%d", document.getValue(), LocalDate.now().getYear() % 100));
		sellingPlaceDate.setText(document.getSettlement());
	}

	public void setDates(String sellingDate, String recivingDate, String lasPayDay) {
		sellingPlaceDate.setText(sellingPlaceDate.getText() + ", " + sellingDate);
		this.recivingDate.setText(recivingDate);
		this.lastPayDay.setText(lasPayDay);
	}

	public void setTableItems(List<ItemTable> items) {
		for (int i = 0; i < items.size() - 1; i++) {
			ItemTable item = items.get(i);
			if (i < items.size() - 2) {
//				System.err.println("MID ROW");
				HBox firstBox = createCell(item.getRowNo(), Pos.CENTER, TextAlignment.CENTER, TableStyle.MID_FIRST);
				rowFirstCells.add(firstBox);
				gridTable.addRow(i + 1,
						firstBox,
						createCell(item.getName(), Pos.CENTER_LEFT, TextAlignment.LEFT, TableStyle.MID_MID),
						createCell(item.getUnitOfMeasure(), Pos.CENTER, TextAlignment.CENTER, TableStyle.MID_MID),
						createCell(item.getQuantityString(), Pos.CENTER, TextAlignment.RIGHT, TableStyle.MID_MID),
						createCell(String.format("%,.2f din.", item.getRealPrice()), Pos.CENTER_RIGHT, TextAlignment.RIGHT,
								TableStyle.MID_MID),
						createCell(String.format("%,.2f din.", item.getSemiPrice()), Pos.CENTER_RIGHT,
								TextAlignment.RIGHT, TableStyle.MID_MID),
						createCell(String.format("%d%%", item.getPdv()), Pos.CENTER, TextAlignment.RIGHT,
								TableStyle.MID_MID),
						createCell(String.format("%,.2f din.", item.getPdvPrice()), Pos.CENTER_RIGHT,
								TextAlignment.RIGHT, TableStyle.MID_MID),
						createCell(String.format("%,.2f din.", item.getTotalPrice()), Pos.CENTER_RIGHT,
								TextAlignment.RIGHT, TableStyle.MID_LAST));
			} else {
//				System.err.println("BOT ROW");
				HBox firstBox = createCell(item.getRowNo(), Pos.CENTER, TextAlignment.CENTER, TableStyle.BOT_FIRST);
				rowFirstCells.add(firstBox);
				gridTable.addRow(i + 1,
						firstBox,
						createCell(item.getName(), Pos.CENTER_LEFT, TextAlignment.LEFT, TableStyle.BOT_MID),
						createCell(item.getUnitOfMeasure(), Pos.CENTER, TextAlignment.CENTER, TableStyle.BOT_MID),
						createCell(item.getQuantityString(), Pos.CENTER, TextAlignment.RIGHT, TableStyle.BOT_MID),
						createCell(String.format("%,.2f din.", item.getRealPrice()), Pos.CENTER_RIGHT, TextAlignment.RIGHT,
								TableStyle.BOT_MID),
						createCell(String.format("%,.2f din.", item.getSemiPrice()), Pos.CENTER_RIGHT,
								TextAlignment.RIGHT, TableStyle.BOT_MID),
						createCell(String.format("%d%%", item.getPdv()), Pos.CENTER, TextAlignment.RIGHT,
								TableStyle.BOT_MID),
						createCell(String.format("%,.2f din.", item.getPdvPrice()), Pos.CENTER_RIGHT,
								TextAlignment.RIGHT, TableStyle.BOT_MID),
						createCell(String.format("%,.2f din.", item.getTotalPrice()), Pos.CENTER_RIGHT,
								TextAlignment.RIGHT, TableStyle.BOT_LAST));
			}
		}
	}

	public void setTotalPrices(String semiPrice, String pdvPrice, String totalPrice, String words) {
		this.words.setText(words + ".");
		totalSemi.setText(semiPrice);
		totalPdv.setText(pdvPrice);
		total.setText(totalPrice);
	}
	
	public ArrayList<HBox> getFirstCells() {
		return rowFirstCells;
	}

}
