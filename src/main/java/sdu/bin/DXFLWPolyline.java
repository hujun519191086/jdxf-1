package sdu.bin;

/**
 * @Author: bin
 * @Date: 2020/4/27 14:19
 * @Description:
 */

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.util.Vector;

public class DXFLWPolyline extends DXFEntity {
    private int numVertices;
    private Vector<RealPoint> vertices;
    private boolean closed;
    private Color color;
    private BasicStroke stroke;

    public DXFLWPolyline(int numVertices, Vector<RealPoint> vertices, boolean closed, Graphics2D graphics) {
        this.numVertices = numVertices;
        this.vertices = vertices;
        this.closed = closed;
        this.color = graphics.getColor();
        this.stroke = (BasicStroke)graphics.getStroke();
    }

    public String toDXFString() {
        String result = "0\nLWPOLYLINE\n";
        result = result + super.toDXFString();
        result = result + "100\nAcDbPolyline\n";
        result = result + "90\n" + this.numVertices + "\n";
        if (this.closed) {
            result = result + "70\n1\n";
        } else {
            result = result + "70\n0\n";
        }

        for(int i = 0; i < this.vertices.size(); ++i) {
            RealPoint point = (RealPoint)this.vertices.elementAt(i);
            result = result + "10\n" + point.x + "\n";
            result = result + "20\n" + point.y + "\n";
            result = result + "30\n" + point.z + "\n";
        }

        result = result + "370\n" + this.getDXFLineWeight(this.stroke.getLineWidth()) + "\n";
        result = result + "62\n" + DXFColor.getClosestDXFColor(this.color.getRGB()) + "\n";
        return result;
    }
}
