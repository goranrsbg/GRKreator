package goran.rs.bg.grkreator;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class DB {

	private static DB instance;

	private EntityManagerFactory emf;
	
	private DB() {
		makeTheConnection();
	}
	
	private void makeTheConnection() {
		emf = Persistence.createEntityManagerFactory("PU");
	}

	public static void connect() {
		DB.instance = new DB();
	}
	
	public static EntityManager getNewEntityManager() {
		return DB.instance.emf.createEntityManager();
	}
	
	public static void close() {
		DB.instance.emf.close();
	}
	
}
