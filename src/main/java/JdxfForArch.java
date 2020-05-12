
import sdu.bin.DXFGraphics;

import java.awt.*;
import java.awt.geom.Ellipse2D;

/**
 * @Author: bin
 * @Date: 2020/3/5 16:49
 * @Description:
 */
public class JdxfForArch {

    /**
     * TODO 踩坑
     * graphics.fillOval(x - r, -(y - r), r * 2, r * 2); 方法无效
     * 自己实现的方法 drawCircle  -> 绘制圆环时，有时会莫名其妙的无法呈现在画布上
     */
    // 锚杆的半径是 5
    private static final int STICK_RADIUS = 5;
    // 锚索的内直径 8
    private static final int ANCHOR_CABLE_INNER_DIAMETER = 8;
    // 锚索的外直径 15
    private static final int ANCHOR_CABLE_OUTTER_DIAMETER = 15;
    // 钢带的厚度 12
    private static final int BELT_THICKNESS = 12;
    // 箭头顶角度数 9
    private static final int ARROW_VERTEX_ANGLE = 9;
    // 箭头长边长 26
    private static final int ARROW_SIDE_LENGTH = 26;


    /**
     * 钢带 中心线的 起止点
     * @param qiDianX
     * @param qiDianY
     * @param zhongDianX
     * @param zhongDianY
     */
    public static void drawGangDai(double qiDianX, double qiDianY, double zhongDianX, double zhongDianY) {
        // TODO 绘制钢带
    }
    /**
     * 圆环   内径+厚度=外径
     * 有时会莫名其妙的无法呈现在画布上
     * @param graphics
     * @param x 圆心x
     * @param y 圆心y
     * @param r 半径（内径）
     * @param thickness 厚度
     */
    public static void drawCircle(DXFGraphics graphics, double x, double y, double r, double thickness, Color backColor, Color frontColor) {
        // 设置 背景 颜色
        graphics.setColor(backColor);
        // 填满大圆
        Ellipse2D round = new Ellipse2D.Double(x - r - thickness, -(y + r + thickness), (r + thickness) * 2, (r + thickness) * 2);
        graphics.fill(round);
        // 设置 前端 颜色
        graphics.setColor(frontColor);
        // 填满小圆
        Ellipse2D round2 = new Ellipse2D.Double(x - r, -(y + r), r * 2, r * 2);
        graphics.fill(round2);

    }


    /**
     * 用线标注某个线段或曲线
     * TODO 这个方法最好可以指定 线的起始位置(可用0-1指定距离左侧点的距离)和指向
     * @param graphics
     * @param x0 被标注曲线的起止点坐标
     * @param y0
     * @param x1
     * @param y1
     * @param text
     */
    public static void markLine(DXFGraphics graphics, double x0, double y0, double x1, double y1, String text) {
        // 默认向左上45度角划去 -135
        final int direction = -135;
        double directionAng = direction * Math.PI / 180;
        // 长度默认为
        int length = 99;
        // 标注线段的起点默认为指向线段(弧)的中间
        double x2 = (x0 + x1) / 2, y2 = (y0 + y1) / 2;
        // 标注线段的终点
        double x3 = x2 + Math.cos(directionAng) * length, y3 = y2 + Math.sin(directionAng) * length;
        // 绘制标注线
        graphics.drawLine(x2, -y2, x3, -y3);
        // 填写标注文字
        graphics.drawString(text, x3, -y3);
    }
    /**
     * 使用某种颜色填充多边形，填充完成后，将画笔颜色恢复
     * @param graphics
     * @param pointsX
     * @param pointsY
     * @param color
     */
    public static void fillPolygon(DXFGraphics graphics, double[] pointsX, double[] pointsY, Color color) {
        /**
         * 最好的方法是继承{@link Paint} ,使用
         * @see    com.jsevy.jdxf.DXFGraphics #setPaint()
         * 所以应继承 {@link Color} 实现画网格线的功能
         */
        Color preColor = graphics.getColor();
        graphics.setColor(color);
        fillPolygon(graphics, pointsX, pointsY);
        graphics.setColor(preColor);
    }


