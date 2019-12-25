package goran.rs.bg.grkreator.etc;

import java.util.HashMap;
import java.util.Map;

public class Store {

    private static Store STORE = null;

    private Map<Integer, Object> data;

    private Store() {
	Store.STORE = this;
	data = new HashMap<>();
    }

    public static void initStore() {
	if (Store.STORE == null) {
	    new Store();
	}
    }

    public static void put(Integer key, Object value) {
	if (Store.STORE == null) {
	    Store.initStore();
	}
	Store.STORE.data.put(key, value);
    }

    public static Object get(Integer key) {
	if (Store.STORE == null) {
	    Store.initStore();
	}
	return Store.STORE.data.get(key);
    }

}
