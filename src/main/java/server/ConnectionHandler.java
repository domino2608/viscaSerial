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

    SerialPortCommunicationHandler serialPortCommunicationHandler;

    public ConnectionHandler(Socket sck) throws IOException {
        this.sck = sck;

        initStreams();
    }

    private void initStreams() throws IOException {
        reader = new BufferedReader(new InputStreamReader(sck.getInputStream()));
        writer = new PrintWriter(new OutputStreamWriter(sck.getOutputStream()), true);
    }

    private void initSerialPortCommunictionHandler() throws PortInUseException,
            UnsupportedCommOperationException, TooManyListenersException, IOException {

        serialPortCommunicationHandler =
                new SerialPortCommunicationHandler(SerialPortCommunicationHandler.getActiveCommPortsList().get(0));
    }

    public void handleCommunication () throws IOException {
        String cmd = reader.readLine();
        System.out.println("Got command: " + cmd);

        switch (cmd) {
            case "right":
                serialPortCommunicationHandler.sendCommandAndGetResponse(CameraCommandUtil.MOVE_RIGHT);
                break;
        }

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
