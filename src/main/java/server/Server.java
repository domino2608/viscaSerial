package server;

import java.io.Closeable;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by Dominik C.
 */
public class Server implements Closeable {

    private ServerSocket serverSocket;

    public Server(String host, int port) throws IOException {
        initServer(host, port);
    }

    private void initServer(String host, int port) throws IOException {
        serverSocket = new ServerSocket();
        serverSocket.bind(new InetSocketAddress(host, port));
    }

    public void listenAndHandleConnections() throws IOException {
        boolean isRunning = true;

        while (isRunning) {
            Socket sckSocket = serverSocket.accept();

            ConnectionHandler connectionHandler = null;
            try {
                connectionHandler = new ConnectionHandler(sckSocket);
                connectionHandler.handleCommunication();
                connectionHandler.close();
            } catch (Exception e) {
                e.printStackTrace();

                if (connectionHandler != null) {
                    connectionHandler.close();
                }
            }

        }
    }

    @Override
    public void close() throws IOException {
        if (serverSocket != null) {
            serverSocket.close();
        }
    }
}
