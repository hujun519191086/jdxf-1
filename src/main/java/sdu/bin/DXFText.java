package sdu.bin;

/**
 * @Author: bin
 * @Date: 2020/4/27 14:20
 * @Description:
 */

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;

public class DXFText extends DXFEntity {
    private RealPoint basePoint;
    private String text;
    private Color color;
    private Font font;
    private FontMetrics fontMetrics;
    private double rotationAngle;
    private double obliqueAngle;
    private DXFStyle style;
    private double capHeight;

    public DXFText(String text, RealPoint basePoint, DXFStyle style, Graphics2D graphics) {
        this(text, basePoint, 0.0D, style, graphics);
    }

    public DXFText(String text, RealPoint basePoint, double rotationAngle, DXFStyle style, Graphics2D graphics) {
        this(text, basePoint, rotationAngle, 0.0D, style, graphics);
    }

    public DXFText(String text, RealPoint basePoint, double rotationAngle, double obliqueAngle, DXFStyle style, Graphics2D graphics) {
        this.text = text;
        this.basePoint = new RealPoint(basePoint);
        this.rotationAngle = rotationAngle;
        this.obliqueAngle = obliqueAngle;
        this.style = style;
        this.font = graphics.getFont();
        this.fontMetrics = graphics.getFontMetrics();
        this.color = graphics.getColor();
        this.capHeight = graphics.getFont().createGlyphVector(graphics.getFontRenderContext(), "H").getVisualBounds().getHeight();
    }

    public String toDXFString() {
        String result = "0\nTEXT\n";
        result = result + super.toDXFString();
        result = result + "100\nAcDbText\n";
        this.text.replace('\n', ' ');
        result = result + "1\n" + this.text + "\n";
        result = result + "7\n" + this.style.getStyleName() + "\n";
        result = result + "10\n" + this.basePoint.x + "\n";
        result = result + "20\n" + this.basePoint.y + "\n";
        result = result + "30\n" + this.basePoint.z + "\n";
        result = result + "11\n" + this.basePoint.x + "\n";
        result = result + "21\n" + this.basePoint.y + "\n";
        result = result + "31\n" + this.basePoint.z + "\n";
        result = result + "40\n" + this.capHeight + "\n";
        result = result + "50\n" + this.rotationAngle + "\n";
        result = result + "51\n" + this.obliqueAngle + "\n";
        result = result + "62\n" + DXFColor.getClosestDXFColor(this.color.getRGB()) + "\n";
        result = result + "100\nAcDbText\n";
        return result;
    }
}

