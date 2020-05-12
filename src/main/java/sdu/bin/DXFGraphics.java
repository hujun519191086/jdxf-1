package sdu.bin;

/**
 * @Author: bin
 * @Date: 2020/4/27 14:05
 * @Description:
 */

import java.awt.BasicStroke;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.Paint;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.RenderingHints.Key;
import java.awt.font.FontRenderContext;
import java.awt.font.GlyphVector;
import java.awt.geom.AffineTransform;
import java.awt.geom.Arc2D;
import java.awt.geom.CubicCurve2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.PathIterator;
import java.awt.geom.QuadCurve2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;
import java.awt.geom.Rectangle2D.Double;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.awt.image.ImageObserver;
import java.awt.image.RenderedImage;
import java.awt.image.renderable.RenderableImage;
import java.text.AttributedCharacterIterator;
import java.util.Map;
import java.util.Vector;

public class DXFGraphics extends Graphics2D {
    private DXFDocument dxfDocument;
    private Color color;
    private Color backColor;
    private DXFGraphics.PaintMode paintMode;
    private Font font;
    private BasicStroke stroke;
    private AffineTransform javaTransformMatrix;
    private AffineTransform javaToDXFGraphicsMatrix;
    private AffineTransform graphicsMatrix;

    public DXFGraphics(DXFDocument dxfDocument) {
        this.dxfDocument = dxfDocument;
        this.color = Color.BLACK;
        this.backColor = Color.WHITE;
        this.paintMode = DXFGraphics.PaintMode.PAINT_MODE;
        this.font = new Font("SansSerif", 0, 12);
        this.stroke = new BasicStroke();
        this.javaTransformMatrix = new AffineTransform();
        this.javaToDXFGraphicsMatrix = new AffineTransform();
        this.javaToDXFGraphicsMatrix.scale(1.0D, -1.0D);
        this.setMatrix();
    }

    public DXFGraphics create() {
        DXFGraphics newGraphics = new DXFGraphics(this.dxfDocument);
        newGraphics.color = this.color;
        newGraphics.backColor = this.backColor;
        newGraphics.paintMode = this.paintMode;
        newGraphics.font = this.font;
        newGraphics.stroke = this.stroke;
        newGraphics.javaTransformMatrix = new AffineTransform(this.javaTransformMatrix);
        newGraphics.javaToDXFGraphicsMatrix = new AffineTransform(this.javaToDXFGraphicsMatrix);
        newGraphics.graphicsMatrix = new AffineTransform(this.graphicsMatrix);
        return newGraphics;
    }

    public DXFGraphics create(int x, int y, int width, int height) {
        DXFGraphics graphics = this.create();
        if (graphics == null) {
            return null;
        } else {
            graphics.translate(x, y);
            return graphics;
        }
    }

    private void setMatrix() {
        this.graphicsMatrix = new AffineTransform(this.javaToDXFGraphicsMatrix);
        this.graphicsMatrix.concatenate(this.javaTransformMatrix);
    }

