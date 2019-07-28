package goran.rs.bg.grkreator;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class GRkretor extends Application {

	@Override
	public void init() throws Exception {
		try {
			DB.connect();
		} catch (Exception e) {
			System.err.println(e.getMessage());
			Platform.exit();
		}
		Font.loadFont(getClass().getResourceAsStream("/fonts/Srisakdi-Regular.ttf"), 11d);
		Font.loadFont(getClass().getResourceAsStream("/fonts/Srisakdi-Bold.ttf"), 11d);
		Font.loadFont(getClass().getResourceAsStream("/fonts/Roboto-Regular.ttf"), 11d);
		Font.loadFont(getClass().getResourceAsStream("/fonts/Roboto-Bold.ttf"), 11d);
		Font.loadFont(getClass().getResourceAsStream("/fonts/VarelaRound-Regular.ttf"), 11d);
		Font.loadFont(getClass().getResourceAsStream("/fonts/CarroisGothicSC-Regular.ttf"), 11d);
	}
	
	@Override
	public void start(Stage stage) throws Exception {
		String fxmlFile = "/fxml/main.fxml";
		FXMLLoader loader = new FXMLLoader();
		Parent rootNode = loader.<Parent>load(getClass().getResourceAsStream(fxmlFile));
		Scene scene = new Scene(rootNode);
		stage.setTitle("GRkreator");
		stage.setScene(scene);
		stage.getIcons().add(new Image("file:icon.png"));
		stage.show();
	}

	@Override
	public void stop() throws Exception {
		DB.close();
	}
	
	public static void main(String[] args) {
		launch(args);
	}

}
