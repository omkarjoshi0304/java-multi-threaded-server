
import java.io.*;
import java.net.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class Client {
    private static final int PORT = 5050;
    private String serverAddress;
    private String deviceName;
    private Socket socket;
    private ObjectOutputStream output;

    // Sensor values
    private double temperature = 25.0; // Default values
    private double humidity = 50.0;
    private double pressure = 1013.0;

    public Client(String serverAddress, String deviceName) {
        this.serverAddress = serverAddress;
        this.deviceName = deviceName;

        try {
            socket = new Socket(serverAddress, PORT);
            output = new ObjectOutputStream(socket.getOutputStream());
            createGUI();
        } catch (IOException e) {
            System.err.println("Client error: " + e.getMessage());
        }

        // Add shutdown hook to close socket on exit
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                if (socket != null) {
                    socket.close();
                }
            } catch (IOException e) {
                System.err.println("Error closing socket: " + e.getMessage());
            }
        }));
    }

    private void sendSensorData() {
        try {
            double[] values = {temperature, humidity, pressure};
            SensorData data = new SensorData(deviceName, values);
            output.writeObject(data);
            output.flush();
        } catch (IOException e) {
            System.err.println("Error sending data: " + e.getMessage());
        }
    }

    private void createGUI() {
        JFrame frame = new JFrame(deviceName);
        frame.setSize(400, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        // Top panel: Device name
        JPanel topPanel = new JPanel();
        JLabel nameLabel = new JLabel("Device: " + deviceName);
        JButton changeNameButton = new JButton("Change Name");
        changeNameButton.addActionListener(e -> {
            String newName = JOptionPane.showInputDialog("Enter new device name:");
            if (newName != null && !newName.trim().isEmpty()) {
                deviceName = newName;
                nameLabel.setText("Device: " + deviceName);
            }
        });
        topPanel.add(nameLabel);
        topPanel.add(changeNameButton);
        frame.add(topPanel, BorderLayout.NORTH);

        // Center panel: Buttons to change sensor values
        JPanel buttonPanel = new JPanel(new GridLayout(3, 3, 10, 10));

        // Temperature controls
        JLabel tempLabel = new JLabel("Temperature: " + temperature + "°C");
        JButton tempIncreaseButton = new JButton("+");
        JButton tempDecreaseButton = new JButton("-");
        tempIncreaseButton.addActionListener(e -> updateSensorValue("Temperature", tempLabel, 1.0));
        tempDecreaseButton.addActionListener(e -> updateSensorValue("Temperature", tempLabel, -1.0));
        buttonPanel.add(tempLabel);
        buttonPanel.add(tempIncreaseButton);
        buttonPanel.add(tempDecreaseButton);

        // Humidity controls
        JLabel humidityLabel = new JLabel("Humidity: " + humidity + "%");
        JButton humidityIncreaseButton = new JButton("+");
        JButton humidityDecreaseButton = new JButton("-");
        humidityIncreaseButton.addActionListener(e -> updateSensorValue("Humidity", humidityLabel, 5.0));
        humidityDecreaseButton.addActionListener(e -> updateSensorValue("Humidity", humidityLabel, -5.0));
        buttonPanel.add(humidityLabel);
        buttonPanel.add(humidityIncreaseButton);
        buttonPanel.add(humidityDecreaseButton);

        // Pressure controls
        JLabel pressureLabel = new JLabel("Pressure: " + pressure + " hPa");
        JButton pressureIncreaseButton = new JButton("+");
        JButton pressureDecreaseButton = new JButton("-");
        pressureIncreaseButton.addActionListener(e -> updateSensorValue("Pressure", pressureLabel, 2.0));
        pressureDecreaseButton.addActionListener(e -> updateSensorValue("Pressure", pressureLabel, -2.0));
        buttonPanel.add(pressureLabel);
        buttonPanel.add(pressureIncreaseButton);
        buttonPanel.add(pressureDecreaseButton);

        frame.add(buttonPanel, BorderLayout.CENTER);

        // Bottom panel: Exit button
        JButton exitButton = new JButton("Exit");
        exitButton.addActionListener(e -> {
            try {
                if (socket != null) {
                    socket.close();
                }
                System.exit(0);
            } catch (IOException ex) {
                System.err.println("Error closing socket: " + ex.getMessage());
            }
        });
        frame.add(exitButton, BorderLayout.SOUTH);

        frame.setVisible(true);
    }

    private void updateSensorValue(String sensorType, JLabel label, double delta) {
        switch (sensorType) {
            case "Temperature":
                temperature = Math.max(0, temperature + delta); // Prevent negative values
                label.setText("Temperature: " + temperature + "°C");
                break;
            case "Humidity":
                humidity = Math.max(0, Math.min(100, humidity + delta)); // Keep within 0-100%
                label.setText("Humidity: " + humidity + "%");
                break;
            case "Pressure":
                pressure = Math.max(900, Math.min(1100, pressure + delta)); // Keep within realistic bounds
                label.setText("Pressure: " + pressure + " hPa");
                break;
        }
        sendSensorData(); // Send updated data to server
    }

    public static void main(String[] args) {
        if (args.length < 2) {
            System.out.println("Usage: java Client <serverAddress> <deviceName>");
            return;
        }
        new Client(args[0], args[1]);
    }
}
