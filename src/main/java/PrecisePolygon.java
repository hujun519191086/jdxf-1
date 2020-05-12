/**
 * @Author: bin
 * @Date: 2020/4/27 14:24
 * @Description:
 */
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.PathIterator;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

/**
 * @Author: bin
 * @Date: 2020/2/29 16:39
 * @Description:
 * 绘制一个多边形
 */
public class PrecisePolygon implements Shape {
    public int npoints;
    public double xpoints[];
    public double ypoints[];

    public PrecisePolygon(double[] xpoints, double[] ypoints) {
        if (xpoints.length != ypoints.length) {
            throw new RuntimeException("pointsX与 pointsY的长度必须一致");
        }
        if (xpoints.length < 3) {
            throw new RuntimeException("不足3个点无法构成多边形");
        }
        this.xpoints = xpoints;
        this.ypoints = ypoints;
        this.npoints = xpoints.length;
    }

    @Override
    public Rectangle getBounds() {
        return null;
    }

    @Override
    public Rectangle2D getBounds2D() {
        return null;
    }

    @Override
    public boolean contains(double x, double y) {
        return false;
    }

    @Override
    public boolean contains(Point2D p) {
        return false;
    }

    @Override
    public boolean intersects(double x, double y, double w, double h) {
        return false;
    }

    @Override
    public boolean intersects(Rectangle2D r) {
        return false;
    }

    @Override
    public boolean contains(double x, double y, double w, double h) {
        return false;
    }

    @Override
    public boolean contains(Rectangle2D r) {
        return false;
    }

    @Override
    public PathIterator getPathIterator(AffineTransform at) {
        // 因为JDXF的现状，只需实现这个。入参为空
        return new MyPolygonPathIterator(this, at);
    }

    @Override
    public PathIterator getPathIterator(AffineTransform at, double flatness) {
        return null;
    }


    class MyPolygonPathIterator implements PathIterator {
        PrecisePolygon poly;
        AffineTransform transform;
        int index;

        public MyPolygonPathIterator(PrecisePolygon pg, AffineTransform at) {
            poly = pg;
            transform = at;
            if (pg.npoints == 0) {
                // Prevent a spurious SEG_CLOSE segment
                index = 1;
            }
        }

        @Override
        public int getWindingRule() {
            return 0;
        }

        @Override
        public boolean isDone() {
            return index > poly.npoints;
        }

        @Override
        public void next() {
            index ++;
        }

        // TODO
        @Override
        public int currentSegment(float[] coords) {
            if (index >= poly.npoints) {
                return SEG_CLOSE;
            }
            coords[0] = (float) poly.xpoints[index];
            coords[1] = (float) poly.ypoints[index];
            if (transform != null) {
                transform.transform(coords, 0, coords, 0, 1);
            }
            return (index == 0 ? SEG_MOVETO : SEG_LINETO);
        }

        @Override
        public int currentSegment(double[] coords) {
            if (index >= poly.npoints) {
                return SEG_CLOSE;
            }
            coords[0] = poly.xpoints[index];
            coords[1] = poly.ypoints[index];
            if (transform != null) {
                transform.transform(coords, 0, coords, 0, 1);
            }
            return (index == 0 ? SEG_MOVETO : SEG_LINETO);
        }
    }
}
