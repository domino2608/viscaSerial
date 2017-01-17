package comm;

import comm.error.PortNotFoundException;
import gnu.io.*;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.TooManyListenersException;

/**
 * Created by Domino on 2016-12-10.
 */
public class SerialPortCommunicationHandler implements AutoCloseable {
    private static final String APP_NAME = "Visca port steering";
    private static final int BAUD_RATE = 9600;
    private static final int MAX_WAIT_TIME = 5000;

    private SerialPort serialPort;
    private InputStream inputStream;
    private OutputStream outputStream;

    public SerialPortCommunicationHandler(String portName) throws PortNotFoundException, PortInUseException,
            IOException, UnsupportedCommOperationException, TooManyListenersException {

        initializeSerialPort(getCOMMPortByName(portName));
        initializeIOStreams();
    }

    public SerialPortCommunicationHandler(CommPortIdentifier  commPortIdentifier) throws IOException,
            TooManyListenersException, PortInUseException, UnsupportedCommOperationException {

        initializeSerialPort(commPortIdentifier);
        initializeIOStreams();
    }

    private void initializeSerialPort(CommPortIdentifier commPort) throws PortInUseException,
            UnsupportedCommOperationException, IOException, TooManyListenersException {

        serialPort = (SerialPort) commPort.open(APP_NAME, MAX_WAIT_TIME);
        serialPort.setSerialPortParams(BAUD_RATE, SerialPort.DATABITS_8, SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);
    }

    private void initializeIOStreams() throws IOException {
        inputStream = serialPort.getInputStream();
        outputStream = serialPort.getOutputStream();
    }

//    private void addSerialPortEventListener() throws TooManyListenersException {
//        serialPort.addEventListener(this);
//        serialPort.notifyOnDataAvailable(true);
//        serialPort.notifyOnOutputEmpty(true);
//    }

    public String sendCommandAndGetResponse(int[] command) throws IOException {
        for (int b : command) {
            outputStream.write(b);
        }

        outputStream.flush();

        return getResponse();
    }

    private String getResponse() throws IOException {
        StringBuilder stringBuilder = new StringBuilder();

        int nextByte = 0;

        while ((nextByte = inputStream.read()) != -1) {
            stringBuilder.append(Integer.toHexString(nextByte)).append(" ");
        }

        return stringBuilder.toString();
    }

    private CommPortIdentifier getCOMMPortByName(String name) throws PortNotFoundException {
        Enumeration commPorts = CommPortIdentifier.getPortIdentifiers();

        while (commPorts.hasMoreElements()) {
            CommPortIdentifier commPortIdentifier = (CommPortIdentifier) commPorts.nextElement();

            if (CommPortIdentifier.PORT_SERIAL == commPortIdentifier.getPortType()
                    && commPortIdentifier.getName().equalsIgnoreCase(name)) {

                return commPortIdentifier;
            }
        }

        throw new PortNotFoundException("Port not found: " + name);
    }

    public static List<CommPortIdentifier> getActiveCommPortsList() {
        List <CommPortIdentifier> commPortIdentifiers = new ArrayList<>();

        Enumeration commPorts = CommPortIdentifier.getPortIdentifiers();

        while (commPorts.hasMoreElements()) {
            CommPortIdentifier commPortIdentifier = (CommPortIdentifier) commPorts.nextElement();

            if (CommPortIdentifier.PORT_SERIAL == commPortIdentifier.getPortType()) {

                commPortIdentifiers.add(commPortIdentifier);
            }
        }

        return commPortIdentifiers;
    }

    public static List<String> getActivePortNames() {
        List<String> activePorts = new ArrayList<>();

        Enumeration commPorts = CommPortIdentifier.getPortIdentifiers();
        while (commPorts.hasMoreElements()) {
            CommPortIdentifier commPortIdentifier = (CommPortIdentifier) commPorts.nextElement();

            if (CommPortIdentifier.PORT_SERIAL == commPortIdentifier.getPortType()) {
                System.out.println(commPortIdentifier.getName());
                activePorts.add(commPortIdentifier.getName());
            }
        }

        return activePorts;
    }

    @Override
    public void close() throws Exception {
        if (outputStream != null) {
            outputStream.close();
        }

        if (inputStream != null) {
            inputStream.close();
        }

        if (serialPort != null) {
            inputStream.close();
        }
    }
}
