package vn.edu.ptit.oop.nhom06.util;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;

/**
 * Lớp tiện ích quản lý việc lưu và đọc cấu hình ngưỡng (Threshold)
 */
public class PropertiesHelper {
    private static final String FILE_NAME = "app.properties";
    private static Properties properties = new Properties();

    static {
        loadProperties();
    }

    private static void loadProperties() {
        try (Reader reader = Files.newBufferedReader(Path.of(FILE_NAME), StandardCharsets.UTF_8)) {
            properties.load(reader);
        } catch (IOException e) {
            // File chưa tồn tại ở lần chạy đầu tiên, bỏ qua
        }
    }

    public static void saveProperties() {
        try (Writer writer = Files.newBufferedWriter(Path.of(FILE_NAME), StandardCharsets.UTF_8)) {
            properties.store(writer, "Cau hinh nguong an toan cho cac cam bien");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void setThreshold(String sensorKey, double min, double max) {
        properties.setProperty(sensorKey + ".min", String.valueOf(min));
        properties.setProperty(sensorKey + ".max", String.valueOf(max));
        saveProperties();
    }

    public static double getMinThreshold(String sensorKey, double defaultValue) {
        String val = properties.getProperty(sensorKey + ".min");
        return val != null ? Double.parseDouble(val) : defaultValue;
    }

    public static double getMaxThreshold(String sensorKey, double defaultValue) {
        String val = properties.getProperty(sensorKey + ".max");
        return val != null ? Double.parseDouble(val) : defaultValue;
    }
}
