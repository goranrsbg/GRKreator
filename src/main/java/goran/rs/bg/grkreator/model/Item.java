package goran.rs.bg.grkreator.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "T_ITEM")
@NamedQueries({ @NamedQuery(name = "Item.findAll", query = "select i from Item i"),
		@NamedQuery(name = "Item.findByName", query = "select i from Item i where lower(ITEM_NAME) like :name") })
public class Item {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "item_generator")
	@SequenceGenerator(name = "item_generator", initialValue = 1, allocationSize = 1)
	@Column(name = "ITEM_ID")
	private Integer id;

	@Column(name = "ITEM_NAME", nullable = false, unique = true)
	private String name;

	@Column(name = "ITEM_UNIT_OF_MEASURE", nullable = false)
	private String unitOfMeasure;

	@Column(name = "ITEM_PRICE", nullable = false)
	private Double price;

	@Column(name = "ITEM_PDV", nullable = false)
	private Integer pdv;

	public Item() {
	}

	public Item(String name, String unitOfMeasure, Double price, Integer pdv) {
		super();
		this.name = name;
		this.unitOfMeasure = unitOfMeasure;
		this.price = price;
		this.pdv = pdv;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUnitOfMeasure() {
		return unitOfMeasure;
	}

	public void setUnitOfMeasure(String unitOfMeasure) {
		this.unitOfMeasure = unitOfMeasure;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public Integer getPdv() {
		return pdv;
	}

	public void setPdv(Integer pdv) {
		this.pdv = pdv;
	}

	public Integer getId() {
		return id;
	}

	public boolean isValid() {
		return id != null;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((pdv == null) ? 0 : pdv.hashCode());
		result = prime * result + ((price == null) ? 0 : price.hashCode());
		result = prime * result + ((unitOfMeasure == null) ? 0 : unitOfMeasure.hashCode());
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
		Item other = (Item) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (pdv == null) {
			if (other.pdv != null)
				return false;
		} else if (!pdv.equals(other.pdv))
			return false;
		if (price == null) {
			if (other.price != null)
				return false;
		} else if (!price.equals(other.price))
			return false;
		if (unitOfMeasure == null) {
			if (other.unitOfMeasure != null)
				return false;
		} else if (!unitOfMeasure.equals(other.unitOfMeasure))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Item [id=" + id + ", name=" + name + ", unitOfMeasure=" + unitOfMeasure + ", price=" + price + ", pdv="
				+ pdv + "]";
	}

}
