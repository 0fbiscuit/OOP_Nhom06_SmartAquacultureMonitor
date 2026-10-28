package vn.edu.ptit.oop.nhom06.ui.tables;

import vn.edu.ptit.oop.nhom06.model.Alert;
import vn.edu.ptit.oop.nhom06.model.Reading;
import vn.edu.ptit.oop.nhom06.model.SensorHub;
import vn.edu.ptit.oop.nhom06.model.SensorListener;

import javax.swing.table.AbstractTableModel;
import java.util.LinkedList;
import java.util.List;

/**
 * Lớp quản lý dữ liệu cho Bảng nhật ký (JTable).
 * Kế thừa AbstractTableModel và thực thi SensorListener để tự động cập nhật dữ liệu.
 */
public class LogTableModel extends AbstractTableModel implements SensorListener {
    // Chỉ giữ tối đa 200 dòng theo yêu cầu Đề 9
    private static final int MAX_ROWS = 200;
    
    private final String[] columnNames = {"Thời gian", "Mã Cảm biến", "Giá trị đọc", "Trạng thái / Cảnh báo"};
    
    // Dùng LinkedList để tối ưu việc thêm vào đầu và xóa ở cuối
    private final List<Object[]> dataList;

    public LogTableModel() {
        this.dataList = new LinkedList<>();
        // Đăng ký nhận sự kiện từ SensorHub
        SensorHub.getInstance().addSensorListener(this);
    }

    @Override
    public int getRowCount() {
        return dataList.size();
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public String getColumnName(int column) {
        return columnNames[column];
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        return dataList.get(rowIndex)[columnIndex];
    }

    /**
     * Thêm một dòng mới lên trên cùng của bảng.
     */
    private void addRow(Object[] rowData) {
        dataList.add(0, rowData); // Thêm vào chỉ số 0 (trên cùng)
        if (dataList.size() > MAX_ROWS) {
            dataList.remove(dataList.size() - 1); // Xóa dòng cũ nhất ở cuối
        }
        // Thông báo cho JTable biết dữ liệu đã thay đổi để vẽ lại
        fireTableDataChanged();
    }

    @Override
    public void onReading(Reading r) {
        // Ghi log bình thường
        addRow(new Object[]{
            r.getFormattedTimestamp(), 
            r.getSensorId(), 
            String.format("%.1f", r.getValue()), 
            "Bình thường"
        });
    }

    @Override
    public void onAlert(Alert a) {
        // Ghi đè log cảnh báo lên đầu (hoặc ghi thêm)
        addRow(new Object[]{
            a.getFormattedTimestamp(), 
            a.getSensorName() + " (" + a.getSensorId() + ")", 
            String.format("%.1f", a.getAbnormalValue()), 
            "⚠ " + a.getMessage()
        });
    }
}
