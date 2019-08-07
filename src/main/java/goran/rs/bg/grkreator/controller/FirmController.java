package goran.rs.bg.grkreator.controller;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.validation.RequiredFieldValidator;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import goran.rs.bg.grkreator.etc.PutItem;
import goran.rs.bg.grkreator.model.Firm;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Window;

public class FirmController implements Initializable, PutItem {

	@FXML
	private VBox sceneElementsVBox;

	@FXML
	private JFXTextField nameField;

	@FXML
	private JFXCheckBox nameCheckBox;

	@FXML
	private JFXTextField streetField;

	@FXML
	private JFXCheckBox streetCheckBox;

	@FXML
	private JFXTextField settlementField;

	@FXML
	private JFXCheckBox settlementCheckBox;

	@FXML
	private JFXTextField pibField;

	@FXML
	private JFXCheckBox pibCheckBox;

	@FXML
	private JFXTextField identificationNumberField;

	@FXML
	private JFXCheckBox identificationNumberCheckBox;

	@FXML
	private JFXTextField checkingAccountField;

	@FXML
	private JFXCheckBox checkingAccountCheckBox;

	@FXML
	private JFXTextField mailField;

	@FXML
	private JFXCheckBox mailCheckBox;

	@FXML
	private JFXTextField phoneHomeField;

	@FXML
	private JFXCheckBox phoneHomeCheckBox;

	@FXML
	private JFXTextField faxField;

	@FXML
	private JFXCheckBox faxCheckBox;

	@FXML
	private JFXTextField phoneMobField;

	@FXML
	private JFXCheckBox phoneMobCheckBox;

	@FXML
	private JFXTextField siteField;

	@FXML
	private JFXCheckBox siteCheckBox;

	@FXML
	private ImageView logoImageView;

	@FXML
	private JFXCheckBox logoCheckBox;

	private Image noLogoImage;
	private String imageFormat;
	private boolean isLogoChosen;
	private boolean isLogoDeleted;
	private ArrayList<JFXCheckBox> checkBoxesList;

