package goran.rs.bg.grkreator.model;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

import javax.imageio.ImageIO;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Lob;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;

@Entity
@NamedQueries({ 
	@NamedQuery(name = "Firm.findAll", query = "select f from Firm f"),
	@NamedQuery(name = "Firm.findByName", query = "select f from Firm f where FIRM_NAME like :name and FIRM_ID <> 1"),
	@NamedQuery(name = "Firm.findByPib", query = "select f from Firm f where FIRM_PIB like :pib and FIRM_ID <> 1"),
	@NamedQuery(name = "Firm.findByIdnu", query = "select f from Firm f where FIRM_IDNU like :idnu and FIRM_ID <> 1")})
@Table(name = "T_FIRM", indexes = { 
		@Index(name = "ind_pib", columnList = "FIRM_PIB", unique = true),
		@Index(name = "ind_name", columnList = "FIRM_NAME", unique = false)})
public class Firm {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "firm_generator")
	@SequenceGenerator(name = "firm_generator", initialValue = 1, allocationSize = 1)
	@Column(name = "FIRM_ID")
	private Integer id;

	@Column(name = "FIRM_NAME", length = 64)
	@NotNull
	private String name;

	@Column(name = "FIRM_STREET", length = 64)
	private String street;

	@Column(name = "FIRM_SETTLEMENT", length = 64)
	@NotNull
	private String settlement;

	@Column(name = "FIRM_PIB", length = 16)
	@NotNull
	private String pib;

	@Column(name = "FIRM_IDNU", length = 16)
	private String identificationNumber;

	@Column(name = "FIRM_CA", length = 32)
	private String checkingAccount;

	@Column(name = "FIRM_PHONE_HOME", length = 32)
	private String phoneHome;

	@Column(name = "FIRM_FAX", length = 32)
	private String fax;

	@Column(name = "FIRM_PHONE_MOB", length = 32)
	private String phoneMob;

	@Column(name = "FIRM_MAIL", length = 32)
	private String mail;

	@Column(name = "FIRM_WEBSITE", length = 32)
	private String site;

	@Lob
	@Basic(fetch = FetchType.EAGER)
	@Column(name = "FIRM_LOGO", length = 500000)
	private byte[] logo;

	@Column(name = "FIRM_DISPLAY_DATA", length = 12)
	@Size(min = 12, max = 12, message = "Veličina mora da bude 12, jer je toliko podataka na raspolaganju.")
	@NotNull
	private String displayData;

	public Firm() {
		name = "bez imena";
		settlement = "bez naselja";
		pib = "bez PIB-a";
		displayData = "101100000000";
	}

	public Firm(int id, @NotNull String name, String street, @NotNull String settlement, @NotNull String pib,
			String identificationNumber, String checkingAccount, String phoneHome, String fax, String phoneMob,
			String mail, String site, byte[] logo,
			@Size(min = 12, max = 12, message = "Veličina mora da bude 12, jer je toliko podataka na raspolaganju.") @NotNull String displayData) {
		this.id = id;
		this.name = name;
		this.street = street;
		this.settlement = settlement;
		this.pib = pib;
		this.identificationNumber = identificationNumber;
		this.checkingAccount = checkingAccount;
		this.phoneHome = phoneHome;
		this.fax = fax;
		this.phoneMob = phoneMob;
		this.mail = mail;
		this.site = site;
		this.logo = logo;
		this.displayData = displayData;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	public String getSettlement() {
		return settlement;
	}

	public void setSettlement(String settlement) {
		this.settlement = settlement;
	}

	public String getPib() {
		return pib;
	}

	public void setPib(String pib) {
		this.pib = pib;
	}

	public String getIdentificationNumber() {
		return identificationNumber;
	}

	public void setIdentificationNumber(String identificationNumber) {
		this.identificationNumber = identificationNumber;
	}

	public String getCheckingAccount() {
		return checkingAccount;
	}

	public void setCheckingAccount(String checkingAccount) {
		this.checkingAccount = checkingAccount;
	}

	public String getPhoneHome() {
		return phoneHome;
	}

	public void setPhoneHome(String phoneHome) {
		this.phoneHome = phoneHome;
	}

	public String getFax() {
		return fax;
	}

	public void setFax(String fax) {
		this.fax = fax;
	}

	public String getPhoneMob() {
		return phoneMob;
	}

	public void setPhoneMob(String phoneMob) {
		this.phoneMob = phoneMob;
	}

	public String getMail() {
		return mail;
	}

	public void setMail(String mail) {
		this.mail = mail;
	}

	public String getSite() {
		return site;
	}

	public void setSite(String site) {
		this.site = site;
	}

	public byte[] getLogo() {
		return logo;
	}

	public void setLogo(byte[] logo) {
		this.logo = logo;
	}

	public String getDisplayData() {
		return displayData;
	}

	public void setDisplayData(String displayData) {
		this.displayData = displayData;
	}
	
	public boolean isFirmValid() {
		return !pib.equals("bez PIB-a");
	}

	public Image getLogoImage() {
		Image image = null;
		if (isLogoExistent()) {
			try {
				InputStream in = new ByteArrayInputStream(logo);
				BufferedImage bi = ImageIO.read(in);
				if (bi != null) {
					image = SwingFXUtils.toFXImage(bi, null);
				}
				in.close();
			} catch (IOException e) {
				System.err.println(e.getMessage());
			}
		}
		return image;
	}

	public void setLogoImage(Image image) {
		if (image == null) {
			setLogo(null);
		} else {
			BufferedImage bi = SwingFXUtils.fromFXImage(image, null);
			ByteArrayOutputStream bs = new ByteArrayOutputStream();
			try {
				ImageIO.write(bi, "png", bs);
				setLogo(bs.toByteArray());
				bs.close();
			} catch (IOException e) {
				System.err.println(e.toString());
				setLogo(null);
			}
		}
	}

	public boolean isLogoExistent() {
		return logo != null;
	}

	public String getDataString(int key) {
		switch (key) {
		case 0:
			return name;
		case 1:
			return street;
		case 2:
			return settlement;
		case 3:
			return pib;
		case 4:
			return identificationNumber;
		case 5:
			return checkingAccount;
		case 6:
			return phoneHome;
		case 7:
			return fax;
		case 8:
			return phoneMob;
		case 9:
			return mail;
		case 10:
			return site;
		default:
			System.err.println("Unknown key: " + key);
			return null;
		}
	}

	public String getDataNameString(int key) {
		switch (key) {
		case 0:
			return "Naziv";
		case 1:
			return "Ulica";
		case 2:
			return "Naselje";
		case 3:
			return "PIB";
		case 4:
			return "Matični broj";
		case 5:
			return "Tekući račun";
		case 6:
			return "Fiksni telefon";
		case 7:
			return "Fax";
		case 8:
			return "Moblilni telefon";
		case 9:
			return "Mail";
		case 10:
			return "Website";
		default:
			System.err.println("Unknown data name key: " + key + " " + Firm.class.getName());
			return null;
		}
	}
	
	
	
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((checkingAccount == null) ? 0 : checkingAccount.hashCode());
		result = prime * result + ((displayData == null) ? 0 : displayData.hashCode());
		result = prime * result + ((fax == null) ? 0 : fax.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((identificationNumber == null) ? 0 : identificationNumber.hashCode());
		result = prime * result + Arrays.hashCode(logo);
		result = prime * result + ((mail == null) ? 0 : mail.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((phoneHome == null) ? 0 : phoneHome.hashCode());
		result = prime * result + ((phoneMob == null) ? 0 : phoneMob.hashCode());
		result = prime * result + ((pib == null) ? 0 : pib.hashCode());
		result = prime * result + ((settlement == null) ? 0 : settlement.hashCode());
		result = prime * result + ((site == null) ? 0 : site.hashCode());
		result = prime * result + ((street == null) ? 0 : street.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Firm other = (Firm) obj;
		if (checkingAccount == null) {
			if (other.checkingAccount != null)
				return false;
		} else if (!checkingAccount.equals(other.checkingAccount))
			return false;
		if (displayData == null) {
			if (other.displayData != null)
				return false;
		} else if (!displayData.equals(other.displayData))
			return false;
		if (fax == null) {
			if (other.fax != null)
				return false;
		} else if (!fax.equals(other.fax))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (identificationNumber == null) {
			if (other.identificationNumber != null)
				return false;
		} else if (!identificationNumber.equals(other.identificationNumber))
			return false;
		if (!Arrays.equals(logo, other.logo))
			return false;
		if (mail == null) {
			if (other.mail != null)
				return false;
		} else if (!mail.equals(other.mail))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (phoneHome == null) {
			if (other.phoneHome != null)
				return false;
		} else if (!phoneHome.equals(other.phoneHome))
			return false;
		if (phoneMob == null) {
			if (other.phoneMob != null)
				return false;
		} else if (!phoneMob.equals(other.phoneMob))
			return false;
		if (pib == null) {
			if (other.pib != null)
				return false;
		} else if (!pib.equals(other.pib))
			return false;
		if (settlement == null) {
			if (other.settlement != null)
				return false;
		} else if (!settlement.equals(other.settlement))
			return false;
		if (site == null) {
			if (other.site != null)
				return false;
		} else if (!site.equals(other.site))
			return false;
		if (street == null) {
			if (other.street != null)
				return false;
		} else if (!street.equals(other.street))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Firm [id=" + id + ", name=" + name + ", street=" + street + ", settlement=" + settlement + ", pib="
				+ pib + ", identificationNumber=" + identificationNumber + ", checkingAccount=" + checkingAccount
				+ ", phoneHome=" + phoneHome + ", fax=" + fax + ", phoneMob=" + phoneMob + ", mail=" + mail + ", site="
				+ site + ", logo=" + logo + ", displayData=" + displayData + "]";
	}
	
}
