package goran.rs.bg.grkreator.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "T_DOCUMENT_NUMBER")
public class Document {

    @Id
    @Column(name = "DOCUMENT_ID")
    private Integer id;

    @Column(name = "DOCUMENT_NUMBER_VALUE", nullable = false)
    private Integer value;

    @Column(name = "DOCUMENT_TITLE", nullable = false)
    private String title;

    @Column(name = "DOCUMENT_SETTLEMENT", nullable = false)
    private String settlement;

    @Column(name = "DOCUMENT_IN_PDV_SYSTEM", nullable = false)
    private Boolean inPdv;

    public Document() {

    }

    public Document(Integer id, Integer value, String title, String settlement, Boolean inPdv) {
	this.id = id;
	this.value = value;
	this.title = title;
	this.settlement = settlement;
	this.inPdv = inPdv;
    }

    public void setInPdv(boolean value) {
	inPdv = value;
    }

    public Boolean isInPdvSystem() {
	return inPdv;
    }

    public Integer getId() {
	return id;
    }

    public void setId(Integer id) {
	this.id = id;
    }

    public Integer getValue() {
	return value;
    }

    public void setValue(Integer value) {
	this.value = value;
    }

    public String getTitle() {
	return title;
    }

    public void setTitle(String title) {
	this.title = title;
    }

    public String getSettlement() {
	return settlement;
    }

    public void setSettlement(String settlement) {
	this.settlement = settlement;
    }

    @Override
    public int hashCode() {
	final int prime = 31;
	int result = 1;
	result = prime * result + ((id == null) ? 0 : id.hashCode());
	result = prime * result + ((settlement == null) ? 0 : settlement.hashCode());
	result = prime * result + ((title == null) ? 0 : title.hashCode());
	result = prime * result + ((value == null) ? 0 : value.hashCode());
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
	Document other = (Document) obj;
	if (id == null) {
	    if (other.id != null)
		return false;
	} else if (!id.equals(other.id))
	    return false;
	if (settlement == null) {
	    if (other.settlement != null)
		return false;
	} else if (!settlement.equals(other.settlement))
	    return false;
	if (title == null) {
	    if (other.title != null)
		return false;
	} else if (!title.equals(other.title))
	    return false;
	if (value == null) {
	    if (other.value != null)
		return false;
	} else if (!value.equals(other.value))
	    return false;
	return true;
    }

    @Override
    public String toString() {
	return "DocumentDetails [id=" + id + ", value=" + value + ", title=" + title + ", settlement=" + settlement
		+ ", inPdv=" + inPdv + "]";
    }

}
