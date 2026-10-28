package vn.edu.ptit.oop.nhom06.model.device;

import vn.edu.ptit.oop.nhom06.model.Alert;
import vn.edu.ptit.oop.nhom06.model.Reading;
import vn.edu.ptit.oop.nhom06.model.SensorHub;
import vn.edu.ptit.oop.nhom06.model.SensorListener;
import vn.edu.ptit.oop.nhom06.util.PropertiesHelper;

/**
 * Lớp Máy sục khí (Aerator) thực thi Controllable và SensorListener.
 * Tính năng BONUS: Tự động bật khi Oxy hòa tan (DO) xuống thấp và tắt khi ổn định.
 */
public class Aerator implements Controllable, SensorListener {
    private boolean state = false; // Trạng thái mặc định là tắt
    private String pondId;

    public Aerator(String pondId) {
        this.pondId = pondId;
        // Đăng ký nhận sự kiện từ Hub để tự động phản ứng
        SensorHub.getInstance().addSensorListener(this);
    }

    @Override
    public void turnOn() {
        if (!state) {
            state = true;
            System.out.println("[BONUS] Máy sục khí ao " + pondId + " ĐÃ BẬT do DO xuống thấp!");
            // Trong thực tế sẽ gửi lệnh qua IoT Gateway
        }
    }

    @Override
    public void turnOff() {
        if (state) {
            state = false;
            System.out.println("[BONUS] Máy sục khí ao " + pondId + " ĐÃ TẮT do DO đã ổn định.");
        }
    }

    @Override
    public boolean isOn() {
        return state;
    }

    @Override
    public void onReading(Reading r) {
        // Chỉ quan tâm đến cảm biến DO (Oxy hòa tan)
        if (r.getSensorId().equals("do")) {
            double minSafeDO = PropertiesHelper.getMinThreshold("do", 4.0);
            // Nếu DO thấp hơn ngưỡng an toàn -> Bật sục khí
            if (r.getValue() < minSafeDO) {
                turnOn();
            } else {
                // Nếu DO đã cao hơn ngưỡng -> Tắt sục khí
                turnOff();
            }
        }
    }

    @Override
    public void onAlert(Alert a) {
        // Có thể bổ sung logic gửi tin nhắn SMS khẩn cấp ở đây
    }
}