    public Color getColor() {
        return this.color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public void setPaintMode() {
        this.paintMode = DXFGraphics.PaintMode.PAINT_MODE;
    }

    public void setXORMode(Color xorColor) {
        this.paintMode = DXFGraphics.PaintMode.XOR_MODE;
    }

    public Font getFont() {
        return this.font;
    }

    public void setFont(Font font) {
        if (font != null) {
            this.font = font;
        }

    }

    public FontMetrics getFontMetrics() {
        return this.getFontMetrics(this.getFont());
    }

    public FontMetrics getFontMetrics(Font f) {
        Canvas c = new Canvas();
        return c.getFontMetrics(f);
    }

    public Rectangle getClipBounds() {
        return null;
    }

    public void clipRect(int x, int y, int width, int height) {
    }

    public void setClip(int x, int y, int width, int height) {
    }

    public Shape getClip() {
        return null;
    }

    public void setClip(Shape clip) {
    }

    public void copyArea(int x, int y, int width, int height, int dx, int dy) {
        throw new UnsupportedOperationException("copyArea not supported");
    }

    public void drawLine(int x1, int y1, int x2, int y2) {
        this.drawLine((double)x1, (double)y1, (double)x2, (double)y2);
    }

    public void drawLine(double x1, double y1, double x2, double y2) {
        DXFLine line = this.createDXFLine(x1, y1, x2, y2);
        this.dxfDocument.addEntity(line);
    }

    private DXFLine createDXFLine(double x1, double y1, double x2, double y2) {
        double[] points = new double[]{x1, y1, x2, y2};
        this.graphicsMatrix.transform(points, 0, points, 0, 2);
        return new DXFLine(new RealPoint(points[0], points[1], 0.0D), new RealPoint(points[2], points[3], 0.0D), this);
    }

    public void fillRect(int x, int y, int width, int height) {
        this.fillRect((double)x, (double)y, (double)width, (double)height);
    }

    public void fillRect(double x, double y, double width, double height) {
        Rectangle2D rect = new Double(x, y, width, height);
        this.fill(rect);
    }

    public void drawRect(int x, int y, int width, int height) {
        this.drawRect((double)x, (double)y, (double)width, (double)height);
    }

    public void drawRect(double x, double y, double width, double height) {
        double[] pts = new double[]{x, y, x + width, y, x + width, y + height, x, y + height};
        this.graphicsMatrix.transform(pts, 0, pts, 0, 4);
        Vector<RealPoint> vertices = new Vector();
        vertices.add(new RealPoint(pts[0], pts[1], 0.0D));
        vertices.add(new RealPoint(pts[2], pts[3], 0.0D));
        vertices.add(new RealPoint(pts[4], pts[5], 0.0D));
        vertices.add(new RealPoint(pts[6], pts[7], 0.0D));
        DXFLWPolyline polyline = new DXFLWPolyline(4, vertices, true, this);
        this.dxfDocument.addEntity(polyline);
    }

    public void clearRect(int x, int y, int width, int height) {
        this.clearRect((double)x, (double)y, (double)width, (double)height);
    }

    public void clearRect(double x, double y, double width, double height) {
        Rectangle2D rect = new Double(x, y, width, height);
        this.clear(rect);
    }

    public void drawRoundRect(int x, int y, int width, int height, int arcWidth, int arcHeight) {
        this.drawRoundRect((double)x, (double)y, (double)width, (double)height, (double)arcWidth, (double)arcHeight);
    }

    public void drawRoundRect(double x, double y, double width, double height, double arcWidth, double arcHeight) {
        double right = x + width;
        double bottom = y + height;
        double rx = arcWidth / 2.0D;
        double ry = arcHeight / 2.0D;
        this.drawLine(x + rx, y, right - rx, y);
        this.drawArc(right - 2.0D * rx, y, arcWidth, arcHeight, 90.0D, -90.0D);
        this.drawLine(right, y + ry, right, bottom - ry);
        this.drawArc(right - 2.0D * rx, bottom - 2.0D * ry, arcWidth, arcHeight, 0.0D, -90.0D);
        this.drawLine(right - rx, bottom, x + rx, bottom);
        this.drawArc(x, bottom - 2.0D * ry, arcWidth, arcHeight, 270.0D, -90.0D);
        this.drawLine(x, bottom - ry, x, y + ry);
        this.drawArc(x, y, arcWidth, arcHeight, 180.0D, -90.0D);
    }

    public void fillRoundRect(int x, int y, int width, int height, int arcWidth, int arcHeight) {
        this.fillRoundRect((double)x, (double)y, (double)width, (double)height, (double)arcWidth, (double)arcHeight);
    }

    public void fillRoundRect(double x, double y, double width, double height, double arcWidth, double arcHeight) {
        Vector<DXFEntity> boundary = new Vector();
        double right = x + width;
        double bottom = y + height;
        double rx = arcWidth / 2.0D;
        double ry = arcHeight / 2.0D;
        boundary.add(this.createDXFEllipticalArc(right - 2.0D * rx, y, arcWidth, arcHeight, 0.0D, 90.0D));
        boundary.add(this.createDXFLine(right - rx, y, x + rx, y));
        boundary.add(this.createDXFEllipticalArc(x, y, arcWidth, arcHeight, 90.0D, 90.0D));
        boundary.add(this.createDXFLine(x, y + ry, x, bottom - ry));
        boundary.add(this.createDXFEllipticalArc(x, bottom - 2.0D * ry, arcWidth, arcHeight, 180.0D, 90.0D));
        boundary.add(this.createDXFLine(x + rx, bottom, right - rx, bottom));
        boundary.add(this.createDXFEllipticalArc(right - 2.0D * rx, bottom - 2.0D * ry, arcWidth, arcHeight, 270.0D, 90.0D));
        boundary.add(this.createDXFLine(right, bottom - ry, right, y + ry));
        Vector<Vector<DXFEntity>> boundaries = new Vector();
        boundaries.add(boundary);
        DXFHatch hatch = new DXFHatch(boundaries, this.color);
        this.dxfDocument.addEntity(hatch);
    }

    public void draw3DRect(int x, int y, int width, int height, boolean raised) {
        super.draw3DRect(x, y, width, height, raised);
    }

    public void fill3DRect(int x, int y, int width, int height, boolean raised) {
        super.fill3DRect(x, y, width, height, raised);
    }

    public void drawOval(int x, int y, int width, int height) {
        this.drawOval((double)x, (double)y, (double)width, (double)height);
    }

    public void drawOval(double x, double y, double width, double height) {
        this.drawArc(x, y, width, height, 0.0D, 360.0D);
    }

    public void fillOval(int x, int y, int width, int height) {
//        this.fillOval((double)x, (double)y, (double)width, (double)height);
    }

    public void fillOval(double x, double y, double width, double height) {
//        Vector<DXFEntity> boundary = new Vector();
//        DXFEllipse dxfEllipse = this.createDXFEllipticalArc(x, y, width, height, 0.0D, 360.0D);
//        boundary.add(dxfEllipse);
//        Vector<Vector<DXFEntity>> boundaries = new Vector();
//        boundaries.add(boundary);
//        DXFHatch hatch = new DXFHatch(boundaries, this.color);
//        this.dxfDocument.addEntity(hatch);
    }

    public void drawArc(int x, int y, int width, int height, int startAngle, int sweepAngle) {
        this.drawArc((double)x, (double)y, (double)width, (double)height, (double)startAngle, (double)sweepAngle);
    }

    public void drawArc(double x, double y, double width, double height, double startAngle, double sweepAngle) {
        DXFEllipse arc = this.createDXFEllipticalArc(x, y, width, height, startAngle, sweepAngle);
        this.dxfDocument.addEntity(arc);
    }

    private DXFEllipse createDXFEllipticalArc(double x, double y, double width, double height, double startAngle, double sweepAngle) {
        double xCen = x + width / 2.0D;
        double yCen = y + height / 2.0D;
        startAngle = -startAngle;
        sweepAngle = -sweepAngle;
        double a = width / 2.0D;
        double b = height / 2.0D;
        double startParameter = startAngle * 0.017453292519943295D;
        double endParameter = (startAngle + sweepAngle) * 0.017453292519943295D;
        double sweepAngleRadians = sweepAngle * 0.017453292519943295D;
        RealPoint center = new RealPoint(xCen, yCen, 0.0D);
        RealPoint startPoint = new RealPoint(xCen + a * Math.cos(startParameter), yCen + b * Math.sin(startParameter), 0.0D);
        RealPoint endPoint = new RealPoint(xCen + a * Math.cos(endParameter), yCen + b * Math.sin(endParameter), 0.0D);
        double[] basisVects = new double[]{1.0D, 0.0D, 0.0D, 1.0D};
        this.mapVectors(this.graphicsMatrix, basisVects);
        double c11 = basisVects[0];
        double c12 = basisVects[2];
        double c21 = basisVects[1];
        double c22 = basisVects[3];
        double A = a * b * (c11 * c12 + c21 * c22);
        double B = a * a * (c11 * c11 + c21 * c21) - b * b * (c12 * c12 + c22 * c22);
        double tangent1;
        if (A != 0.0D) {
            tangent1 = (-B + Math.sqrt(B * B + 4.0D * A * A)) / (2.0D * A);
        } else {
            tangent1 = 0.0D;
        }

        double t1 = Math.atan(tangent1);
        double t2 = t1 + 1.5707963267948966D;
        double D1 = (c11 * a * Math.cos(t1) + c12 * b * Math.sin(t1)) * (c11 * a * Math.cos(t1) + c12 * b * Math.sin(t1)) + (c21 * a * Math.cos(t1) + c22 * b * Math.sin(t1)) * (c21 * a * Math.cos(t1) + c22 * b * Math.sin(t1));
        double D2 = (c11 * a * Math.cos(t2) + c12 * b * Math.sin(t2)) * (c11 * a * Math.cos(t2) + c12 * b * Math.sin(t2)) + (c21 * a * Math.cos(t2) + c22 * b * Math.sin(t2)) * (c21 * a * Math.cos(t2) + c22 * b * Math.sin(t2));
        double majorAxisParameter;
        double minorAxisParameter;
        double majorAxisLength;
        double minorAxisLength;
        if (D1 >= D2) {
            majorAxisParameter = t1;
            minorAxisParameter = t2;
            majorAxisLength = Math.sqrt(D1);
            minorAxisLength = Math.sqrt(D2);
        } else {
            majorAxisParameter = t2;
            minorAxisParameter = t1;
            majorAxisLength = Math.sqrt(D2);
            minorAxisLength = Math.sqrt(D1);
        }

        double transformedAxisRatio = minorAxisLength / majorAxisLength;
        RealPoint transformedCenter = RealPoint.mapPoint(this.graphicsMatrix, center);
        RealPoint transformedMajorAxisRelative = new RealPoint(c11 * a * Math.cos(majorAxisParameter) + c12 * b * Math.sin(majorAxisParameter), c21 * a * Math.cos(majorAxisParameter) + c22 * b * Math.sin(majorAxisParameter), 0.0D);
        RealPoint transformedMinorAxisRelative = new RealPoint(c11 * a * Math.cos(minorAxisParameter) + c12 * b * Math.sin(minorAxisParameter), c21 * a * Math.cos(minorAxisParameter) + c22 * b * Math.sin(minorAxisParameter), 0.0D);
        RealPoint crossProduct = RealPoint.crossProduct(transformedMajorAxisRelative, transformedMinorAxisRelative);
        if (crossProduct.z < 0.0D) {
            transformedMinorAxisRelative = RealPoint.scalarProduct(-1.0D, transformedMinorAxisRelative);
        }

        RealPoint transformedStartPoint = RealPoint.mapPoint(this.graphicsMatrix, startPoint);
        RealPoint transformedEndPoint = RealPoint.mapPoint(this.graphicsMatrix, endPoint);
        RealPoint transformedStartPointRelative = RealPoint.difference(transformedStartPoint, transformedCenter);
        RealPoint transformedEndPointRelative = RealPoint.difference(transformedEndPoint, transformedCenter);
        double uStart = RealPoint.dotProduct(transformedStartPointRelative, transformedMajorAxisRelative) / RealPoint.magnitude(transformedMajorAxisRelative);
        double vStart = RealPoint.dotProduct(transformedStartPointRelative, transformedMinorAxisRelative) / RealPoint.magnitude(transformedMinorAxisRelative);
        double transformedStartParameter;
        if (uStart == 0.0D) {
            if (vStart > 0.0D) {
                transformedStartParameter = 1.5707963267948966D;
            } else {
                transformedStartParameter = -1.5707963267948966D;
            }
        } else {
            transformedStartParameter = Math.atan(vStart * majorAxisLength / (uStart * minorAxisLength));
            if (uStart < 0.0D) {
                transformedStartParameter += 3.141592653589793D;
            }
        }

        double uEnd = RealPoint.dotProduct(transformedEndPointRelative, transformedMajorAxisRelative) / RealPoint.magnitude(transformedMajorAxisRelative);
        double vEnd = RealPoint.dotProduct(transformedEndPointRelative, transformedMinorAxisRelative) / RealPoint.magnitude(transformedMinorAxisRelative);
        double transformedEndParameter;
        if (uEnd == 0.0D) {
            if (vEnd > 0.0D) {
                transformedEndParameter = 1.5707963267948966D;
            } else {
                transformedEndParameter = -1.5707963267948966D;
            }
        } else {
            transformedEndParameter = Math.atan(vEnd * majorAxisLength / (uEnd * minorAxisLength));
            if (uEnd < 0.0D) {
                transformedEndParameter += 3.141592653589793D;
            }
        }

        double dxfStartParameter = transformedStartParameter;
        double dxfEndParameter = transformedEndParameter;
        boolean dxfIsCounterclockwise = true;
        RealPoint basisVectorI = new RealPoint(1.0D, 0.0D, 0.0D);
        RealPoint basisVectorJ = new RealPoint(0.0D, 1.0D, 0.0D);
        RealPoint transformedBasisVectorI = RealPoint.mapVector(this.graphicsMatrix, basisVectorI);
        RealPoint transformedBasisVectorJ = RealPoint.mapVector(this.graphicsMatrix, basisVectorJ);
        RealPoint transformedBasisVectorCrossProduct = RealPoint.crossProduct(transformedBasisVectorI, transformedBasisVectorJ);
        double orientationSign = transformedBasisVectorCrossProduct.z;
        if (orientationSign * sweepAngle < 0.0D) {
            dxfStartParameter = transformedEndParameter;
            dxfEndParameter = transformedStartParameter;
            dxfIsCounterclockwise = false;
        }

        if (Math.abs(sweepAngleRadians) >= 6.283185307179586D) {
            dxfEndParameter = dxfStartParameter + 6.283185307179586D;
        }

        return new DXFEllipse(transformedCenter, transformedMajorAxisRelative, transformedAxisRatio, dxfStartParameter, dxfEndParameter, dxfIsCounterclockwise, this);
    }

    public void fillArc(int x, int y, int width, int height, int startAngle, int arcAngle) {
        Vector<DXFEntity> boundary = new Vector();
        double startParameter = (double)startAngle * 0.017453292519943295D;
        double endParameter = (double)(startAngle + arcAngle) * 0.017453292519943295D;
        double halfWidth = (double)width / 2.0D;
        double halfHeight = (double)height / 2.0D;
        RealPoint center = new RealPoint((double)x + halfWidth, (double)y + halfHeight, 0.0D);
        RealPoint startPoint = new RealPoint(center.x + halfWidth * Math.cos(startParameter), center.y - halfHeight * Math.sin(startParameter), 0.0D);
        RealPoint endPoint = new RealPoint(center.x + halfWidth * Math.cos(endParameter), center.y - halfHeight * Math.sin(endParameter), 0.0D);
        if (arcAngle >= 0) {
            boundary.add(this.createDXFEllipticalArc((double)x, (double)y, (double)width, (double)height, (double)startAngle, (double)arcAngle));
            boundary.add(this.createDXFLine(endPoint.x, endPoint.y, center.x, center.y));
            boundary.add(this.createDXFLine(center.x, center.y, startPoint.x, startPoint.y));
        } else {
            boundary.add(this.createDXFEllipticalArc((double)x, (double)y, (double)width, (double)height, (double)(startAngle + arcAngle), (double)(-arcAngle)));
            boundary.add(this.createDXFLine(startPoint.x, startPoint.y, center.x, center.y));
            boundary.add(this.createDXFLine(center.x, center.y, endPoint.x, endPoint.y));
        }

        Vector<Vector<DXFEntity>> boundaries = new Vector();
        boundaries.add(boundary);
        DXFHatch hatch = new DXFHatch(boundaries, this.color);
        this.dxfDocument.addEntity(hatch);
    }

    public void drawPolyline(int[] xPoints, int[] yPoints, int nPoints) {
        double[] xPointsFloat = new double[nPoints];
        double[] yPointsFloat = new double[nPoints];

        for(int i = 0; i < nPoints; ++i) {
            xPointsFloat[i] = (double)xPoints[i];
            yPointsFloat[i] = (double)yPoints[i];
        }

        this.drawPolyline(xPointsFloat, yPointsFloat, nPoints);
    }

    public void drawPolyline(double[] xPoints, double[] yPoints, int nPoints) {
        boolean isClosed;
        if (xPoints[0] == xPoints[nPoints - 1] && yPoints[0] == yPoints[nPoints - 1]) {
            isClosed = true;
        } else {
            isClosed = false;
        }

        this.drawPolyline(xPoints, yPoints, nPoints, isClosed);
    }

    private void drawPolyline(double[] xPoints, double[] yPoints, int nPoints, boolean isClosed) {
        double[] pts = new double[2 * nPoints];

        for(int i = 0; i < nPoints; ++i) {
            pts[2 * i] = xPoints[i];
            pts[2 * i + 1] = yPoints[i];
        }

        this.graphicsMatrix.transform(pts, 0, pts, 0, nPoints);
        Vector<RealPoint> vertices = new Vector();

        for(int i = 0; i < nPoints; ++i) {
            vertices.add(new RealPoint(pts[2 * i], pts[2 * i + 1], 0.0D));
        }

        DXFLWPolyline polyline = new DXFLWPolyline(nPoints, vertices, isClosed, this);
        this.dxfDocument.addEntity(polyline);
    }

    public void drawPolygon(int[] xPoints, int[] yPoints, int nPoints) {
        double[] xPointsFloat = new double[nPoints];
        double[] yPointsFloat = new double[nPoints];

        for(int i = 0; i < nPoints; ++i) {
            xPointsFloat[i] = (double)xPoints[i];
            yPointsFloat[i] = (double)yPoints[i];
        }

        this.drawPolygon(xPointsFloat, yPointsFloat, nPoints);
    }

    public void drawPolygon(double[] xPoints, double[] yPoints, int nPoints) {
        this.drawPolyline(xPoints, yPoints, nPoints, true);
    }

    public void drawPolygon(Polygon p) {
        this.drawPolygon(p.xpoints, p.ypoints, p.npoints);
    }

    public void fillPolygon(int[] xPoints, int[] yPoints, int nPoints) {
        Polygon p = new Polygon(xPoints, yPoints, nPoints);
        this.fillPolygon(p);
    }

    public void fillPolygon(double[] xPoints, double[] yPoints, int nPoints) {
        int[] xPointsInt = new int[nPoints];
        int[] yPointsInt = new int[nPoints];

        for(int i = 0; i < nPoints; ++i) {
            xPointsInt[i] = (int)xPoints[i];
            yPointsInt[i] = (int)yPoints[i];
        }

        Polygon p = new Polygon(xPointsInt, yPointsInt, nPoints);
        this.fill(p);
    }

    public void fillPolygon(Polygon p) {
        this.fill(p);
    }

    public void drawSpline(int degree, double[] controlPoints, int[] multiplicities, boolean throughEndpoints) {
//        Vector<SplineControlPoint> transformedControlPoints = new Vector();
//
//        for(int i = 0; i < controlPoints.length / 2; ++i) {
//            double[] coords = new double[]{controlPoints[2 * i], controlPoints[2 * i + 1]};
//            this.graphicsMatrix.transform(coords, 0, coords, 0, 1);
//            SplineControlPoint transformedControlPoint = new SplineControlPoint(coords[0], coords[1], 0.0D, multiplicities[i]);
//            transformedControlPoints.add(transformedControlPoint);
//        }
//
//        DXFSpline spline = new DXFSpline(degree, transformedControlPoints, throughEndpoints, this);
//        this.dxfDocument.addEntity(spline);
    }

    public void drawSpline(int degree, double[] controlPoints, double[] knots) {
//        this.graphicsMatrix.transform(controlPoints, 0, controlPoints, 0, controlPoints.length / 2);
//        DXFSpline spline = new DXFSpline(degree, controlPoints, knots, this);
//        this.dxfDocument.addEntity(spline);
    }

    public void drawString(String str, int x, int y) {
        this.drawString(str, (double)x, (double)y);
    }

    public void drawString(AttributedCharacterIterator charIterator, int x, int y) {
        if (charIterator == null) {
            throw new NullPointerException();
        } else {
            StringBuffer stringBuffer = new StringBuffer();

            for(char c = charIterator.first(); c != '\uffff'; c = charIterator.next()) {
                stringBuffer.append(c);
            }

            this.drawString(stringBuffer.toString(), x, y);
        }
    }

    public void drawChars(char[] data, int offset, int length, int x, int y) {
        this.drawString(new String(data, offset, length), x, y);
    }

    public void drawBytes(byte[] data, int offset, int length, int x, int y) {
        this.drawString(new String(data, 0, offset, length), x, y);
    }

    public boolean drawImage(Image img, int x, int y, ImageObserver observer) {
        throw new UnsupportedOperationException("Image drawing not supported");
    }

    public boolean drawImage(Image img, int x, int y, int width, int height, ImageObserver observer) {
        throw new UnsupportedOperationException("Image drawing not supported");
    }

    public boolean drawImage(Image img, int x, int y, Color bgcolor, ImageObserver observer) {
        throw new UnsupportedOperationException("Image drawing not supported");
    }

    public boolean drawImage(Image img, int x, int y, int width, int height, Color bgcolor, ImageObserver observer) {
        throw new UnsupportedOperationException("Image drawing not supported");
    }

    public boolean drawImage(Image img, int dx1, int dy1, int dx2, int dy2, int sx1, int sy1, int sx2, int sy2, ImageObserver observer) {
        throw new UnsupportedOperationException("Image drawing not supported");
    }

    public boolean drawImage(Image img, int dx1, int dy1, int dx2, int dy2, int sx1, int sy1, int sx2, int sy2, Color bgcolor, ImageObserver observer) {
        throw new UnsupportedOperationException("Image drawing not supported");
    }

    public void dispose() {
    }

    public void finalize() {
        this.dispose();
    }

    public String toString() {
        return this.getClass().getName() + "[font=" + this.getFont() + ",color=" + this.getColor() + "]";
    }

    /** @deprecated */
    @Deprecated
    public Rectangle getClipRect() {
        return this.getClipBounds();
    }

    public boolean hitClip(int x, int y, int width, int height) {
        return true;
    }

    public Rectangle getClipBounds(Rectangle r) {
        return r;
    }

    private DXFStyle addFontStyle() {
        DXFStyle style = new DXFStyle(this.font);
        return this.dxfDocument.addStyle(style);
    }

    public void addRenderingHints(Map<?, ?> arg0) {
    }

    public void clip(Shape arg0) {
    }

    public void draw(Shape shape) {
        if (shape instanceof Line2D) {
            Line2D line = (Line2D)shape;
            this.drawLine(line.getX1(), line.getX1(), line.getX2(), line.getY2());
        } else {
            Rectangle2D rect;
            if (shape instanceof Ellipse2D) {
                Ellipse2D ellipse = (Ellipse2D)shape;
                rect = ellipse.getFrame();
                this.drawOval(rect.getX(), rect.getY(), rect.getWidth(), rect.getHeight());
            } else if (shape instanceof Arc2D) {
                Arc2D arc = (Arc2D)shape;
                rect = arc.getFrame();
                this.drawArc(rect.getX(), rect.getY(), rect.getWidth(), rect.getHeight(), arc.getAngleStart(), arc.getAngleExtent());
            } else if (shape instanceof Rectangle2D) {
//                Rectangle2D rect = (Rectangle2D)shape;
//                this.drawRect(rect.getX(), rect.getY(), rect.getWidth(), rect.getHeight());
            } else if (shape instanceof RoundRectangle2D) {
//                RoundRectangle2D rect = (RoundRectangle2D)shape;
//                this.drawRoundRect(rect.getX(), rect.getY(), rect.getWidth(), rect.getHeight(), rect.getArcWidth(), rect.getArcHeight());
            } else {
                double[] knots;
                double[] controlPoints;
                if (shape instanceof QuadCurve2D) {
                    QuadCurve2D spline = (QuadCurve2D)shape;
                    controlPoints = new double[]{spline.getX1(), spline.getY1(), spline.getCtrlX(), spline.getCtrlY(), spline.getX2(), spline.getY2()};
                    knots = new double[]{0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D};
                    this.drawSpline(2, controlPoints, knots);
                } else if (shape instanceof CubicCurve2D) {
                    CubicCurve2D spline = (CubicCurve2D)shape;
                    controlPoints = new double[]{spline.getX1(), spline.getY1(), spline.getCtrlX1(), spline.getCtrlY1(), spline.getCtrlX2(), spline.getCtrlY2(), spline.getX2(), spline.getY2()};
                    knots = new double[]{0.0D, 0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D, 1.0D};
                    this.drawSpline(3, controlPoints, knots);
                } else {
                    PathIterator iterator = shape.getPathIterator(new AffineTransform());
                    double currentX = 0.0D;
                    double currentY = 0.0D;
                    double initialX = 0.0D;
                    double initialY = 0.0D;
                    double[] coords = new double[6];
                    int curveType = iterator.currentSegment(coords);
                    if (curveType == 0) {
                        initialX = coords[0];
                        initialY = coords[1];
                        currentX = coords[0];
                        currentY = coords[1];
                        iterator.next();

                        for(; !iterator.isDone(); iterator.next()) {
                            curveType = iterator.currentSegment(coords);
//                            double[] controlPoints;
//                            double[] knots;
                            switch(curveType) {
                                case 0:
                                    initialX = coords[0];
                                    initialY = coords[1];
                                    currentX = coords[0];
                                    currentY = coords[1];
                                    break;
                                case 1:
                                    this.drawLine(currentX, currentY, coords[0], coords[1]);
                                    currentX = coords[0];
                                    currentY = coords[1];
                                    break;
                                case 2:
                                    controlPoints = new double[]{currentX, currentY, coords[0], coords[1], coords[2], coords[3]};
                                    knots = new double[]{0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D};
                                    this.drawSpline(2, controlPoints, knots);
                                    currentX = coords[2];
                                    currentY = coords[3];
                                    break;
                                case 3:
                                    controlPoints = new double[]{currentX, currentY, coords[0], coords[1], coords[2], coords[3], coords[4], coords[5]};
                                    knots = new double[]{0.0D, 0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D, 1.0D};
                                    this.drawSpline(3, controlPoints, knots);
                                    currentX = coords[4];
                                    currentY = coords[5];
                                    break;
                                case 4:
                                    if (currentX != initialX || currentY != initialY) {
                                        this.drawLine(currentX, currentY, initialX, initialY);
                                    }
                            }
                        }
                    }
                }
            }
        }

    }

    public void drawGlyphVector(GlyphVector arg0, float arg1, float arg2) {
        throw new UnsupportedOperationException("Glyph drawing not supported");
    }

    public boolean drawImage(Image arg0, AffineTransform arg1, ImageObserver arg2) {
        throw new UnsupportedOperationException("Image drawing not supported");
    }

    public void drawImage(BufferedImage arg0, BufferedImageOp arg1, int arg2, int arg3) {
        throw new UnsupportedOperationException("Image drawing not supported");
    }

    public void drawRenderableImage(RenderableImage arg0, AffineTransform arg1) {
        throw new UnsupportedOperationException("Image drawing not supported");
    }

    public void drawRenderedImage(RenderedImage arg0, AffineTransform arg1) {
        throw new UnsupportedOperationException("Image drawing not supported");
    }

    public void drawString(String str, float x, float y) {
        this.drawString(str, (double)x, (double)y);
    }

    public void drawString(String string, double x, double y) {
        if (string == null) {
            throw new NullPointerException();
        } else {
            DXFStyle style = this.addFontStyle();
            double[] coords = new double[]{x, y};
            this.graphicsMatrix.transform(coords, 0, coords, 0, 1);
            double[] unitVectorICoords = new double[]{1.0D, 0.0D};
            double[] var10000 = new double[]{0.0D, 1.0D};
            double[] unitVectorITransformedCoords = new double[]{1.0D, 0.0D};
            double[] unitVectorJTransformedCoords = new double[]{0.0D, 1.0D};
            this.mapVectors(this.graphicsMatrix, unitVectorITransformedCoords);
            this.mapVectors(this.graphicsMatrix, unitVectorJTransformedCoords);
            RealPoint unitVectorITransformed = new RealPoint(unitVectorITransformedCoords[0], unitVectorITransformedCoords[1], 0.0D);
            RealPoint unitVectorI = new RealPoint(unitVectorICoords[0], unitVectorICoords[1], 0.0D);
            RealPoint unitVectorJTransformed = new RealPoint(unitVectorJTransformedCoords[0], unitVectorJTransformedCoords[1], 0.0D);
            double rotationAngle = RealPoint.angleBetween(unitVectorI, unitVectorITransformed);
            if (unitVectorITransformed.y < 0.0D) {
                rotationAngle = -rotationAngle;
            }

            double obliqueAngle = RealPoint.angleBetween(unitVectorITransformed, unitVectorJTransformed) - 1.5707963267948966D;
            if (unitVectorJTransformed.x * unitVectorITransformed.y - unitVectorJTransformed.y * unitVectorITransformed.x < 0.0D) {
                obliqueAngle = -obliqueAngle;
            }

            rotationAngle *= 57.29577951308232D;
            obliqueAngle *= 57.29577951308232D;
            DXFText text = new DXFText(string, new RealPoint(coords[0], coords[1], 0.0D), rotationAngle, obliqueAngle, style, this);
            this.dxfDocument.addEntity(text);
        }
    }

    public void drawString(AttributedCharacterIterator charIterator, float x, float y) {
        if (charIterator == null) {
            throw new NullPointerException();
        } else {
            StringBuffer stringBuffer = new StringBuffer();

            for(char c = charIterator.first(); c != '\uffff'; c = charIterator.next()) {
                stringBuffer.append(c);
            }

            this.drawString(stringBuffer.toString(), x, y);
        }
    }

    public void fill(Shape shape) {
        this.fill(shape, this.getColor());
    }

    public void clear(Shape shape) {
        this.fill(shape, this.getBackground());
    }

    public void fill(Shape shape, Color color) {
        Vector<Vector<DXFEntity>> boundaries = new Vector();
        PathIterator iterator = shape.getPathIterator(new AffineTransform());
        double currentX = 0.0D;
        double currentY = 0.0D;
        double initialX = 0.0D;
        double initialY = 0.0D;
        double[] coords = new double[6];
        int curveType = iterator.currentSegment(coords);
        if (curveType == 0) {
            Vector<DXFEntity> boundary = new Vector();
            boundaries.add(boundary);
            initialX = coords[0];
            initialY = coords[1];
            currentX = coords[0];
            currentY = coords[1];
            iterator.next();

            for(; !iterator.isDone(); iterator.next()) {
                curveType = iterator.currentSegment(coords);
                double[] points;
                DXFLine line;
                byte degree;
//                DXFSpline spline;
                double[] knots;
                switch(curveType) {
                    case 0:
                        boundary = new Vector();
                        boundaries.add(boundary);
                        initialX = coords[0];
                        initialY = coords[1];
                        currentX = coords[0];
                        currentY = coords[1];
                        break;
                    case 1:
                        points = new double[]{currentX, currentY, coords[0], coords[1]};
                        this.graphicsMatrix.transform(points, 0, points, 0, 2);
                        line = new DXFLine(new RealPoint(points[0], points[1], 0.0D), new RealPoint(points[2], points[3], 0.0D), this);
                        boundary.add(line);
                        currentX = coords[0];
                        currentY = coords[1];
                        break;
                    case 2:
                        points = new double[]{currentX, currentY, coords[0], coords[1], coords[2], coords[3]};
                        knots = new double[]{0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D};
                        degree = 2;
                        this.graphicsMatrix.transform(points, 0, points, 0, 3);
//                        spline = new DXFSpline(degree, points, knots, this);
//                        boundary.add(spline);
                        currentX = coords[2];
                        currentY = coords[3];
                        break;
                    case 3:
                        points = new double[]{currentX, currentY, coords[0], coords[1], coords[2], coords[3], coords[4], coords[5]};
                        knots = new double[]{0.0D, 0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D, 1.0D};
                        degree = 3;
                        this.graphicsMatrix.transform(points, 0, points, 0, 4);
//                        spline = new DXFSpline(degree, points, knots, this);
//                        boundary.add(spline);
                        currentX = coords[4];
                        currentY = coords[5];
                        break;
                    case 4:
                        if (currentX != initialX || currentY != initialY) {
                            points = new double[]{currentX, currentY, initialX, initialY};
                            this.graphicsMatrix.transform(points, 0, points, 0, 2);
                            line = new DXFLine(new RealPoint(points[0], points[1], 0.0D), new RealPoint(points[2], points[3], 0.0D), this);
                            boundary.add(line);
                        }
                }
            }
        }

        DXFHatch hatch = new DXFHatch(boundaries, color);
        this.dxfDocument.addEntity(hatch);
    }

    public Color getBackground() {
        return this.backColor;
    }

    public Composite getComposite() {
        return null;
    }

    public GraphicsConfiguration getDeviceConfiguration() {
        return null;
    }

    public FontRenderContext getFontRenderContext() {
        return GraphicsEnvironment.getLocalGraphicsEnvironment().createGraphics(new BufferedImage(100, 100, 6)).getFontRenderContext();
    }

    public Paint getPaint() {
        return this.color;
    }

    public Object getRenderingHint(Key arg0) {
        return null;
    }

    public RenderingHints getRenderingHints() {
        return null;
    }

    public Stroke getStroke() {
        return this.stroke;
    }

    public AffineTransform getTransform() {
        return new AffineTransform(this.javaTransformMatrix);
    }

    public boolean hit(Rectangle arg0, Shape arg1, boolean arg2) {
        return false;
    }

    public void rotate(double radians) {
        this.javaTransformMatrix.rotate(radians);
        this.setMatrix();
    }

    public void rotate(double radians, double x, double y) {
        this.translate(x, y);
        this.rotate(radians);
        this.translate(-x, -y);
    }

    public void scale(double sx, double sy) {
        this.javaTransformMatrix.scale(sx, sy);
        this.setMatrix();
    }

    public void setBackground(Color color) {
        this.backColor = color;
    }

    public void setComposite(Composite arg0) {
    }

    public void setPaint(Paint paint) {
        if (paint instanceof Color) {
            this.color = (Color)paint;
        }

    }

    public void setRenderingHint(Key arg0, Object arg1) {
    }

    public void setRenderingHints(Map<?, ?> arg0) {
    }

    public void setStroke(Stroke stroke) {
        if (stroke instanceof BasicStroke) {
            this.stroke = (BasicStroke)stroke;
        } else {
            throw new UnsupportedOperationException("Only BasicStroke supported by DXFGraphics");
        }
    }

    public void setTransform(AffineTransform newTransform) {
        this.javaTransformMatrix = new AffineTransform(newTransform);
        this.setMatrix();
    }

    public void shear(double sx, double sy) {
        this.javaTransformMatrix.shear(sx, sy);
        this.setMatrix();
    }

    public void transform(AffineTransform transform) {
        this.javaTransformMatrix.concatenate(transform);
        this.setMatrix();
    }

    public void translate(int x, int y) {
        this.translate((double)x, (double)y);
    }

    public void translate(double x, double y) {
        this.javaTransformMatrix.translate(x, y);
        this.setMatrix();
    }

    private void mapVectors(AffineTransform m, double[] vectors) {
        double[] matrixCoeffs = new double[]{0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D};
        m.getMatrix(matrixCoeffs);
        matrixCoeffs[4] = 0.0D;
        matrixCoeffs[5] = 0.0D;
        AffineTransform l = new AffineTransform(matrixCoeffs);
        l.transform(vectors, 0, vectors, 0, vectors.length / 2);
    }

    private static enum PaintMode {
        PAINT_MODE,
        XOR_MODE;

        private PaintMode() {
        }
    }
}

