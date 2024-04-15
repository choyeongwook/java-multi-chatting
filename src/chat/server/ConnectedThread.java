package chat.server;

import javafx.application.Platform;
import javafx.scene.control.TextArea;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ConnectedThread implements Runnable {

    private Socket s;
    private BufferedReader in;
    private PrintWriter out;
    private TextArea textArea;

    private static List<PrintWriter> outStreamList = new ArrayList<>();

    public ConnectedThread(Socket s, TextArea textArea) {
        this.s = s;
        this.textArea = textArea;
        try {
            in = new BufferedReader(new InputStreamReader(s.getInputStream()));
            out = new PrintWriter(s.getOutputStream());

            outStreamList.add(out);
            printMessage("새 클라이언트 접속: " + s.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        // client가 메시지를 주는 것을 기다렸다가 메시지를 수신하면 다시 클라이언트에게 전달하는 작업 반복

        while (true) {
            String msg = null;
            try {
                msg = in.readLine();

                // 클라이언트가 연결 종료 요청 시
                if (msg == null) {
                    outStreamList.remove(out);
                    printMessage("클라이언트 연결 종료: " + s.toString());

                    in.close();
                    out.close();
                    s.close();

                    break;
                }
                System.out.println(outStreamList);
            } catch (IOException e) {
                e.printStackTrace();
            }
//            out.println(msg);
//            out.flush();

//             연결된 모든 클라이언트에게 print
            for (PrintWriter out : outStreamList) {
                out.println(msg);
                out.flush();
            }
        }
    }

    private void printMessage(String msg) {
        Platform.runLater(() -> {
            textArea.appendText(msg + "\n");
        });
    }
}