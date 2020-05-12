package sdu.bin;

/**
 * @Author: bin
 * @Date: 2020/4/27 14:17
 * @Description:
 */
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;

public class DXFLine extends DXFEntity {
    private RealPoint start;
    private RealPoint end;
    private Color color;
    private BasicStroke stroke;

    public DXFLine(RealPoint start, RealPoint end, Graphics2D graphics) {
        this.start = new RealPoint(start);
        this.end = new RealPoint(end);
        this.color = graphics.getColor();
        this.stroke = (BasicStroke)graphics.getStroke();
    }

    public String toDXFString() {
        String result = "0\nLINE\n";
        result = result + super.toDXFString();
        result = result + "100\nAcDbLine\n";
        result = result + "10\n" + this.start.x + "\n";
        result = result + "20\n" + this.start.y + "\n";
        result = result + "30\n" + this.start.z + "\n";
        result = result + "11\n" + this.end.x + "\n";
        result = result + "21\n" + this.end.y + "\n";
        result = result + "31\n" + this.end.z + "\n";
        result = result + "370\n" + this.getDXFLineWeight(this.stroke.getLineWidth()) + "\n";
        result = result + "62\n" + DXFColor.getClosestDXFColor(this.color.getRGB()) + "\n";
        return result;
    }

    public String getDXFHatchInfo() {
        String result = "72\n1\n";
        result = result + "10\n" + this.start.x + "\n";
        result = result + "20\n" + this.start.y + "\n";
        result = result + "11\n" + this.end.x + "\n";
        result = result + "21\n" + this.end.y + "\n";
        return result;
    }
}
