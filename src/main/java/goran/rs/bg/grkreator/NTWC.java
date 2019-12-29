package goran.rs.bg.grkreator;

import java.util.HashMap;
import java.util.Map;

public class NTWC {

    private static NTWC instance;

    private final StringBuilder sb;
    private final String DINARA = "dinara";
    private final String DINAR = "dinar";
    private final String PARA = "para";
    private final String PARE = "pare";
    private final char ZERO = '0';
    private final char ONE = '1';
    private final char TWO = '2';
    private final char SPACE = ' ';

    private Map<Long, String> words;

    private NTWC() {
	sb = new StringBuilder(101);
	words = new HashMap<Long, String>(48);
	initWordsMap();
    }

    private void initWordsMap() {
	words.put(0L, "nula");
	words.put(1L, "jedan");
	words.put(101L, "jedna");
	words.put(2L, "dva");
	words.put(102L, "dve");
	words.put(3L, "tri");
	words.put(4L, "četri");
	words.put(5L, "pet");
	words.put(6L, "šest");
	words.put(7L, "sedam");
	words.put(8L, "osam");
	words.put(9L, "devet");
	words.put(10L, "deset");
	words.put(11L, "jedanaest");
	words.put(12L, "dvanaest");
	words.put(13L, "trinaest");
	words.put(14L, "četrnaest");
	words.put(15L, "petnaest");
	words.put(16L, "šesnaest");
	words.put(17L, "sedamanaest");
	words.put(18L, "osamnaes");
	words.put(19L, "devetnaest");
	words.put(20L, "dvadeset");
	words.put(30L, "trideset");
	words.put(40L, "četrdeset");
	words.put(50L, "pedeset");
	words.put(60L, "šezdeset");
	words.put(70L, "sedamdeset");
	words.put(80L, "osamdeset");
	words.put(90L, "devedeset");
	words.put(100L, "sto");
	words.put(200L, "dvesta");
	words.put(300L, "trista");
	words.put(400L, "četristo");
	words.put(500L, "petsto");
	words.put(600L, "šesto");
	words.put(700L, "sedamsto");
	words.put(800L, "osamsto");
	words.put(900L, "devetsto");
	words.put(1000L, "hiljada");
	words.put(1001L, "hiljada");
	words.put(1002L, "hiljade");
	words.put(1003L, "hiljadu");
	words.put(1000000L, "milion");
	words.put(1000001L, "miliona");
	words.put(1000000000L, "milijarda");
	words.put(1000000001L, "milijardi");
	words.put(1000000002L, "milijarde");
	words.put(1000000003L, "milijardu");
	words.put(1000000000000L, "bilion");
	words.put(1000000000001L, "biliona");
	words.put(1000000000000000L, "bilijarda");
	words.put(1000000000000001L, "bilijardi");
	words.put(1000000000000002L, "bilijarde");
	words.put(1000000000000003L, "bilijardu");
	words.put(1000000000000000000L, "trilion");
	words.put(1000000000000000001L, "triliona");
    }

