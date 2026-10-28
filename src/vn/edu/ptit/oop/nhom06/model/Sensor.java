package vn.edu.ptit.oop.nhom06.model;

import vn.edu.ptit.oop.nhom06.exception.InvalidThresholdException;

public abstract class Sensor {
    private final String sensorId; // mã cảm biến
    private final String name; // tên cảm biến
    private final String unit; // đơn vị đo
    private double minSafe; // dới hạn nhỏ nhất
    private double maxSafe; // dới hạn lón nhất

    public Sensor(String sensorId, String name, String unit, double minSafe, double maxSafe) {
        if (minSafe >= maxSafe) {
            throw new IllegalArgumentException(
                    "Khởi tạo thất bại: minSafe (" + minSafe + ") phải nhỏ hơn maxSafe (" + maxSafe + ")");
        }
        this.sensorId = sensorId;
        this.name = name;
        this.unit = unit;
        this.minSafe = minSafe;
        this.maxSafe = maxSafe;
    }

    public abstract double read();

    // ================= GETTERS =================
    public String getSensorId() {
        return sensorId;
    }

    public String getName() {
        return name;
    }

    public String getUnit() {
        return unit;
    }

    public double getMinSafe() {
        return minSafe;
    }

    public double getMaxSafe() {
        return maxSafe;
    }

    // ================= SETTERS VỚI KIỂM TRA RÀNG BUỘC (ĐÓNG GÓI) =================
    // Cập nhật cả hai ngưỡng min và max an toàn.
    public void setSafeThresholds(double min, double max) throws InvalidThresholdException {
        if (min >= max) {
            throw new InvalidThresholdException(
                    String.format("Lỗi cấu hình cảm biến [%s]: Ngưỡng Min (%.2f) phải nhỏ hơn Max (%.2f)!", this.name,
                            min, max));
        }
        this.minSafe = min;
        this.maxSafe = max;
    }

    // Cập nhật riêng ngưỡng minSafe.
    public void setMinSafe(double min) throws InvalidThresholdException {
        if (min >= this.maxSafe) {
            throw new InvalidThresholdException(
                    String.format("Lỗi cảm biến [%s]: Ngưỡng Min mới (%.2f) không được >= Max hiện tại (%.2f)!",
                            this.name, min, this.maxSafe));
        }
        this.minSafe = min;
    }

    // Cập nhật riêng ngưỡng maxSafe.
    public void setMaxSafe(double max) throws InvalidThresholdException {
        if (max <= this.minSafe) {
            throw new InvalidThresholdException(
                    String.format("Lỗi cảm biến [%s]: Ngưỡng Max mới (%.2f) không được <= Min hiện tại (%.2f)!",
                            this.name, max, this.minSafe));
        }
        this.maxSafe = max;
    }

    // Kiểm tra một giá trị đo có nằm trong vùng an toàn hay không.
    public boolean isSafe(double value) {
        return value >= this.minSafe && value <= this.maxSafe;
    }

    // Kiểm tra giá trị có vượt ngưỡng (cần kích hoạt Alert) hay không.
    public boolean isAlert(double value) {
        return !isSafe(value);
    }

    @Override
    public String toString() {
        return String.format("[%s] %s (An toàn: %.1f - %.1f %s)", sensorId, name, minSafe, maxSafe, unit);
    }
}