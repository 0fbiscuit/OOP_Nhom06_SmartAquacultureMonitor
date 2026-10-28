package vn.edu.ptit.oop.nhom06.ui.dialogs;

import javax.swing.*;
import java.awt.*;

/**
 *  * Cửa sổ About hiển thị thông tin nhóm và đề t
  */
public class AboutDialog extends JDialog {

    public AboutDialog(JFrame parent) {
        super(parent, "Thông tin Nhóm 09", true); // true = modal dialog
        setSize(400, 300);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout(10, 10));

        // Tiêu đề
        JLabel titleLabel = new JLabel("Đề tài 9: Giám sát Ao nuôi tôm IoT", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titleLabel.setForeground(new Color(0, 102, 204));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        add(titleLabel, BorderLayout.NORTH);

        // Nội dung thông tin thành viên
        String info = "Môn học: Lập trình hướng đối tượng \n"
                + "Giảng viên hướng dẫn: Lê Ngọc Hiếu\n\n"
                + "Danh sách thành viên:\n"
                + "1. Đào Thế Quân        - MainWindow & Khởi chạy hệ thống\n"
                + "2. Trần Nhật Tiến      - UI & Cấu hình\n"
                + "3. Trần Nguyễn Cầu     - Model Cảm biến\n"
                + "4. Lê Hồng Phát        - Model Cảm biến\n"
                + "5. Lê Hữu Phát         - Dữ liệu & Kiểm thử\n"
                + "6. Nguyễn Hoàng Hưng   - Logic Ao nuôi\n"
                + "7. Chiêm Lâm Khánh Tân - Event Listener\n"
                + "8. Trần Phú Cường      - Đồ họa Gauge\n"
                + "9. Nguyễn Ngọc Quy     - Bảng dữ liệu & Timer\n"
                + "10. Nguyễn Tấn Phát    - JFreeChart & Bonus";

        JTextArea textArea = new JTextArea(info);
        textArea.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        textArea.setEditable(false);
        textArea.setBackground(getBackground());
        textArea.setMargin(new Insets(10, 20, 10, 20));
        add(new JScrollPane(textArea), BorderLayout.CENTER);

        // Nút đóng
        JButton closeButton = new JButton("Đóng");
        closeButton.addActionListener(e -> dispose());
        JPanel bottomPanel = new JPanel();
        bottomPanel.add(closeButton);
        add(bottomPanel, BorderLayout.SOUTH);
    }
}
