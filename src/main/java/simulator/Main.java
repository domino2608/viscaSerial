package simulator;

import java.io.IOException;

/**
 * @author Dominik C.
 */
public class Main {
    private static final String HOST = "localhost";
    private static final int PORT = 9000;

    public static void main(String[] args) throws IOException {
        new SimulatorConnector(HOST, PORT).handleConnection();
    }
}
