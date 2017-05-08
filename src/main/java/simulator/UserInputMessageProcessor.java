package simulator;

import java.io.Closeable;
import java.io.IOException;
import java.util.Scanner;

/**
 * @author Dominik C.
 */
public class UserInputMessageProcessor implements Closeable {

    private Scanner scanner;

    public UserInputMessageProcessor() {
        scanner = new Scanner(System.in);
    }

    public String createMessageFromInput() {
        System.out.println("Enter command for server or enter beginning of message");

        String message = scanner.nextLine();

        if (message.toLowerCase().startsWith("pos") && !message.toLowerCase().contains("x")) {
            message = getPosMessageForServer();
        }

        return message;
    }

    public String getPosMessageForServer() {
        System.out.println("POS message");

        StringBuilder builder = new StringBuilder("POS: x=");
        System.out.println("x=");
        String x = scanner.next();

        System.out.println("y=");
        String y = scanner.next();

        builder.append(x).append(";y=").append(y);

        return builder.toString();
    }

    @Override
    public void close() throws IOException {
        if (scanner != null) {
            scanner.close();
        }
    }
}
