import sdu.bin.DXFColor;
import sdu.bin.DXFDocument;
import sdu.bin.DXFGraphics;

import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.io.FileWriter;

/**
 * @Author: bin
 * @Date: 2020/4/27 14:21
 * @Description:
 */
public class Test {
    public static void main(String[] args) throws Exception {

        /* Create a DXF document and get its associated DXFGraphics instance */

        DXFDocument dxfDocument = new
                DXFDocument("Example");
        DXFGraphics dxfGraphics =
                dxfDocument.getGraphics();

        /* Do drawing commands as on any other Graphics. If you have a paint(Graphics) method, you can just use it with the DXFGraphics instance since it's a subclass of Graphics. */
        paintArrowLine(dxfGraphics, 23.45, 34.56, 45.67, 56.78, "adfadas");

        dxfGraphics.setColor(new DXFColor(DXFColor.Pattern.ACAD_ISO12W100));
        dxfGraphics.fillRoundRect(100, 100, 200, 300, 10, 20);
        dxfGraphics.setColor(new Color(128, 128, 0));

        double[] xPoints = {-109, 10, 109, 221, 365};
        double[] yPoints = {-109, 10, 109, 221, 365};
        dxfGraphics.drawSpline(3, xPoints, yPoints);

//        test222(dxfGraphics);
        /* Get the DXF output as a string - it's just text - and  save  in a file for use with a CAD package */
        String stringOutput = dxfDocument.toDXFString();
        String filePath = "D:\\home\\test\\tttt3.dxf";
        FileWriter fileWriter = new FileWriter(filePath);
        fileWriter.write(stringOutput.toCharArray());
        fileWriter.flush();
        fileWriter.close();
    }

    public static void paintArrowLine(DXFGraphics graphics, double x0, double y0, double x1, double y1, String markString) {
        paintArrowLine(graphics, x0, y0, x1, y1);
        graphics.drawString(markString, (x0 + x1) / 2, -(y0 + y1) / 2);
    }

    /**
     * 填满圆
     * @param graphics
     * @param x 圆心x
     * @param y 圆心y
     * @param r 半径
     */
    public static void fillRoundPoint(DXFGraphics graphics, double x, double y, double r) {
        // 填满椭圆
        Ellipse2D round = new Ellipse2D.Double(x - r, -(y - r), r * 2, r * 2);
        graphics.fill(round);
    }
    /**
     * 画 带有箭头的直线 箭头指向外
     * @param graphics
     * @param x0
     * @param y0
     * @param x1
     * @param y1
     */
    public static void paintArrowLine(DXFGraphics graphics, double x0, double y0, double x1, double y1) {
        // 画普通直线
        graphics.drawLine(x0, -y0, x1, -y1);
        // 求得直线的方向
        double directionAng = Math.atan((y1 - y0) / (x1 - x0));
        // 绘制两个箭头
        paintArrow(graphics, x0, y0, directionAng);
        paintArrow(graphics, x1, y1, directionAng + Math.PI);
    }

    private static void paintArrow(DXFGraphics graphics, double x, double y, double direction) {
        // 箭头中线的长度  箭头长边长
        int lineLength = 38, sideLength = 26;
        double arrowTopAng = 9 * Math.PI / 180;
        // 求得线的顶端坐标
        double lineEndX = x + (Math.cos(direction) * lineLength),
                lineEndY = y + (Math.sin(direction) * lineLength);
        // 画线 箭尾
        graphics.drawLine(x, -y, lineEndX, -lineEndY);
        // 填满多边形
        double x1 = x + (Math.cos(direction - arrowTopAng) * sideLength),
                x2 = x + (Math.cos(direction + arrowTopAng) * sideLength),
                y1 = y + (Math.sin(direction - arrowTopAng) * sideLength),
                y2 = y + (Math.sin(direction + arrowTopAng) * sideLength);
        // 绘制多边形
        PrecisePolygon polygon = new PrecisePolygon(new double[]{x, x1, x2}, new double[]{-y, -y1, -y2});
        // 将绘制好的多边形作为入参
        graphics.fill(polygon);
    }
}
