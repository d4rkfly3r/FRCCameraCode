package network;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by robotics on 2/8/2016.
 */
public class MainServerTest {

    ServerSocket serverSocket;

    MainServerTest() {
        try {
            serverSocket = new ServerSocket(7093, 3);
            Socket client;
            while ((client = serverSocket.accept()) != null) {
                while (client.getKeepAlive()) {
                    byte[] trs = new byte[1024];
                    client.getInputStream().read(trs);
                    System.out.println(new String(trs));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new MainServerTest();
    }
}
