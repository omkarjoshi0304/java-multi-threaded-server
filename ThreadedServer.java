import java.io.*;
import java.net.*;
import java.util.*;

public class ThreadedServer {
    private static final int PORT = 5050;
    private ServerSocket serverSocket;
    private static List<ClientData> connectedClients = Collections.synchronizedList(new ArrayList<>());

    public static void main(String[] args) {
        System.out.println("Starting the server...");
        new ThreadedServer();
    }

    public ThreadedServer() {
        try {
            serverSocket = new ServerSocket(PORT);
            System.out.println("Server running on port " + PORT );
            new ServerGUI(); // Launch the server 

            while (true) {
                System.out.println("Waiting for client connections...");
                Socket clientSocket = serverSocket.accept();
                System.out.println("Client connected: " + clientSocket.getInetAddress());
                new Thread(new ThreadedConnectionHandler(clientSocket)).start();
            }
        } catch (IOException e) {
            System.err.println("Server error: " + e.getMessage());
        }
    }

    public static void updateClientData(String deviceName, double[] sensorValues) {
        synchronized (connectedClients) {
            connectedClients.removeIf(client -> client.getDeviceName().equals(deviceName));
            connectedClients.add(new ClientData(deviceName, sensorValues));
            ServerGUI.refreshDisplay(connectedClients); // Update GUI
        }
    }
}

class ClientData {
    private String deviceName;
    private double[] sensorValues;

    public ClientData(String deviceName, double[] sensorValues) {
        this.deviceName = deviceName;
        this.sensorValues = sensorValues;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public double[] getSensorValues() {
        return sensorValues;
    }
}
