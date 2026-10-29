package vn.edu.ptit.oop.nhom06.service;

import vn.edu.ptit.oop.nhom06.model.*;
import vn.edu.ptit.oop.nhom06.util.PropertiesHelper;

import javax.swing.Timer;
import java.time.LocalDateTime;

/**
 * Trái tim của hệ thống: Quản lý luồng thời gian thực.
 * Sử dụng javax.swing.Timer để đọc dữ liệu mỗi 1 giây và phát sự kiện.
 */
public class SimulationEngine {
    private Timer timer;
    private Farm farm;

    public SimulationEngine(Farm farm) {
        this.farm = farm;
        
        // Khởi tạo Timer chạy lặp lại mỗi 1000ms (1 giây)
        // Swing Timer đảm bảo các sự kiện được thực thi an toàn trên Event Dispatch Thread (EDT)
        this.timer = new Timer(1000, e -> processTick());
    }

    public void start() {
        if (!timer.isRunning()) {
            timer.start();
        }
    }

    public void stop() {
        if (timer.isRunning()) {
            timer.stop();
        }
    }

    /**
     * Hành động được gọi mỗi khi Timer tick (mỗi 1 giây).
     */
    private void processTick() {
        // Duyệt qua tất cả các ao trong trang trại
        for (Pond pond : farm.getPonds()) {
            // Duyệt qua tất cả các cảm biến trong ao
            for (Sensor sensor : pond.getSensors()) {
                
                // 1. Đọc dữ liệu (Đa hình: gọi hàm read() của từng lớp con)
                double value = sensor.read();
                
                // 2. Tạo đối tượng Reading bất biến
                Reading reading = new Reading(sensor.getSensorId(), value, LocalDateTime.now());
                
                // 3. Phát sự kiện có dữ liệu mới cho các GaugePanel và Biểu đồ
                SensorHub.getInstance().publishReading(reading);

                // 4. Kiểm tra ngưỡng an toàn (đọc từ Properties để lấy cấu hình mới nhất)
                double minSafe = PropertiesHelper.getMinThreshold(sensor.getSensorId(), sensor.getMinSafe());
                double maxSafe = PropertiesHelper.getMaxThreshold(sensor.getSensorId(), sensor.getMaxSafe());

                if (value < minSafe || value > maxSafe) {
                    String msg = (value < minSafe) ? "Thấp dưới ngưỡng Min" : "Vượt ngưỡng Max";
                    Alert alert = new Alert(sensor.getSensorId(), sensor.getName(), value, msg);
                    
                    // Phát sự kiện cảnh báo
                    SensorHub.getInstance().publishAlert(alert);
                }
            }
        }
    }
}
