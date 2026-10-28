package vn.edu.ptit.oop.nhom06.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Alert {
    private String sensorId;
    private String sensorName;
    private double abnormalValue;
    private String message;
    private LocalDateTime timestamp;

    public Alert(String sensorId, String sensorName, double abnormalValue, String message) {
        this.sensorId = sensorId;
        this.sensorName = sensorName;
        this.abnormalValue = abnormalValue;
        this.message = message;
        this.timestamp = LocalDateTime.now();
    }

    public String getSensorId() { return sensorId; }
    public String getSensorName() { return sensorName; }
    public double getAbnormalValue() { return abnormalValue; }
    public String getMessage() { return message; }
    public LocalDateTime getTimestamp() { return timestamp; }

    public String getFormattedTimestamp() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        return timestamp.format(formatter);
    }
}
