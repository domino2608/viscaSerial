package server;

import comm.CameraCommandUtil;
import comm.SerialPortCommunicationHandler;
import gnu.io.PortInUseException;
import gnu.io.UnsupportedCommOperationException;

import java.io.*;
import java.net.Socket;
import java.util.TooManyListenersException;

/**
 * Created by Dominik C.
 */
public class ConnectionHandler implements Closeable {

    private Socket sck;
    private BufferedReader reader;
    private PrintWriter writer;

    private static SerialPortCommunicationHandler serialPortCommunicationHandler;

    static {

        try {
            serialPortCommunicationHandler =
                    new SerialPortCommunicationHandler(SerialPortCommunicationHandler.getActiveCommPortsList().get(0));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ConnectionHandler(Socket sck) throws Exception {
        this.sck = sck;

        initStreams();
    }

    private void initStreams() throws IOException {
        reader = new BufferedReader(new InputStreamReader(sck.getInputStream()));
        writer = new PrintWriter(new OutputStreamWriter(sck.getOutputStream()), true);
    }

    public void handleCommunication() throws IOException {
        String cmd = reader.readLine();
        System.out.println("Got command: " + cmd);

        String response = null;

        switch (cmd) {
            case "right":
                response = serialPortCommunicationHandler.sendCommandAndGetResponse(
                        CameraCommandUtil.getCommandCopyWithChangedSpeed(CameraCommandUtil.MOVE_RIGHT, 7, 7));
                break;
            case "left":
                response = serialPortCommunicationHandler.sendCommandAndGetResponse(
                        CameraCommandUtil.getCommandCopyWithChangedSpeed(CameraCommandUtil.MOVE_LEFT, 7, 7));
                break;
            case "up":
                response = serialPortCommunicationHandler.sendCommandAndGetResponse(
                        CameraCommandUtil.getCommandCopyWithChangedSpeed(CameraCommandUtil.MOVE_UP, 7, 7));
                break;
            case "down":
                response = serialPortCommunicationHandler.sendCommandAndGetResponse(
                        CameraCommandUtil.getCommandCopyWithChangedSpeed(CameraCommandUtil.MOVE_DOWN, 7, 7));
                break;
            case "stop":
                response = serialPortCommunicationHandler.sendCommandAndGetResponse(CameraCommandUtil.STOP);
                break;
        }

        System.out.println(response);

    }

    @Override
    public void close() throws IOException {
        if (reader != null) {
            reader.close();
        }
        if (writer != null) {
            writer.close();
        }
        sck.close();

        if (serialPortCommunicationHandler != null) {
            try {
                serialPortCommunicationHandler.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
