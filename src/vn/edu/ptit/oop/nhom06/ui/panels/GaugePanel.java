package vn.edu.ptit.oop.nhom06.ui.panels;

import java.awt.*;
import java.awt.geom.Arc2D;
import java.awt.geom.Line2D;
import java.util.Locale;
import java.util.Objects;
import javax.swing.*;
import vn.edu.ptit.oop.nhom06.model.Alert;
import vn.edu.ptit.oop.nhom06.model.Reading;
import vn.edu.ptit.oop.nhom06.model.SensorHub;
import vn.edu.ptit.oop.nhom06.model.SensorListener;
import vn.edu.ptit.oop.nhom06.util.PropertiesHelper;

/** TV8: đồng hồ tự vẽ, lắng nghe dữ liệu qua Observer Pattern. */
public class GaugePanel extends JPanel implements SensorListener {
    private static final long serialVersionUID = 1L;
    private static final Color GREEN = new Color(24, 135, 91);
    private static final Color YELLOW = new Color(176, 115, 0);
    private static final Color RED = new Color(204, 54, 65);
    private static final Color GRAY = new Color(103, 119, 137);
    // Vàng: trong 10% bề rộng khoảng an toàn, sát một trong hai ngưỡng.
    private static final double WARNING_RATIO = 0.10;
    private final String sensorId;
    private final String title;
    private double minSafe;
    private double maxSafe;
    private double currentValue;
    private boolean hasReading;
    private Color currentColor = GRAY;
    private String status = "Chờ dữ liệu";

