package goran.rs.bg.grkreator.etc;

public enum FirmDetails {
	NAME(0),
	STREET(1),
	SETTLEMENT(2),
	PIB(3),
	MBR(4),
	CAC(5),
	PHONE_HOME(6),
	PHONE_FAX(7),
	PHONE_MOB(8),
	EMAIL(9),
	WEB_ADDRESS(10);
	public final int ID;
	private FirmDetails(int id) {
		this.ID = id;
	}
	
}
