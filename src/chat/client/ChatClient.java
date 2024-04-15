package chat.client;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class ChatClient extends Application {

	@Override
	public void start(Stage primaryStage) {
		Parent root = null;
		FXMLLoader loader = new FXMLLoader(getClass().getResource("chatClient.fxml"));
		
		try {
			root = loader.load();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		Scene scene = new Scene(root);
		primaryStage.setScene(scene);

		// Java Application 창 닫을 시 소켓 close
		ChatClientController controller = loader.getController();
		primaryStage.setOnCloseRequest((e) -> {
			controller.handleQuitButtonAction(null);
		});


		primaryStage.show();
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
