package sdu.bin;

/**
 * @Author: bin
 * @Date: 2020/4/27 14:16
 * @Description:
 */

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;

public class DXFEllipse extends DXFEntity {
    private RealPoint center;
    private RealPoint majorAxisEndpoint;
    private double axisRatio;
    private double startParameter;
    private double endParameter;
    private boolean isCounterclockwise;
    private Color color;
    private BasicStroke stroke;

    public DXFEllipse(RealPoint center, RealPoint majorAxisEndpoint, double axisRatio, double startParameter, double endParameter, boolean isCounterclockwise, Graphics2D graphics) {
        this.center = new RealPoint(center);
        this.majorAxisEndpoint = new RealPoint(majorAxisEndpoint);
        this.axisRatio = axisRatio;
        this.startParameter = startParameter;
        this.endParameter = endParameter;
        this.isCounterclockwise = isCounterclockwise;
        this.color = graphics.getColor();
        this.stroke = (BasicStroke)graphics.getStroke();
    }

    public String toDXFString() {
        String result = "0\nELLIPSE\n";
        result = result + super.toDXFString();
        result = result + "100\nAcDbEllipse\n";
        result = result + "10\n" + this.center.x + "\n";
        result = result + "20\n" + this.center.y + "\n";
        result = result + "30\n" + this.center.z + "\n";
        result = result + "11\n" + this.majorAxisEndpoint.x + "\n";
        result = result + "21\n" + this.majorAxisEndpoint.y + "\n";
        result = result + "31\n" + this.majorAxisEndpoint.z + "\n";
        result = result + "40\n" + this.axisRatio + "\n";
        result = result + "41\n" + this.startParameter + "\n";
        result = result + "42\n" + this.endParameter + "\n";
        result = result + "370\n" + this.getDXFLineWeight(this.stroke.getLineWidth()) + "\n";
        result = result + "62\n" + DXFColor.getClosestDXFColor(this.color.getRGB()) + "\n";
        return result;
    }

    public String getDXFHatchInfo() {
        String result = "72\n3\n";
        result = result + "10\n" + this.center.x + "\n";
        result = result + "20\n" + this.center.y + "\n";
        result = result + "11\n" + this.majorAxisEndpoint.x + "\n";
        result = result + "21\n" + this.majorAxisEndpoint.y + "\n";
        result = result + "40\n" + this.axisRatio + "\n";
        double startAngle = this.findHatchAngleDegrees(this.startParameter);

        double endAngle;
        for(endAngle = this.findHatchAngleDegrees(this.endParameter); startAngle < 0.0D; startAngle += 360.0D) {
            ;
        }

        while(endAngle < 0.0D) {
            endAngle += 360.0D;
        }

        if (startAngle >= 360.0D) {
            startAngle -= 360.0D;
        }

        if (endAngle >= 360.0D) {
            endAngle -= 360.0D;
        }

        if (this.isCounterclockwise && endAngle < startAngle) {
            endAngle += 360.0D;
        }

        if (!this.isCounterclockwise) {
            double temp = startAngle;
            startAngle = endAngle;
            endAngle = temp;
        }

        result = result + "50\n" + startAngle + "\n";
        result = result + "51\n" + endAngle + "\n";
        if (this.isCounterclockwise) {
            result = result + "73\n1\n";
        } else {
            result = result + "73\n0\n";
        }

        return result;
    }

    private double findHatchAngleDegrees(double parameter) {
        double x = Math.cos(parameter);
        double y = this.axisRatio * Math.sin(parameter);
        double angle;
        if (x == 0.0D) {
            if (y > 0.0D) {
                angle = 1.5707963267948966D;
            } else {
                angle = 4.71238898038469D;
            }
        } else {
            angle = Math.atan(Math.abs(y / x));
        }

        if (x < 0.0D && y < 0.0D) {
            angle += 3.141592653589793D;
        } else if (x < 0.0D && y > 0.0D) {
            angle = 3.141592653589793D - angle;
        } else if (x > 0.0D && y < 0.0D) {
            angle = 6.283185307179586D - angle;
        }

        return angle * 180.0D / 3.141592653589793D;
    }
}
