package sdu.bin;

/**
 * @Author: bin
 * @Date: 2020/4/30 23:46
 * @Description:
 */

public class SplineControlPoint extends RealPoint {
    private static final long serialVersionUID = 1L;
    public int multiplicity;
    public int expandedIndex;

    public SplineControlPoint(double x, double y, double z, int weight) {
        super(x, y, z);
        this.multiplicity = weight;
    }

    public SplineControlPoint(SplineControlPoint other) {
        super(other.x, other.y, other.z);
        this.multiplicity = other.multiplicity;
    }

    public boolean equals(Object object) {
        if (!(object instanceof SplineControlPoint)) {
            return false;
        } else {
            SplineControlPoint other = (SplineControlPoint)object;
            return this.x == other.x && this.y == other.y && this.z == other.z && this.multiplicity == other.multiplicity;
        }
    }
}
