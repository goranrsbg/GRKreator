package goran.rs.bg.grkreator;

import javafx.application.Preloader;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class GRKPreloader extends Preloader {

    private Stage stage;

    @Override
    public void start(Stage stage) throws Exception {
	this.stage = stage;
	Parent root = FXMLLoader.<Parent>load(getClass().getResource("/fxml/preloader.fxml"));
	Scene scene = new Scene(root);
	stage.initStyle(StageStyle.TRANSPARENT);
	scene.setFill(Color.TRANSPARENT);
	stage.setScene(scene);
	stage.show();
    }

    @Override
    public void handleStateChangeNotification(StateChangeNotification info) {
	super.handleStateChangeNotification(info);
	if (info.getType() == StateChangeNotification.Type.BEFORE_START) {
	    stage.hide();
	}
    }

}
