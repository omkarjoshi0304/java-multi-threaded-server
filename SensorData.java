import java.io.Serializable;

public class SensorData implements Serializable {
    private String deviceName;
    private double[] values;

    public SensorData(String deviceName, double[] values) {
        this.deviceName = deviceName;
        this.values = values;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public double[] getValues() {
        return values;
    }
}


