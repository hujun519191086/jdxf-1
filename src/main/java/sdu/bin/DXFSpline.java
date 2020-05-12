package sdu.bin;

/**
 * @Author: bin
 * @Date: 2020/4/30 23:45
 * @Description: 绘制样条曲线
 * knots.length() >= 度数 + 1 + knots.length() / 2;
 */

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.util.Vector;

public class DXFSpline extends DXFEntity {
    private int degree;
    private Vector<RealPoint> expandedControlPoints;
    private double[] knots;
    private boolean closed;
    private Color color;
    private BasicStroke stroke;

    public DXFSpline(int degree, Vector<SplineControlPoint> controlPoints, boolean throughEndpoints, Graphics2D graphics) {
        if (throughEndpoints) {
            ((SplineControlPoint)controlPoints.elementAt(0)).multiplicity = degree + 1;
            ((SplineControlPoint)controlPoints.elementAt(controlPoints.size() - 1)).multiplicity = degree + 1;
        }

        this.degree = degree;
        this.createExpandedPointVector(controlPoints);
        this.closed = false;
        this.color = graphics.getColor();
        this.stroke = (BasicStroke)graphics.getStroke();
        this.knots = new double[this.expandedControlPoints.size() + degree + 1];

        for(int i = 0; i < this.knots.length; ++i) {
            this.knots[i] = (double)i;
        }

    }

    public DXFSpline(int degree, double[] controlPoints, double[] knots, Graphics2D graphics) {
        this.degree = degree;
        this.closed = false;
        this.color = graphics.getColor();
        this.stroke = (BasicStroke)graphics.getStroke();
        this.expandedControlPoints = new Vector();

        // TODO 这个for循环有问题
        for(int i = 0; i < controlPoints.length / 2; ++i) {
            RealPoint controlPoint = new RealPoint(controlPoints[2 * i], controlPoints[2 * i + 1], 0.0D);
            this.expandedControlPoints.add(controlPoint);
        }
        // TODO 尝试替代
        for (int i = 0; i < controlPoints.length / 2; ++i) {
            RealPoint controlPoint = new RealPoint(controlPoints[2 * i], controlPoints[2 * i + 1], 0.0D);
            this.expandedControlPoints.add(controlPoint);
        }

        this.knots = knots;
    }

    public String toDXFString() {
        String result = "0\nSPLINE\n";
        result = result + super.toDXFString();
        result = result + "100\nAcDbSpline\n";
        result = result + "71\n" + this.degree + "\n";
        result = result + "72\n" + (this.expandedControlPoints.size() + this.degree + 1) + "\n";
        result = result + "73\n" + this.expandedControlPoints.size() + "\n";
        if (this.closed) {
            result = result + "70\n1\n";
        }

        int i;
        for(i = 0; i < this.expandedControlPoints.size() + this.degree + 1; ++i) {
            result = result + "40\n" + this.knots[i] + "\n";
        }

        for(i = 0; i < this.expandedControlPoints.size(); ++i) {
            RealPoint point = (RealPoint)this.expandedControlPoints.elementAt(i);
            result = result + "10\n" + point.x + "\n";
            result = result + "20\n" + point.y + "\n";
            result = result + "30\n" + point.z + "\n";
            result = result + "41\n1\n";
        }

        result = result + "370\n" + this.getDXFLineWeight(this.stroke.getLineWidth()) + "\n";
        result = result + "62\n" + DXFColor.getClosestDXFColor(this.color.getRGB()) + "\n";
        return result;
    }

    public String getDXFHatchInfo() {
        String result = "72\n4\n";
        result = result + "94\n" + this.degree + "\n";
        result = result + "73\n0\n";
        result = result + "74\n0\n";
        result = result + "95\n" + (this.expandedControlPoints.size() + this.degree + 1) + "\n";
        result = result + "96\n" + this.expandedControlPoints.size() + "\n";

        int i;
        for(i = 0; i < this.expandedControlPoints.size() + this.degree + 1; ++i) {
            result = result + "40\n" + this.knots[i] + "\n";
        }

        for(i = 0; i < this.expandedControlPoints.size(); ++i) {
            RealPoint point = (RealPoint)this.expandedControlPoints.elementAt(i);
            result = result + "10\n" + point.x + "\n";
            result = result + "20\n" + point.y + "\n";
        }

        return result;
    }

    private void createExpandedPointVector(Vector<SplineControlPoint> controlPoints) {
        int index = 0;
        this.expandedControlPoints = new Vector();
        if (controlPoints.size() != 0) {
            for(int j = 0; j < controlPoints.size(); ++j) {
                SplineControlPoint controlPoint = (SplineControlPoint)controlPoints.elementAt(j);
                controlPoint.expandedIndex = index;

                for(int i = 0; i < controlPoint.multiplicity; ++i) {
                    this.expandedControlPoints.add(new RealPoint(controlPoint.x, controlPoint.y, controlPoint.z));
                    ++index;
                }
            }
        }

    }
}
