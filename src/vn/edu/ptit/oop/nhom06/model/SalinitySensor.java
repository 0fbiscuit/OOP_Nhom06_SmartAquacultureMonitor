package vn.edu.ptit.oop.nhom06.model;

import java.util.Random;

/**
 * Lớp Cảm biến Độ mặn, kế thừa từ lớp Sensor.
 * Thể hiện nguyên lý Kế thừa và Đa hình.
 */
public class SalinitySensor extends Sensor {
    
    private Random random;
    private double baseValue; 

    public SalinitySensor(String sensorId, double minSafe, double maxSafe) {
        // Độ mặn an toàn cho tôm thường từ 10 - 25 phần ngàn (ppt / ‰)
        super(sensorId, "Độ mặn (Salinity)", "‰", minSafe, maxSafe);
        this.random = new Random();
        // Độ mặn trung bình thường ở mức 15 ‰
        this.baseValue = 15.0; 
    }

    /**
     * Ghi đè phương thức read() để sinh dữ liệu.
     * Độ mặn thay đổi rất chậm trong thực tế, nên biên độ dao động nhỏ.
     */
    @Override
    public double read() {
        double noise = random.nextGaussian() * 0.8; 
        double currentValue = this.baseValue + noise;
        
        // Đảm bảo độ mặn không âm
        if (currentValue < 0) currentValue = 0.0;
        
        // Làm tròn 1 chữ số thập phân
        return Math.round(currentValue * 10.0) / 10.0;
    }
}