    private String transform(final String value) {
	int digitCurrentPositon = 0;
	int digitHundredPostion = 0;
	char charAt;
	long val;
	sb.setLength(0);
	String[] split = null;
	String dinPart = null;
	String paraPart = null;
	split = value.split("\\.");
	dinPart = split[0];
	paraPart = split[1];

	while (digitCurrentPositon < dinPart.length()) {
	    digitHundredPostion = (dinPart.length() - digitCurrentPositon) % 3;
	    switch (digitHundredPostion) {
	    // STOTINE
	    case 0:
		charAt = dinPart.charAt(digitCurrentPositon);
		if (charAt != ZERO) {
		    sb.append(SPACE);
		    sb.append(words.get(getNum(charAt + "") * 100));
		}
		break;
	    // DESETICE
	    case 2:
		charAt = dinPart.charAt(digitCurrentPositon);
		if (charAt != ZERO) {
		    if (charAt == ONE) {
			sb.append(SPACE);
			long tenth = getNum(dinPart.substring(digitCurrentPositon, digitCurrentPositon + 2));
			sb.append(words.get(tenth));
			digitCurrentPositon++;
			appendHundreds(digitCurrentPositon, dinPart, tenth);
		    } else {
			sb.append(SPACE);
			sb.append(words.get(getNum(charAt + "" + ZERO)));
		    }
		}
		break;
	    // JEDINICE
	    case 1:
		charAt = dinPart.charAt(digitCurrentPositon);
		if (charAt != ZERO && (!(digitCurrentPositon == 0 && charAt == ONE) || dinPart.length() == 1)) {
		    val = getNum(charAt + "");
		    if ((val == 1 || val == 2) && isPlural(dinPart.length() - digitCurrentPositon)) {
			val += 100L;
		    }
		    sb.append(SPACE);
		    sb.append(words.get(val));
		}
		if (digitCurrentPositon == 0 || (charAt != ZERO || dinPart.charAt(digitCurrentPositon - 1) != ZERO
			|| dinPart.charAt(digitCurrentPositon - 2) != ZERO)) {
		    appendHundreds(digitCurrentPositon, dinPart, 0);
		}
		break;
	    default:
		break;
	    }
	    digitCurrentPositon++;
	}
	if (dinPart.charAt(dinPart.length() - 1) == ONE && !endsWithEleven(dinPart)) {
	    sb.append(SPACE);
	    sb.append(DINAR);
	} else {
	    if (getNum(dinPart) == 0) {
		sb.append(SPACE);
		sb.append(words.get(0L));
	    }
	    sb.append(SPACE);
	    sb.append(DINARA);
	}

	// para part
	val = getNum(paraPart);
	if (val != 0) {
	    sb.append(" i");
	    charAt = paraPart.charAt(0);
	    if (charAt != ZERO) {
		if (charAt == ONE) {
		    sb.append(SPACE);
		    sb.append(words.get(val));
		} else {
		    sb.append(SPACE);
		    sb.append(words.get(getNum(charAt + "" + ZERO)));
		}
	    }
	    if (charAt != ONE) {
		charAt = paraPart.charAt(1);
		if (charAt != ZERO) {
		    if (charAt == ONE || charAt == TWO) {
			sb.append(SPACE);
			sb.append(words.get(getNum(charAt + "") + 100L));
		    } else {
			sb.append(SPACE);
			sb.append(words.get(getNum(charAt + "")));
		    }
		}
	    }
	    sb.append(SPACE);
	    val = getNum(paraPart.charAt(1) + "");
	    if ((val == 2L || val == 3L || val == 4L) && !(getNum(paraPart) == val + 10L)) {
		sb.append(PARE);
	    } else {
		sb.append(PARA);
	    }
	}

	firstCharToUpperCase();
	return sb.toString();
    }

    private void firstCharToUpperCase() {
	if (sb.length() > 0) {
	    sb.replace(0, 1, "");
	}
	sb.setCharAt(0, Character.toUpperCase(sb.charAt(0)));
    }

    private void appendHundreds(int digitCurrentPositon, String dinPart, long tenth) {
	long pos = dinPart.length() - digitCurrentPositon;
	long val = 0;
	if (pos > 3) {
	    long pow = pos / 3L;
	    pos = (long) Math.pow(1000d, pow);
	    val = getNum(dinPart.charAt(digitCurrentPositon) + "");
	    if (pos == 1000L || pos == 1000000000L || pos == 1000000000000000L) {
		if (digitCurrentPositon == 0 && val == 1L) {
		    pos += 3;
		} else if ((val == 2L || val == 3L || val == 4L) && !(tenth == val + 10L)) {
		    pos += 2;
		} else if (val != 1L || tenth == val + 10L) {
		    pos += 1;
		}
	    } else if (val != 1 || tenth == val + 10L) {
		pos += 1;
	    }
	    sb.append(SPACE);
	    sb.append(words.get(pos));
	}
    }

    private boolean isPlural(long pos) {
	pos = (pos - 1L) / 3L;
	return pos == 1L || pos == 3L || pos == 5L;
    }

    private boolean endsWithEleven(String value) {
	if (value.length() > 1 && value.charAt(value.length() - 2) == ONE) {
	    return true;
	}
	return false;
    }

    private long getNum(String val) {
	return Long.parseLong(val);
    }

    /**
     * Converts and positive number to words.
     * 
     * @param value Number to convert to words.
     * @return
     * @throws Exceptionv Value out of bounds exception.
     */
    public static String convert(double value) throws Exception {
	if (instance == null) {
	    instance = new NTWC();
	}
	if (value > Long.MAX_VALUE) {
	    throw new Exception("Number out of bounds. " + value);
	}
	return instance.transform(String.format("%.2f", value));
    }
}
