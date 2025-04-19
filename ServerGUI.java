import javax.swing.*;
import java.awt.*;
import java.util.List;

public class ServerGUI extends JFrame {
    private static JTextArea logArea;

    public ServerGUI() {
        setTitle("Sensor Aggregation Server");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        logArea = new JTextArea();
        logArea.setEditable(false);
        add(new JScrollPane(logArea), BorderLayout.CENTER);

        setVisible(true);
    }

    public static synchronized void refreshDisplay(List<ClientData> clientDataList) {
        SwingUtilities.invokeLater(() -> {
            logArea.setText(""); // Clear the log
            for (ClientData clientData : clientDataList) {
                logArea.append("Device: " + clientData.getDeviceName() + 
                               ", Values: " + java.util.Arrays.toString(clientData.getSensorValues()) + "\n");
            }
        });
    }
}

