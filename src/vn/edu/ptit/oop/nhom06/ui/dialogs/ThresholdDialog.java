package vn.edu.ptit.oop.nhom06.ui.dialogs;

import javax.swing.*;

import vn.edu.ptit.oop.nhom06.exception.InvalidThresholdException;
import vn.edu.ptit.oop.nhom06.util.PropertiesHelper;

import java.awt.*;

/**
 * Cửa sổ cấu hình ngưỡng an toàn.
 * Thể hiện ngoại lệ InvalidThresholdException.
 */
public class ThresholdDialog extends JDialog {

    private JSpinner minTemp, maxTemp;
    private JSpinner minPH, maxPH;
    private JSpinner minDO, maxDO;     
    private JSpinner minSal, maxSal;

    public ThresholdDialog(JFrame parent) {
        super(parent, "Cấu hình ngưỡng an toàn", true);
        setSize(400, 250);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout());

        JPanel formPanel = new JPanel(new GridLayout(5, 3, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // Header
        formPanel.add(new JLabel("Cảm biến", SwingConstants.CENTER));
        formPanel.add(new JLabel("Ngưỡng Min", SwingConstants.CENTER));
        formPanel.add(new JLabel("Ngưỡng Max", SwingConstants.CENTER));

        // Row 1: Nhiệt độ
        formPanel.add(new JLabel("Nhiệt độ (°C):"));
        minTemp = new JSpinner(new SpinnerNumberModel(PropertiesHelper.getMinThreshold("temp", 26.0), 0.0, 50.0, 0.5));
        maxTemp = new JSpinner(new SpinnerNumberModel(PropertiesHelper.getMaxThreshold("temp", 32.0), 0.0, 50.0, 0.5));
        formPanel.add(minTemp);
        formPanel.add(maxTemp);

        // Row 2: pH
        formPanel.add(new JLabel("Độ pH:"));
        minPH = new JSpinner(new SpinnerNumberModel(PropertiesHelper.getMinThreshold("ph", 7.5), 0.0, 14.0, 0.1));
        maxPH = new JSpinner(new SpinnerNumberModel(PropertiesHelper.getMaxThreshold("ph", 8.5), 0.0, 14.0, 0.1));
        formPanel.add(minPH);
        formPanel.add(maxPH);
        
        // Row 3: Oxy hòa tan (DO)
        formPanel.add(new JLabel("Oxy (mg/L):"));
        minDO = new JSpinner(new SpinnerNumberModel(PropertiesHelper.getMinThreshold("do", 4.0), 0.0, 10.0, 0.1));
        maxDO = new JSpinner(new SpinnerNumberModel(PropertiesHelper.getMaxThreshold("do", 8.0), 0.0, 10.0, 0.1));
        formPanel.add(minDO);
        formPanel.add(maxDO);

        // Row 4: Độ mặn
        formPanel.add(new JLabel("Độ mặn (‰):"));
        minSal = new JSpinner(new SpinnerNumberModel(PropertiesHelper.getMinThreshold("sal", 10.0), 0.0, 35.0, 0.5));
        maxSal = new JSpinner(new SpinnerNumberModel(PropertiesHelper.getMaxThreshold("sal", 25.0), 0.0, 35.0, 0.5));
        formPanel.add(minSal);
        formPanel.add(maxSal);

        add(formPanel, BorderLayout.CENTER);

        // Nút bấm
        JPanel buttonPanel = new JPanel();
        JButton btnSave = new JButton("Lưu cấu hình");
        JButton btnCancel = new JButton("Hủy");

        btnCancel.addActionListener(e -> dispose());
        btnSave.addActionListener(e -> saveSettings());

        buttonPanel.add(btnSave);
        buttonPanel.add(btnCancel);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void saveSettings() {
        double currentMinTemp = (Double) minTemp.getValue();
        double currentMaxTemp = (Double) maxTemp.getValue();
        double currentMinPH = (Double) minPH.getValue();
        double currentMaxPH = (Double) maxPH.getValue();
        double currentMinDO = (Double) minDO.getValue();
        double currentMaxDO = (Double) maxDO.getValue();
        double currentMinSal = (Double) minSal.getValue();
        double currentMaxSal = (Double) maxSal.getValue();

        try {
            // Kiểm tra tính hợp lệ, ném ngoại lệ nếu dữ liệu sai logic
            
            if (currentMinTemp >= currentMaxTemp) {
                throw new InvalidThresholdException("Ngưỡng tối thiểu của Nhiệt độ phải nhỏ hơn ngưỡng tối đa!");
            }
            if (currentMinPH >= currentMaxPH) {
                throw new InvalidThresholdException("Ngưỡng tối thiểu của pH phải nhỏ hơn ngưỡng tối đa!");
            }
            if (currentMinDO >= currentMaxDO) {
                throw new InvalidThresholdException("Ngưỡng Min của Oxy phải nhỏ hơn Max!");
            }
            if (currentMinSal >= currentMaxSal) {
                throw new InvalidThresholdException("Ngưỡng Min của Độ mặn phải nhỏ hơn Max!");
            }

            // Nếu không có lỗi, tiến hành lưu vào Properties
            PropertiesHelper.setThreshold("temp", currentMinTemp, currentMaxTemp);
            PropertiesHelper.setThreshold("ph", currentMinPH, currentMaxPH);
            PropertiesHelper.setThreshold("do", currentMinDO, currentMaxDO);
            PropertiesHelper.setThreshold("sal", currentMinSal, currentMaxSal);

            JOptionPane.showMessageDialog(this, "Lưu cấu hình thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            dispose();

        } catch (InvalidThresholdException ex) {
            // BẮT NGOẠI LỆ TỰ ĐỊNH NGHĨA
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Lỗi nhập liệu", JOptionPane.ERROR_MESSAGE);
        }
    }
}
