package vn.edu.ptit.oop.nhom06.model;

import java.util.Random;

/**
 * Lớp Cảm biến Nhiệt độ, kế thừa từ lớp Sensor.
 * Thể hiện nguyên lý Kế thừa (Inheritance) và Đa hình (Polymorphism).
 */
public class TemperatureSensor extends Sensor {
    
    private Random random;
    private double baseValue; // Giá trị trung tâm để mô phỏng

    public TemperatureSensor(String sensorId, double minSafe, double maxSafe) {
        super(sensorId, "Nhiệt độ (Temperature)", "°C", minSafe, maxSafe);
        this.random = new Random();
        // Giả sử nhiệt độ ao tôm thường xoay quanh 29 độ C
        this.baseValue = 29.0;
    }

    /**
     * Ghi đè phương thức read() để sinh dữ liệu ngẫu nhiên.
     * Dùng Random.nextGaussian() theo đúng yêu cầu đề tài (mục 6, Đề 9).
     */
    @Override
    public double read() {
        // Sinh giá trị dao động ngẫu nhiên theo phân phối chuẩn (Gaussian)
        // Độ lệch chuẩn (standard deviation) giả sử là 1.5 để dữ liệu có lúc vượt ngưỡng
        double noise = random.nextGaussian() * 1.5;
        double currentValue = this.baseValue + noise;
        
        // Làm tròn 1 chữ số thập phân cho đẹp
        return Math.round(currentValue * 10.0) / 10.0;
    }
}
