package vn.edu.ptit.oop.nhom06.ui;

import javax.swing.*;

import vn.edu.ptit.oop.nhom06.common.BaseWindow;
import vn.edu.ptit.oop.nhom06.model.*;
import vn.edu.ptit.oop.nhom06.model.device.Aerator;
import vn.edu.ptit.oop.nhom06.service.SimulationEngine;
import vn.edu.ptit.oop.nhom06.ui.charts.ChartWindow;
import vn.edu.ptit.oop.nhom06.ui.dialogs.AboutDialog;
import vn.edu.ptit.oop.nhom06.ui.dialogs.ThresholdDialog;
import vn.edu.ptit.oop.nhom06.ui.panels.DashboardPanel;
import vn.edu.ptit.oop.nhom06.ui.tables.LogTableModel;

import java.awt.*;

/**
 * Lớp giao diện chính của chương trình. Bản hoàn chỉnh (Tích hợp code cả nhóm).
 */
public class MainWindow extends BaseWindow {

    private JComboBox<Pond> pondSelector;
    private JPanel dashboardPanel;
    private JTable logTable;
    private SimulationEngine engine;

    public MainWindow() {
        super("Nhóm 06 - Giám sát Ao nuôi tôm IoT", 1200, 800);
    }

    @Override
    protected void initUI() {
        // --- 1. KHỞI TẠO DỮ LIỆU MÔ PHỎNG (Code của Tổ Model & Observer) ---
        Farm farm = new Farm("Trại Tôm Minh Phú", "Cà Mau");
        Pond pond1 = new Pond("P1", "Ao 1 (Tôm sú)", 500);
        
        // Thêm các cảm biến (TV 3, 4, 5)
        pond1.addSensor(new TemperatureSensor("temp", 26.0, 32.0));
        pond1.addSensor(new PHSensor("ph", 7.5, 8.5));
        pond1.addSensor(new DissolvedOxygenSensor("do", 4.0, 10.0));
        pond1.addSensor(new SalinitySensor("sal", 10.0, 25.0));
        farm.addPond(pond1);
        
        // Khởi tạo máy sục khí (BONUS TV 10)
        new Aerator(pond1.getPondId());


        // --- 2. XÂY DỰNG GIAO DIỆN (UI) ---
        
        // --- Panel Trên cùng: Chọn ao & Nút chức năng ---
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.add(new JLabel("Chọn ao giám sát: "));
        
        // Đưa danh sách Pond vào JComboBox (TV 6)
        pondSelector = new JComboBox<>();
        for (Pond p : farm.getPonds()) {
            pondSelector.addItem(p);
        }
        topPanel.add(pondSelector);
        
        // Nút mở form nhập ngưỡng an toàn (TV 2)
        JButton btnSettings = new JButton("Cấu hình Ngưỡng (Threshold)");
        btnSettings.addActionListener(e -> new ThresholdDialog(this).setVisible(true));
        topPanel.add(btnSettings);
        
        // Nút mở Biểu đồ phân tích (TV 10)
        JButton btnChart = new JButton("Mở Biểu đồ (Charts)");
        btnChart.addActionListener(e -> new ChartWindow().setVisible(true));
        topPanel.add(btnChart);

        add(topPanel, BorderLayout.NORTH);

        // --- Panel Giữa: Dashboard chứa các GaugePanel (TV 8) ---
        dashboardPanel = new DashboardPanel();
        add(dashboardPanel, BorderLayout.CENTER);

        // --- Panel Dưới: Bảng nhật ký Log (TV 9) ---
        logTable = new JTable(new LogTableModel());
        JScrollPane scrollPane = new JScrollPane(logTable);
        scrollPane.setPreferredSize(new Dimension(1200, 200));
        
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.add(scrollPane, BorderLayout.CENTER);
        bottomPanel.add(this.statusBar, BorderLayout.SOUTH);
        
        add(bottomPanel, BorderLayout.SOUTH);


        // --- 3. KHỞI ĐỘNG HỆ THỐNG (Engine của TV 9) ---
        engine = new SimulationEngine(farm);
        engine.start();

        setStatus("Hệ thống đang chạy. Đang liên tục đọc dữ liệu cảm biến...");
    }

    /**
     * Mở cửa sổ thông tin nhóm (Code của TV 2).
     */
    @Override
    protected void showAbout() {
        new AboutDialog(this).setVisible(true);
    }
}
