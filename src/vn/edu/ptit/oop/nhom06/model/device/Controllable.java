package vn.edu.ptit.oop.nhom06.model.device;

/**
 * Interface cho các thiết bị chấp hành (Bonus).
 * Đảm bảo yêu cầu: "interface Controllable cho thiết bị chấp hành".
 */
public interface Controllable {
    void turnOn();
    void turnOff();
    boolean isOn();
}
