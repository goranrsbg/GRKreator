package goran.rs.bg.grkreator.format;

import goran.rs.bg.grkreator.model.Firm;
import javafx.scene.control.ListCell;

public class FirmListFormatCell extends ListCell<Firm> {

	@Override
	protected void updateItem(Firm item, boolean empty) {
		super.updateItem(item, empty);
		if (item != null) {
			this.setText(item.getName() + "(" + item.getPib() + ")");
		} else {
			this.setText("");
		}
	}

}
