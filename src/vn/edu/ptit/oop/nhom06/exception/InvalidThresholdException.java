package vn.edu.ptit.oop.nhom06.exception;

/**
 * Lớp ngoại lệ tự định nghĩa để xử lý các trường hợp người dùng
 * hoặc hệ thống thiết lập ngưỡng an toàn không hợp lệ cho cảm biến.
 * Đảm bảo yêu cầu: "ít nhất 1 exception tự định nghĩa"
 */
public class InvalidThresholdException extends Exception {
    
    // Thuộc tính để lưu trữ thêm thông tin (giá trị gây lỗi)
    private final double invalidValue;

    // Constructor nhận thông điệp lỗi (minh họa Đóng gói/Kế thừa)
    public InvalidThresholdException(String message) {
        super(message);
        this.invalidValue = 0.0;
    }

    // Constructor nạp chồng (Overloading) nhận thêm giá trị gây lỗi
    public InvalidThresholdException(String message, double invalidValue) {
        super(message);
        this.invalidValue = invalidValue;
    }

    public double getInvalidValue() {
        return invalidValue;
    }
}