    /** Giữ chữ ký constructor để TV1 tích hợp. */
    public GaugePanel(String sensorId, String title, double minSafe, double maxSafe) {
        if (!validThresholds(minSafe, maxSafe)) {
            throw new IllegalArgumentException("Ngưỡng phải hữu hạn và Min < Max");
        }
        this.sensorId = Objects.requireNonNull(sensorId, "sensorId");
        this.title = Objects.requireNonNull(title, "title");
        this.minSafe = minSafe;
        this.maxSafe = maxSafe;
        setPreferredSize(new Dimension(360, 250));
        setMinimumSize(new Dimension(240, 170));
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createLineBorder(new Color(220, 228, 235)));
        getAccessibleContext().setAccessibleName(title);
        updateDescription();
    }
    private static boolean validThresholds(double min, double max) {
        return Double.isFinite(min) && Double.isFinite(max) && min < max
                && Double.isFinite(max - min);
    }
    /** Đăng ký khi panel được gắn vào giao diện, gỡ đăng ký khi bị tháo. */
    @Override public void addNotify() {
        super.addNotify();
        SensorHub.getInstance().addSensorListener(this);
    }
    @Override public void removeNotify() {
        SensorHub.getInstance().removeSensorListener(this);
        super.removeNotify();
    }
    @Override public void onReading(Reading reading) {
        if (reading != null && sensorId.equals(reading.getSensorId())) {
            updateOnEdt(reading.getValue());
        }
    }
    @Override public void onAlert(Alert alert) {
        if (alert != null && sensorId.equals(alert.getSensorId())) {
            // Cùng dùng ngưỡng Properties với Engine, tránh tính màu khác nhau.
            updateOnEdt(alert.getAbnormalValue());
        }
    }
    private void updateOnEdt(double value) {
        if (!SwingUtilities.isEventDispatchThread()) {
            SwingUtilities.invokeLater(() -> updateOnEdt(value));
            return;
        }
        currentValue = value;
        hasReading = Double.isFinite(value);
        try {
            double min = PropertiesHelper.getMinThreshold(sensorId, minSafe);
            double max = PropertiesHelper.getMaxThreshold(sensorId, maxSafe);
            if (!validThresholds(min, max)) {
                throw new IllegalArgumentException("Min/Max không hợp lệ");
            }
            // Cập nhật cả thang đo và màu sau khi TV2 đổi cấu hình.
            minSafe = min;
            maxSafe = max;
            if (!hasReading) {
                currentColor = GRAY;
                status = "Dữ liệu không hợp lệ";
            } else if (value < minSafe || value > maxSafe) {
                currentColor = RED;
                status = "Cảnh báo: vượt ngưỡng";
            } else if (value - minSafe <= (maxSafe - minSafe) * WARNING_RATIO
                    || maxSafe - value <= (maxSafe - minSafe) * WARNING_RATIO) {
                currentColor = YELLOW;
                status = "Chú ý: gần ngưỡng";
            } else {
                currentColor = GREEN;
                status = "An toàn";
            }
        } catch (IllegalArgumentException ex) {
            currentColor = GRAY;
            status = "Lỗi cấu hình ngưỡng";
            setToolTipText(ex.getMessage());
            updateDescription();
            repaint();
            return;
        }
        setToolTipText(title + ": " + status);
        updateDescription();
        repaint();
    }
    private void updateDescription() {
        getAccessibleContext().setAccessibleDescription(title + ": "
                + (hasReading ? format(currentValue) : "--") + "; " + status);
    }
    private static String format(double value) {
        return String.format(Locale.ROOT, "%.2f", value);
    }
    /** Đa hình: Swing gọi phiên bản paintComponent của GaugePanel. */
    @Override protected void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);
        Graphics2D g = (Graphics2D) graphics.create();
        try {
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);
            // Co giãn đồng đều bản vẽ 360 x 250 để cung tròn không bị méo.
            double scale = Math.min(getWidth() / 360.0, getHeight() / 250.0);
            if (scale <= 0) return;
            g.translate((getWidth() - 360 * scale) / 2, (getHeight() - 250 * scale) / 2);
            g.scale(scale, scale);
            g.setColor(new Color(36, 57, 76));
            centeredText(g, title, 27, 17, Font.BOLD);
            g.setStroke(new BasicStroke(14, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
            g.setColor(new Color(231, 237, 243));
            g.draw(new Arc2D.Double(80, 50, 200, 200, 180, -180, Arc2D.OPEN));
            double span = maxSafe - minSafe;
            double displayMin = minSafe - span * 0.5;
            double displayMax = maxSafe + span * 0.5;
            if (hasReading) {
                double fraction = Math.max(0, Math.min(1,
                        (currentValue - displayMin) / (displayMax - displayMin)));
                g.setColor(currentColor);
                g.draw(new Arc2D.Double(80, 50, 200, 200, 180, -180 * fraction, Arc2D.OPEN));
                double angle = Math.PI * (1 - fraction);
                g.setStroke(new BasicStroke(3, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                g.draw(new Line2D.Double(180, 150,
                        180 + 84 * Math.cos(angle), 150 - 84 * Math.sin(angle)));
                g.fillOval(175, 145, 10, 10);
            }
            g.setColor(GRAY);
            g.setFont(new Font("SansSerif", Font.PLAIN, 11));
            g.drawString(format(displayMin), 54, 169);
            String high = format(displayMax);
            g.drawString(high, 306 - g.getFontMetrics().stringWidth(high), 169);
            g.setColor(new Color(36, 57, 76));
            centeredText(g, hasReading ? format(currentValue) : "--", 190, 29, Font.BOLD);
            g.setColor(currentColor);
            centeredText(g, status, 215, 14, Font.BOLD);
            g.setColor(GRAY);
            centeredText(g, "Ngưỡng an toàn: " + format(minSafe) + " – " + format(maxSafe),
                    237, 12, Font.PLAIN);
        } finally {
            g.dispose(); // Không làm thay đổi Graphics gốc của Swing.
        }
    }
    private void centeredText(Graphics2D g, String text, int y, int size, int style) {
        g.setFont(new Font("SansSerif", style, size));
        g.drawString(text, (360 - g.getFontMetrics().stringWidth(text)) / 2, y);
    }
}