	private Window window;
	private Firm theFirm;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		imageFormat = null;
		initCheckBoxesList();
		addValidators();
		noLogoImage = new Image(getClass().getResourceAsStream("/images/logo.png"));
		Platform.runLater(() -> {
			window = nameField.getScene().getWindow();
		});
		System.err.println(nameField.getText());
		System.err.println(settlementField.getText());
		System.err.println(pibField.getText());
	}

	private void initCheckBoxesList() {
		checkBoxesList = new ArrayList<>(12);
		checkBoxesList.add(nameCheckBox);
		checkBoxesList.add(streetCheckBox);
		checkBoxesList.add(settlementCheckBox);
		checkBoxesList.add(pibCheckBox);
		checkBoxesList.add(identificationNumberCheckBox);
		checkBoxesList.add(checkingAccountCheckBox);
		checkBoxesList.add(phoneHomeCheckBox);
		checkBoxesList.add(faxCheckBox);
		checkBoxesList.add(phoneMobCheckBox);
		checkBoxesList.add(mailCheckBox);
		checkBoxesList.add(siteCheckBox);
		checkBoxesList.add(logoCheckBox);
	}

	private void addValidators() {
		RequiredFieldValidator requiredNameValidator = new RequiredFieldValidator();
		RequiredFieldValidator requiredSettlementValidator = new RequiredFieldValidator();
		RequiredFieldValidator requiredPibValidator = new RequiredFieldValidator();
		requiredNameValidator.setIcon(new FontAwesomeIconView(FontAwesomeIcon.TIMES_CIRCLE));
		requiredSettlementValidator.setIcon(new FontAwesomeIconView(FontAwesomeIcon.TIMES_CIRCLE));
		requiredPibValidator.setIcon(new FontAwesomeIconView(FontAwesomeIcon.TIMES_CIRCLE));
		nameField.focusedProperty().addListener((o, oldVal, newVal) -> {
			if (!newVal) {
				nameField.validate();
			}
		});
		settlementField.focusedProperty().addListener((o, oldVal, newVal) -> {
			if (!newVal) {
				settlementField.validate();
			}
		});
		pibField.focusedProperty().addListener((o, oldVal, newVal) -> {
			if (!newVal) {
				pibField.validate();
			}
		});
		nameField.getValidators().add(requiredNameValidator);
		settlementField.getValidators().add(requiredSettlementValidator);
		pibField.getValidators().add(requiredPibValidator);
	}

	@FXML
	void onDeleteImageAction(ActionEvent event) {
		isLogoDeleted = true;
		setDefaultLogoImage();
	}

	@FXML
	void onOpenImageAction(ActionEvent event) {
		FileChooser fc = new FileChooser();
		fc.setTitle("Izaberi Logo");
		fc.setInitialDirectory(new File(System.getProperty("user.home")));
		fc.getExtensionFilters().addAll(
				new FileChooser.ExtensionFilter("All", "*.jpg", "*.jpeg", "*.png", "*.bmp", "*.gif"),
				new FileChooser.ExtensionFilter("JPG", "*.jpg"), new FileChooser.ExtensionFilter("JPEG", "*.jpeg"),
				new FileChooser.ExtensionFilter("PNG", "*.png"), new FileChooser.ExtensionFilter("JPEG", "*.bmp"),
				new FileChooser.ExtensionFilter("JPEG", "*.gif"));
		File logoFile = fc.showOpenDialog(window);
		if (logoFile != null) {
			Image image = new Image(logoFile.toURI().toString());
			logoImageView.setImage(image);
			imageFormat = logoFile.getName().substring(logoFile.getName().lastIndexOf('.') + 1);
			System.err.println("FORMAT: " + imageFormat);
			isLogoChosen = true;
			isLogoDeleted = false;
		}
	}

	@FXML
	void onExitButtonAction(ActionEvent event) {
		updateFirmData();
		window.hide();
	}

	public void putItem(Object item) {
		Firm firm = (Firm) item;
		Platform.runLater(() -> {
			nameField.setText(firm.getName());
			streetField.setText(firm.getStreet());
			settlementField.setText(firm.getSettlement());
			pibField.setText(firm.getPib());
			identificationNumberField.setText(firm.getIdentificationNumber());
			checkingAccountField.setText(firm.getCheckingAccount());
			phoneHomeField.setText(firm.getPhoneHome());
			faxField.setText(firm.getFax());
			phoneMobField.setText(firm.getPhoneMob());
			siteField.setText(firm.getSite());
			mailField.setText(firm.getMail());
			if (firm.isLogoExistent()) {
				logoImageView.setImage(firm.getLogoImage());
			} else {
				setDefaultLogoImage();
			}
			readShown(firm.getDisplayData());
			theFirm = firm;
		});
	}

	/**
	 * Reads and sets check boxes accordingly.
	 * 
	 * @param shown Display code.
	 */
	private void readShown(String shown) {
		if (shown != null) {
			char[] dd = shown.toCharArray();
			for (int i = 0; i < dd.length; i++) {
				if (dd[i] == MainController.SHOW) {
					checkBoxesList.get(i).setSelected(true);
				}
			}
		}
	}

	/**
	 * Generates code which contains what firm information to be displayed.
	 * Adjusting check boxes accordingly. 110100000000 default value.
	 * 
	 * @return Code of display data.
	 */
	private String writeShown() {
		StringBuilder sb = new StringBuilder(12);
		for (int i = 0; i < checkBoxesList.size(); i++) {
			if (checkBoxesList.get(i).isSelected()) {
				sb.append(MainController.SHOW);
			} else {
				sb.append(MainController.HIDE);
			}
		}
		return sb.toString();
	}

	private void setDefaultLogoImage() {
		logoImageView.setImage(noLogoImage);
		imageFormat = null;
	}

	private void updateFirmData() {
		if (isValidFields()) {
			theFirm.setName(getText(nameField));
			theFirm.setStreet(getText(streetField));
			theFirm.setSettlement(getText(settlementField));
			theFirm.setPib(getText(pibField));
			theFirm.setIdentificationNumber(getText(identificationNumberField));
			theFirm.setCheckingAccount(getText(checkingAccountField));
			theFirm.setMail(getText(mailField));
			theFirm.setPhoneMob(getText(phoneMobField));
			theFirm.setPhoneHome(getText(phoneHomeField));
			theFirm.setFax(getText(faxField));
			theFirm.setSite(getText(siteField));
			theFirm.setDisplayData(writeShown());
			if (isLogoChosen) {
				theFirm.setLogoImage(logoImageView.getImage(), imageFormat);
			} else if (isLogoDeleted) {
				theFirm.setLogoImage(null, null);
			}
		}
	}

	private boolean isValidFields() {
		String n = nameField.getText();
		String s = settlementField.getText();
		String p = pibField.getText();
		return !((n == null || n.isEmpty()) || (s == null || s.isEmpty()) || (p == null || p.isEmpty()));
	}

	private String getText(JFXTextField field) {
		String text = field.getText();
		if (text != null) {
			text = text.trim();
			if (text.isEmpty()) {
				text = null;
			}
		}
		return text;
	}

}
