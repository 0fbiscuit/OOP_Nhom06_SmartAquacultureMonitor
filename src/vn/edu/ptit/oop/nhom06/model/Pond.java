package vn.edu.ptit.oop.nhom06.model;

import java.util.ArrayList;
import java.util.List;

public class Pond {
    private String pondId;
    private String name;
    private double area;

    private List<Sensor> sensors;

    public Pond(String pondId, String name, double area) {
        this.pondId = pondId;
        this.name = name;
        this.area = area;
        this.sensors = new ArrayList<>();
    }

    public void addSensor(Sensor sensor) {
        if (sensor != null) {
            this.sensors.add(sensor);
        }
    }

    public String getPondId() { return pondId; }
    public String getName() { return name; }
    public double getArea() { return area; }

    public List<Sensor> getSensors() {
        return java.util.Collections.unmodifiableList(sensors);
    }

    @Override
    public String toString() {
        return name;
    }
}
