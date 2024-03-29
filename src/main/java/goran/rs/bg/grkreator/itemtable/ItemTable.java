package goran.rs.bg.grkreator.itemtable;

import java.text.DecimalFormat;

import goran.rs.bg.grkreator.model.Item;
import javafx.beans.binding.DoubleBinding;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class ItemTable {

	private Item item;
	private StringProperty rowNo;
	private StringProperty name;
	private StringProperty unitOfMeasure;
	private DoubleProperty price;
	private IntegerProperty pdv;
	private DoubleProperty quantity;
	private DoubleProperty semiPrice;
	private DoubleProperty pdvPrice;
	private DoubleProperty totalPrice;

	private static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("###,###.##");

	private ItemTable() {
		rowNo = new SimpleStringProperty();
		name = new SimpleStringProperty();
		unitOfMeasure = new SimpleStringProperty();
		price = new SimpleDoubleProperty();
		pdv = new SimpleIntegerProperty();
		quantity = new SimpleDoubleProperty();
		semiPrice = new SimpleDoubleProperty();
		pdvPrice = new SimpleDoubleProperty();
		totalPrice = new SimpleDoubleProperty();
		// calculations
		semiPrice.bind(quantity.multiply(new DoubleBinding() {
			{
				super.bind(price, pdv);
			}
			@Override
			protected double computeValue() {
				return getRealPrice();
			}
		}));
		totalPrice.bind(price.multiply(quantity));
		pdvPrice.bind(totalPrice.subtract(semiPrice));
	}

	public ItemTable(Item item) {
		this();
		quantity.set(0d);
		setItem(item);
	}

	public void setItem(Item item) {
		this.item = item;
		refreshItem();
	}

	public void refreshItem() {
		name.set(item.getName());
		unitOfMeasure.set(item.getUnitOfMeasure());
		price.set(item.getPrice());
		pdv.set(item.getPdv());
	}

	public Item getItem() {
		return item;
	}

	public StringProperty rowNoProperty() {
		return rowNo;
	}

	public String getRowNo() {
		return rowNo.get();
	}

	public void setRowNo(int value) {
		rowNo.set(String.format("%02d.", value));
	}

	public StringProperty nameProperty() {
		return name;
	}

	public String getName() {
		return name.get();
	}

	public void setName(String name) {
		this.name.set(name);
	}

	public StringProperty unitOfMeasureProperty() {
		return unitOfMeasure;
	}

	public String getUnitOfMeasure() {
		return unitOfMeasure.get();
	}

	public void setUnitOfMeasure(String unitOfMeasure) {
		this.unitOfMeasure.set(unitOfMeasure);
		;
	}

	public DoubleProperty priceProperty() {
		return price;
	}

	public double getPrice() {
		return price.get();
	}

	public double getRealPrice() {
		return Double.parseDouble(String.format("%.2f", price.get()/ (1d + pdv.get() / 100d)));
	}

	public void setPrice(double price) {
		this.price.set(price);
		;
	}

	public IntegerProperty pdvProperty() {
		return pdv;
	}

	public int getPdv() {
		return pdv.get();
	}

	public void setPdv(int pdv) {
		this.pdv.set(pdv);
	}

	public DoubleProperty quantityProperty() {
		return quantity;
	}

	public double getQuantity() {
		return quantity.get();
	}

	public String getQuantityString() {
		return DECIMAL_FORMAT.format(getQuantity());
	}

	public void setQuantity(double quantity) {
		this.quantity.set(quantity);
	}

	public DoubleProperty semiPriceProperty() {
		return semiPrice;
	}

	public double getSemiPrice() {
		return semiPrice.get();
	}

	public DoubleProperty pdvPriceProperty() {
		return pdvPrice;
	}

	public double getPdvPrice() {
		return pdvPrice.get();
	}

	public DoubleProperty totalPriceProperty() {
		return totalPrice;
	}

	public double getTotalPrice() {
		return totalPrice.get();
	}
}
