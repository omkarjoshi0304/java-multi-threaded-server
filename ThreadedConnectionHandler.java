import java.io.*;
import java.net.*;

public class ThreadedConnectionHandler implements Runnable {
    private Socket clientSocket;
    private ObjectInputStream input;

    public ThreadedConnectionHandler(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    @Override
    public void run() {
        try {
            input = new ObjectInputStream(clientSocket.getInputStream());
            while (true) {
                SensorData receivedData = (SensorData) input.readObject();
                ThreadedServer.updateClientData(receivedData.getDeviceName(), receivedData.getValues());
            }
        } catch (Exception e) {
            System.err.println("Error handling client: " + e.getMessage());
        } finally {
            try {
                if (clientSocket != null) {
                    clientSocket.close();
                }
            } catch (IOException e) {
                System.err.println("Error closing client socket: " + e.getMessage());
            }
        }
    }
}
