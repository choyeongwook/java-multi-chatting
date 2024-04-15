package chat.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ConnectedThread implements Runnable {

    private Socket s;
    private BufferedReader in;
    private PrintWriter out;

    public ConnectedThread(Socket s) {
        this.s = s;
        try {
            in = new BufferedReader(new InputStreamReader(s.getInputStream()));
            out = new PrintWriter(s.getOutputStream());
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

            } catch (IOException e) {
                e.printStackTrace();
            }
            out.println(msg);
            out.flush();

        }

    }
}