package vn.edu.ptit.oop.nhom09.model;


public interface SensorListener {
    
    void onReading(Reading r);

    
    void onAlert(Alert a);
}
