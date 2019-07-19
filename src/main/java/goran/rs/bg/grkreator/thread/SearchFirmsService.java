package goran.rs.bg.grkreator.thread;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import com.jfoenix.controls.JFXTextField;

import goran.rs.bg.grkreator.model.Firm;
import javafx.application.Platform;
import javafx.beans.property.IntegerProperty;
import javafx.collections.ObservableList;
import javafx.concurrent.Service;
import javafx.concurrent.Task;

public class SearchFirmsService extends Service<Void> {
	
	private final long SLEEP_TIME = 201L;
	private final JFXTextField searchField;
	private final IntegerProperty searchTypeIndex;
	private final EntityManager em;
	private TypedQuery<Firm> query;
	private String oldSearchText = "*";
	private final ObservableList<Firm> items;

	public SearchFirmsService(ObservableList<Firm> items, JFXTextField searchField, IntegerProperty serachTypeIndex, EntityManager em) {
		this.items = items;
		this.searchField = searchField;
		this.searchTypeIndex = serachTypeIndex;
		this.em = em;
	}
	
	public void resetSearch() {
		oldSearchText = "*";
	}
	
	@Override
	protected Task<Void> createTask() {
		return new Task<Void>() {

			@Override
			protected Void call() throws Exception {
				while (!this.isCancelled()) {
					String newSearchText = searchField.getText();
					if (!oldSearchText.equals(newSearchText)) {
						switch (searchTypeIndex.get()) {
						case 0:
							query = em.createNamedQuery("Firm.findByName", Firm.class);
							query.setParameter("name", "%" + newSearchText + "%");
							break;
						case 1:
							query = em.createNamedQuery("Firm.findByPib", Firm.class);
							query.setParameter("pib", newSearchText + "%");
							break;
						case 2:
							query = em.createNamedQuery("Firm.findByIdnu", Firm.class);
							query.setParameter("idnu", newSearchText + "%");
							break;
						default:
							throw new Exception("Unknown search type index.");
						}
						
						List<Firm> resultList = query.getResultList();
						
						Platform.runLater(()->{
							items.clear();
							items.addAll(resultList);
						});
						
						oldSearchText = newSearchText;
					}
					
					Thread.sleep(SLEEP_TIME);
				}
				return null;
			}
		};
	}

}
