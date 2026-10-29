package vn.edu.ptit.oop.nhom06.ui.charts;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.StandardChartTheme;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.time.Second;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;

import vn.edu.ptit.oop.nhom06.model.Alert;
import vn.edu.ptit.oop.nhom06.model.Reading;
import vn.edu.ptit.oop.nhom06.model.SensorHub;
import vn.edu.ptit.oop.nhom06.model.SensorListener;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Cửa sổ chứa Biểu đồ hiển thị dữ liệu thời gian thực và thống kê cảnh báo.
 * Sử dụng thư viện JFreeChart 1.5.x theo yêu cầu.
 */
public class ChartWindow extends JFrame implements SensorListener {

    // Dữ liệu cho biểu đồ TimeSeries (Realtime)
    private Map<String, TimeSeries> seriesMap;
    
    // Dữ liệu cho biểu đồ Cột (BarChart)
    private DefaultCategoryDataset alertDataset;
    private Map<String, Integer> alertCounts;

    public ChartWindow() {
        super("Biểu đồ Phân tích Dữ liệu Ao nuôi");
        setSize(800, 600);
        setLocationRelativeTo(null);
        // Lưu ý kỹ thuật số 5: KHÔNG dùng EXIT_ON_CLOSE cho cửa sổ con
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        
        setupVietnameseFont(); // Chống lỗi ô vuông tiếng Việt

        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.addTab("Thời gian thực (Realtime)", createRealtimeChartPanel());
        tabbedPane.addTab("Thống kê Cảnh báo", createBarChartPanel());
        
        add(tabbedPane, BorderLayout.CENTER);

        // Đăng ký nhận dữ liệu từ Hub
        SensorHub.getInstance().addSensorListener(this);
    }

    private JPanel createRealtimeChartPanel() {
        seriesMap = new HashMap<>();
        TimeSeriesCollection dataset = new TimeSeriesCollection();
        
        // Khởi tạo 4 đường đồ thị, chỉ lưu 60 giây gần nhất
        String[] sensors = {"temp", "ph", "do", "sal"};
        String[] names = {"Nhiệt độ", "Độ pH", "DO", "Độ mặn"};
        
        for (int i = 0; i < sensors.length; i++) {
            TimeSeries series = new TimeSeries(names[i]);
            series.setMaximumItemAge(60); // Chỉ giữ 60 giây gần nhất
            seriesMap.put(sensors[i], series);
            dataset.addSeries(series);
        }

        JFreeChart chart = ChartFactory.createTimeSeriesChart(
                "Biến động các chỉ số trong 60 giây qua",
                "Thời gian (Giây)",
                "Giá trị",
                dataset,
                true, true, false
        );
        
        return new ChartPanel(chart);
    }

    private JPanel createBarChartPanel() {
        alertDataset = new DefaultCategoryDataset();
        alertCounts = new HashMap<>();
        
        // Khởi tạo mốc đếm bằng 0
        String[] sensors = {"Nhiệt độ", "Độ pH", "Oxy hòa tan", "Độ mặn"};
        for (String s : sensors) {
            alertCounts.put(s, 0);
            alertDataset.addValue(0, "Cảnh báo", s);
        }

        JFreeChart chart = ChartFactory.createBarChart(
                "Số lần cảnh báo theo Cảm biến",
                "Loại cảm biến",
                "Số lần vượt ngưỡng",
                alertDataset
        );
        
        return new ChartPanel(chart);
    }

    /**
     * Tiện ích cấu hình Font tiếng Việt cho JFreeChart để không bị lỗi font.
     */
    private void setupVietnameseFont() {
        StandardChartTheme theme = (StandardChartTheme) StandardChartTheme.createJFreeTheme();
        theme.setExtraLargeFont(new Font("Segoe UI", Font.BOLD, 18));
        theme.setLargeFont(new Font("Segoe UI", Font.BOLD, 14));
        theme.setRegularFont(new Font("Segoe UI", Font.PLAIN, 12));
        ChartFactory.setChartTheme(theme);
    }

    @Override
    public void onReading(Reading r) {
        // Cập nhật biểu đồ đường theo thời gian thực
        TimeSeries series = seriesMap.get(r.getSensorId());
        if (series != null) {
            // Thêm dữ liệu vào giây hiện tại, JFreeChart sẽ tự động vẽ lại
            series.addOrUpdate(new Second(), r.getValue());
        }
    }

    @Override
    public void onAlert(Alert a) {
        // Cập nhật số lần cảnh báo cho biểu đồ cột
        String name = a.getSensorName();
        // Lấy tên rút gọn nếu tên có chứa ngoặc (ví dụ: "Nhiệt độ (Temperature)" -> "Nhiệt độ")
        if(name.contains("(")) {
            name = name.substring(0, name.indexOf("(")).trim();
        }
        
        Integer currentCount = alertCounts.getOrDefault(name, 0);
        alertCounts.put(name, currentCount + 1);
        
        // Cập nhật Dataset, BarChart sẽ tự động vẽ lại cột
        alertDataset.setValue(currentCount + 1, "Cảnh báo", name);
    }
}
