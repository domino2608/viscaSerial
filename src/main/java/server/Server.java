package server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by Dominik C.
 */
public class Server {

    private ServerSocket serverSocket;

    public Server(String host, int port) throws IOException {
        initServer(host, port);
    }

    private void initServer(String host, int port) throws IOException {
        serverSocket = new ServerSocket();
        serverSocket.bind(new InetSocketAddress(host, port));
    }

    public void listenAndHandleConections() throws IOException {
        boolean isRunning = true;

        while (isRunning) {
            Socket sckSocket = serverSocket.accept();

            ConnectionHandler connectionHandler = new ConnectionHandler(sckSocket);
            connectionHandler.handleCommunication();

            connectionHandler.close();
        }
    }

}
