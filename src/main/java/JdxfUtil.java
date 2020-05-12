
import sdu.bin.DXFDocument;
import sdu.bin.DXFGraphics;

import java.io.FileWriter;

/**
 * @Author: bin
 * @Date: 2020/2/29 23:47
 * @Description:
 * 针对 {@link JdxfBaseMethods} 的再次封装
 */
public class JdxfUtil {


    /**
     * 保存为dxf格式的文件
     * @param filePath
     * @param fileName
     * @throws Exception
     */
    public static void writeToFile(String filePath, String fileName) throws Exception {
        /* Create a DXF document and get its associated DXFGraphics instance */

        DXFDocument dxfDocument = new
                DXFDocument("Example");
        DXFGraphics dxfGraphics =
                dxfDocument.getGraphics();

        /* Do drawing commands as on any other Graphics. If you have a paint(Graphics) method, you can just use it with the DXFGraphics instance since it's a subclass of Graphics. */
        // 此处执行绘图的静态方法

        /* Get the DXF output as a string - it's just text - and  save  in a file for use with a CAD package */
        String stringOutput = dxfDocument.toDXFString();
//        String filePath = "D:\\home\\test\\test.dxf";
        FileWriter fileWriter = new FileWriter(filePath + fileName + ".dxf");
        fileWriter.write(stringOutput.toCharArray());
        fileWriter.flush();
        fileWriter.close();
    }


    // 绘制矩形 支护断面图

    /**
     * 锚杆间距：图中：800
     * 顶锚索横向间距：图中1800
     * 顶锚杆与竖直的夹角：图中：10~15度
     * 帮锚杆与水平的夹角：图中：20~30度
     * 内高度：图中3200
     * 内宽度：图中：4400
     * 壁厚： 图中：100
     *
     * 顶锚索纵向间距：图中1600
     *
     * 一个小问题，图中有个500，是最下一根帮锚索与地面的距离，不是上下平均的，如果是上下平均应该是400
     */
    public static void drawRectangleFractureSurface(DXFGraphics graphics,int totalWidth, int totalHeight, int horizontalAngle, int verticalAngle) {


    }
}
