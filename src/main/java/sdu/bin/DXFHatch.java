package sdu.bin;

/**
 * @Author: bin
 * @Date: 2020/4/27 13:26
 * @Description:
 */
import java.awt.Color;
import java.util.Vector;

public class DXFHatch extends DXFEntity {
    private Vector<Vector<DXFEntity>> boundaries;
    private Color color;

    public DXFHatch(Vector<Vector<DXFEntity>> boundaries, Color color) {
        this.boundaries = boundaries;
        this.color = color;
    }

    public String toDXFString() {
        String result = "0\nHATCH\n";
        result = result + super.toDXFString();
        result = result + "100\nAcDbHatch\n";
        result = result + "10\n0\n";
        result = result + "20\n0\n";
        result = result + "30\n0\n";
        result = result + "210\n0\n";
        result = result + "220\n0\n";
        result = result + "230\n1\n";

        if (color instanceof DXFColor) {  // 不是这里控制的
            // 图案填充
            result = result + "2\n" + ((DXFColor)(color)).pattern.value + "\n";
        } else {
            // 颜色填充
            result = result + "2\nFILL_" + this.handle + "\n";
        }
        result = result + "70\n1\n";
        result = result + "71\n0\n";
        result = result + "91\n" + this.boundaries.size() + "\n";

        for(int j = 0; j < this.boundaries.size(); ++j) {
            Vector<DXFEntity> boundary = (Vector)this.boundaries.elementAt(j);
            result = result + "92\n0\n";
            result = result + "93\n" + boundary.size() + "\n";

            for(int i = 0; i < boundary.size(); ++i) {
                result = result + ((DXFEntity)boundary.elementAt(i)).getDXFHatchInfo();
            }

            result = result + "97\n0\n";
        }

        // 导出文件中没有看到这个
//        result = result + "62\n" + DXFColor.getClosestDXFColor(this.color.getRGB()) + "\n";
        result = result + "75\n0\n";
        result = result + "76\n1\n";
        result = result + "98\n0\n";
        return result;
    }
}