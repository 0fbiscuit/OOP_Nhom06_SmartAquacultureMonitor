
package vn.edu.ptit.oop.nhom06;

import javax.swing.SwingUtilities;

import vn.edu.ptit.oop.nhom06.ui.MainWindow;

/**
 * Lớp khởi chạy ứng dụng (Entry point)
 */
public class Main {
    public static void main(String[] args) {
        // Đảm bảo GUI Swing được tạo và cập nhật trên Event Dispatch Thread (EDT)
        // Đây là yêu cầu bắt buộc (Mục 5 - Lưu ý kỹ thuật)
        SwingUtilities.invokeLater(() -> {
            MainWindow window = new MainWindow();
            window.setVisible(true);
        });
    }
}
