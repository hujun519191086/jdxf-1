package sdu.bin;

/**
 * @Author: bin
 * @Date: 2020/4/27 14:16
 * @Description:
 */

import java.awt.geom.AffineTransform;
import java.io.Serializable;

public class RealPoint implements Serializable {
    private static final long serialVersionUID = 1L;
    public double x;
    public double y;
    public double z;

    public RealPoint(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public RealPoint(RealPoint other) {
        this.x = other.x;
        this.y = other.y;
        this.z = other.z;
    }

    public static RealPoint sum(RealPoint p1, RealPoint p2) {
        return new RealPoint(p1.x + p2.x, p1.y + p2.y, p1.z + p2.z);
    }

    public static RealPoint difference(RealPoint p1, RealPoint p2) {
        return new RealPoint(p1.x - p2.x, p1.y - p2.y, p1.z - p2.z);
    }

    public static RealPoint scalarProduct(double a, RealPoint p) {
        return new RealPoint(a * p.x, a * p.y, a * p.z);
    }

    public static RealPoint mapPoint(AffineTransform m, RealPoint p) {
        double[] src = new double[]{p.x, p.y};
        double[] dst = new double[2];
        m.transform(src, 0, dst, 0, 1);
        return new RealPoint(dst[0], dst[1], p.z);
    }

    public static RealPoint mapVector(AffineTransform m, RealPoint p) {
        double[] matrixCoeffs = new double[]{0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D};
        m.getMatrix(matrixCoeffs);
        matrixCoeffs[4] = 0.0D;
        matrixCoeffs[5] = 0.0D;
        AffineTransform l = new AffineTransform(matrixCoeffs);
        double[] src = new double[]{p.x, p.y};
        double[] dst = new double[2];
        l.transform(src, 0, dst, 0, 1);
        return new RealPoint(dst[0], dst[1], p.z);
    }

    public static double magnitude(RealPoint p) {
        return Math.sqrt(p.x * p.x + p.y * p.y + p.z * p.z);
    }

    public static double dotProduct(RealPoint p, RealPoint q) {
        return p.x * q.x + p.y * q.y + p.z * q.z;
    }

    public static RealPoint crossProduct(RealPoint p, RealPoint q) {
        RealPoint result = new RealPoint(p.y * q.z - p.z * q.y, p.z * q.x - p.x * q.z, p.x * q.y - p.y * q.x);
        return result;
    }

    public static double angleBetween(RealPoint p, RealPoint q) {
        double magnitudeProd = magnitude(p) * magnitude(q);
        return magnitudeProd == 0.0D ? 0.0D : Math.acos(dotProduct(p, q) / magnitudeProd);
    }

    public boolean equals(Object object) {
        if (!(object instanceof RealPoint)) {
            return false;
        } else {
            RealPoint other = (RealPoint)object;
            return this.x == other.x && this.y == other.y && this.z == other.z;
        }
    }
}
