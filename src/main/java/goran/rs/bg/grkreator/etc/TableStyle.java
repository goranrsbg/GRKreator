package goran.rs.bg.grkreator.etc;

public enum TableStyle {
	
	HEADER_FIRST("header-first"),
	HEADER_MID("header-mid"),
	HEADER_LAST("header-last"),
	MID_FIRST("mid-first"),
	MID_MID("mid-mid"),
	MID_LAST("mid-last"),
	BOT_FIRST("bot-first"),
	BOT_MID("bot-mid"),
	BOT_LAST("bot-last"),
	;
	
	public final String VALUE;
	
	private TableStyle(String value) {
		this.VALUE = value;
	}
	
}
