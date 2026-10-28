package vn.edu.ptit.oop.nhom06.model;

import java.util.ArrayList;
import java.util.List;


public class SensorHub {
  
    private static SensorHub instance;
    
    private List<SensorListener> listeners;

    private SensorHub() {
        this.listeners = new ArrayList<>();
    }

    public static SensorHub getInstance() {
        if (instance == null) {
            instance = new SensorHub();
        }
        return instance;
    }

    public void addSensorListener(SensorListener listener) {
        if (listener != null && !listeners.contains(listener)) {
            listeners.add(listener);
        }
    }

    public void removeSensorListener(SensorListener listener) {
        listeners.remove(listener);
    }

    public void publishReading(Reading r) {
        for (SensorListener listener : listeners) {
            listener.onReading(r);
        }
    }

    
    public void publishAlert(Alert a) {
        for (SensorListener listener : listeners) {
            listener.onAlert(a);
        }
    }
}
