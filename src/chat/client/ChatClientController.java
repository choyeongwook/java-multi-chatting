package chat.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class ChatClientController implements Initializable {
	
	@FXML TextField inputNameField;
	@FXML TextField inputMessageField;
	@FXML Button transportButton;
	@FXML Button quitButton;
	@FXML TextArea textArea;
	
	private Socket s;
	private BufferedReader in;
	private PrintWriter out;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		transportButton.setOnAction(e -> handleTransportButtonAction(e));
		quitButton.setOnAction(e -> handleQuitButtonAction(e));
		
		try {
			s = new Socket("localhost", 4444);
			in = new BufferedReader(new InputStreamReader(s.getInputStream()));
			out = new PrintWriter(s.getOutputStream());
			
			printMessage("서버에 연결되었습니다.");

			new Thread(() -> {
				while (true) {
					try {
						String receivedMsg = in.readLine();
						printMessage(receivedMsg);
					} catch (IOException e) {
						throw new RuntimeException(e);
					}
				}
			}).start();
			
		     
		} catch (UnknownHostException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
	}
	
	public void handleTransportButtonAction(ActionEvent e) {
		String msg = inputNameField.getText() + " > " + inputMessageField.getText();
		out.println(msg);
		out.flush();

		// readLine() 메소드로 인해 block을 피하기 위해 쓰레드로 읽기 동작 수행
//		try {
//			String receivedMsg = in.readLine();
//			printMessage(receivedMsg);
//		} catch (IOException e1) {
//			e1.printStackTrace();
//		}
	}
	public void handleQuitButtonAction(ActionEvent e) {
		// TODO: 서버 연결 해제
		System.out.println("종료버튼 클릭");
	}
	
	public void sendMessage(String msg) {
		
	}
	
	public void printMessage(String msg) {
		Platform.runLater(() -> {
			textArea.appendText(msg + "\n");
		});
	}

	
}
