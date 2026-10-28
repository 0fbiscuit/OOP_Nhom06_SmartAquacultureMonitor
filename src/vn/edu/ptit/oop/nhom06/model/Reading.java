package vn.edu.ptit.oop.nhom06.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Lớp Bất biến (Immutable) lưu trữ một lần đọc dữ liệu từ cảm biến.
 */
public final class Reading {
    
    // Các trường dữ liệu đều là private và final (không thể thay đổi sau khi khởi tạo)
    private final String sensorId;
    private final double value;
    private final LocalDateTime timestamp;

    /**
     * Constructor duy nhất để khởi tạo giá trị.
     */
    public Reading(String sensorId, double value, LocalDateTime timestamp) {
        this.sensorId = sensorId;
        this.value = value;
        this.timestamp = timestamp;
    }

    // CHỈ CÓ GETTER, KHÔNG CÓ SETTER để đảm bảo tính bất biến (Immutable)
    
    public String getSensorId() {
        return sensorId;
    }

    public double getValue() {
        return value;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }
    
    /**
     * Hàm tiện ích trả về thời gian định dạng chuỗi (dùng cho bảng logTable).
     */
    public String getFormattedTimestamp() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss dd/MM/yyyy");
        return timestamp.format(formatter);
    }
}
