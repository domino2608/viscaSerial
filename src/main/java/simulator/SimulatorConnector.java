package simulator;

import java.io.*;
import java.net.Socket;

/**
 * @author Dominik C.
 */
public class SimulatorConnector implements Closeable {

    private Socket socket;
    private BufferedReader reader;
    private PrintWriter writer;
    private UserInputMessageProcessor userInputMessageProcessor = new UserInputMessageProcessor();

    public SimulatorConnector(String host, int port) throws IOException {
        socket = new Socket(host, port);
        initStreams();
    }

    private void initStreams() throws IOException {
        reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        writer = new PrintWriter(socket.getOutputStream(), true);
    }

    public void handleConnection() {
        boolean isRunning = true;

        while (isRunning) {
            String posMessage = userInputMessageProcessor.createMessageFromInput();

            writer.println(posMessage);
            System.out.println("Message sent");
        }
    }

    @Override
    public void close() throws IOException {
        if (writer != null) {
            writer.close();
        }

        if (reader != null) {
            reader.close();
        }

        if (socket != null) {
            socket.close();
        }
    }

}
