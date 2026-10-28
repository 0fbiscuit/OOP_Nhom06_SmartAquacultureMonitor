package vn.edu.ptit.oop.nhom06.ui.panels;

import java.awt.Color;
import java.awt.GridLayout;
import javax.swing.BorderFactory;
import javax.swing.JPanel;

/** TV8: Composition - dashboard chứa bốn đối tượng GaugePanel. */
public class DashboardPanel extends JPanel {
    private static final long serialVersionUID = 1L;
    public DashboardPanel() {
        setLayout(new GridLayout(2, 2, 16, 16));
        setBackground(new Color(242, 246, 250));
        setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));
        // Giá trị dự phòng khớp MainWindow; GaugePanel đọc Properties mỗi sự kiện.
        add(new GaugePanel("temp", "Nhiệt độ (°C)", 26.0, 32.0));
        add(new GaugePanel("ph", "Độ pH", 7.5, 8.5));
        add(new GaugePanel("do", "Oxy hòa tan (mg/L)", 4.0, 10.0));
        add(new GaugePanel("sal", "Độ mặn (‰)", 10.0, 25.0));
    }
}
