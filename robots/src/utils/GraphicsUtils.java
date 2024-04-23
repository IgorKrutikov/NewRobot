package utils;

import java.awt.*;

public class GraphicsUtils {
    public static double getDPICorrectCoordinate(double coordinate){
        double dpi = Toolkit.getDefaultToolkit().getScreenResolution();

        return coordinate * dpi / 96;

    }

    public static void fillOval(Graphics g, int centerX, int centerY, int diam1, int diam2)
    {
        g.fillOval(centerX - diam1 / 2, centerY - diam2 / 2, diam1, diam2);
    }

    public static void drawOval(Graphics g, int centerX, int centerY, int diam1, int diam2)
    {
        g.drawOval(centerX - diam1 / 2, centerY - diam2 / 2, diam1, diam2);
    }
}
