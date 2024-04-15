package chat.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;

public class ChatServerController implements Initializable {
	
	@FXML private Button startButton;
	@FXML private TextArea textArea;
	
	private void printMessage(String msg) {
		Platform.runLater(() -> {
			textArea.appendText(msg + "\n");
		});
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		
		startButton.setOnAction(e -> handleStartButtonAction(e));
	}

	
	public void handleStartButtonAction(ActionEvent e) {
		try {
			// 서버 소켓 생성
			ServerSocket server = new ServerSocket(4444);
			
			// accept()에 의해서 Thread가 block됨
			// UI Thread가 block되어서 화면을 사용할 수 없음
			// 별도의 Thread로 만들어서 사용해야 한다.
			new Thread(() -> {
				try {
					printMessage("서버가 시작되었어요.");
					
					while (true) {
						Socket s = server.accept(); // 클라이언트 접속 대기
						
						// 클라이언트가 접속해서 만들어진 socket을 이용해서
						// 통신처리를 담당하는 Thread를 만들어서 실행
						new Thread(new ConnectedThread(s, textArea)).start();
					}
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}).start();
			
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
	}
	
}