    /**
     * 画带双箭头的弧，一般用于指出两线之间的夹角  双箭头向外指
     * x, y 用于指示圆心位置 （两线的交点）
     * r 用于指示半径
     * startAngle 弧的起止角度
     * endAngle
     */
    public static void paintArc(DXFGraphics graphics, double x, double y, double r,
                                int startAngle, int endAngle) {
        int radius = 137;  // 默认半径
        System.out.println("圆心：" + x + " " + y + " 半径： " + r);
        // 画弧
        graphics.drawArc(x - r, -(r + y), 2 * r, 2 * r, startAngle, endAngle - startAngle);
        double startAng = startAngle * Math.PI / 180;
        double endAng = endAngle * Math.PI / 180;
        // 画两个箭头
        double x1 = x + (Math.cos(startAng) * r), y1 = y + (Math.sin(startAng) * r);
        paintArrow(graphics, x1, y1, startAngle - 90);
        System.out.println("顶点坐标：" + (x + (Math.cos(startAng) * r)) + ", " +  (y + (Math.sin(startAng) * r)) + " 箭尾指向：" + (180 - startAngle));
        double x2 = x + (Math.cos(endAng) * r), y2 = y + (Math.sin(endAng) * r);
        paintArrow(graphics, x2, y2, endAngle + 90);
        System.out.println("顶点坐标：" + (x + (Math.cos(endAng) * r)) + ", " +  (y + (Math.sin(endAng) * r)) + " 箭尾指向：" + endAngle);
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
    /**
     * 绘制弧上的标注线
     * @param graphics
     * 画带双箭头的弧，一般用于指出两线之间的夹角
     * x, y 用于指示圆心位置 （两线的交点）
     * r 用于指示半径
     * startAngle 弧的起止角度
     * endAngle
     */
    public static void paintMarkingLineOfArc(DXFGraphics graphics, double x, double y, double r,
                                             int startAngle, int endAngle, String markString) {
        // 求得弧的起止点坐标
        Point2D startPoint = getArcCrossPoint(x, y, r, startAngle);
        Point2D endPoint = getArcCrossPoint(x, y, r, endAngle);
        // 取得两点中心，并画标注线
        paintMarkingLine(graphics, (startPoint.x + endPoint.x) / 2, (startPoint.y + endPoint.y) /2, markString);
    }

    /**
     * 由圆心，半径，直线的方向（直线过圆心），确定交点
     * @param x 圆心x
     * @param y 圆心y
     * @param r 半径
     * @param direction 直线方向（度）
     * @return
     */
    public static Point2D getArcCrossPoint(double x, double y, double r, double direction) {
        double directionAng = direction * Math.PI / 180;
        double x1 = x + Math.cos(directionAng) * r, y1 = y + Math.sin(directionAng) * r;
        return new Point2D(x1, y1);
    }


    /**
     * 由圆心，半径，直线的方向（直线过圆心），确定交点
     * @param p 圆心
     * @param r 半径
     * @param direction 直线方向（度）
     * @return
     */
    public static Point2D getArcCrossPoint(Point2D p, double r, double direction) {
        return getArcCrossPoint(p.x, p.y, r, direction);
    }

    // 画标识线，标识线的起始坐标， 标识文字
    public static void paintMarkingLine(Graphics graphics, double x, double y, String text) {
        // 标识线一般成130~140度， 长度40~140不等
        int lineLength = 45;
        // TODO
//        graphics.drawLine(x, -y, lineEndX, -lineEndY);
    }

    /**
     * 画箭头 x,y 箭头顶点坐标  angle 箭头尾的指向(度)
     * @param graphics
     * @param x
     * @param y
     * @param angle
     */
    public static void paintArrow(DXFGraphics graphics, double x, double y, int angle) {
        double direction = angle * Math.PI / 180;
        paintArrow(graphics, x, y, direction);
    }


    /**
     * 画箭头 x,y 箭头顶点坐标  direction 箭头尾的指向(弧度)
     * @param graphics
     * @param x
     * @param y
     * @param direction
     */
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
    public static void fillPolygon(DXFGraphics graphics, double[] pointsX, double[] pointsY) {
        // 反转 y轴 坐标
        for (int i = 0; i < pointsY.length; i ++) {
            pointsY[i] = -pointsY[i];
        }
        PrecisePolygon polygon = new PrecisePolygon(pointsX, pointsY);
        // 将绘制好的多边形作为入参
        graphics.fill(polygon);
    }

    /**
     * 绘制多边形,
     * 1、pointsX与 pointsY的长度必须一致
     * 2、各点的坐标必须按照顺序填写。（即：不保证为凸多边形）
     * @param graphics
     * @param pointsX
     * @param pointsY
     */
    public static void drawPolygon(DXFGraphics graphics, double[] pointsX, double[] pointsY) {
        if (pointsX.length != pointsY.length) {
            throw new RuntimeException("pointsX与 pointsY的长度必须一致");
        }
        if (pointsX.length < 3) {
            throw new RuntimeException("不足3个点无法构成多边形");
        }
        for (int i = 0; i < pointsX.length - 1; i ++) {
            graphics.drawLine(pointsX[i], pointsY[i], pointsX[i + 1], pointsY[i + 1]);
        }
        graphics.drawLine(pointsX[0], pointsY[0], pointsX[pointsX.length - 1], pointsY[pointsX.length - 1]);
    }

    public static void drawLine(DXFGraphics graphics, double x1, double y1, double x2, double y2) {
        graphics.drawLine(x1, -y1, x2, -y2);
    }

}
