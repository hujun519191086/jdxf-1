

import sdu.bin.DXFGraphics;

import java.awt.*;

/**
 * @Author: bin
 * @Date: 2020/3/5 16:25
 * @Description:
 * 构建图形的基本方法
 *
 * 针对 {@link JdxfForArch} 的再次封装
 */
public class JdxfBaseMethods {
    /**
     * TODO  输入长度的 10%  交给 JdxfForArch
     */
    // 标注数据的 长辅助线 长度
    private static final double LENGTH_LONG_GUIDE_LINE = 200;
    // 标注数据的 短辅助线 长度
    private static final double LENGTH_SHORT_GUIDE_LINE = 100;

    /**
     * 绘制一个 矩形 巷道
     * 内宽
     * 内高
     * 壁厚
     * 左下角坐标
     * 锚杆间距 暂无 暂定800  锚杆数量使用内宽计算
     * 锚索间距 暂无 暂定1200
     * 顶部锚杆倾角 暂无 暂定0
     * 侧边锚杆倾角 暂无 暂定0
     */
    private static double stickSpace = 200; // 锚杆间距
    private static double ropeSpace = 300; // 锚索间距
    private static double stickLength = 240; // 锚杆长度
    private static double ropeLength = 340; // 锚索长度

    public static void drawRectangleTunnel(DXFGraphics graphics, double innerWidth, double innerHeight,
                                           double wallWidth, double leftDownX, double leftDownY) {
        double x0 = leftDownX,
                x1 = x0,
                x2 = x0 + innerWidth + wallWidth * 2,
                x3 = x2,
                x4 = x0 + innerWidth + wallWidth,
                x5 = x4,
                x6 = x0 + wallWidth,
                x7 = x6;
        double y0 = leftDownY,
                y1 = y0 + innerHeight + wallWidth,
                y2 = y1,
                y3 = y0,
                y4 = y0,
                y5 = y0 + innerHeight,
                y6 = y5,
                y7 = y0;
        double[] xPoints = {x0, x1, x2, x3, x4, x5, x6, x7};
        double[] yPoints = {y0, y1, y2, y3, y4, y5, y6, y7};
        JdxfForArch.fillPolygon(graphics, xPoints, yPoints, Color.BLUE);

        // 底部横线
        JdxfForArch.drawLine(graphics, x7, y7, x4, y4);

        /** 内宽标注 */
        // 左 长标注线
        JdxfForArch.drawLine(graphics, x0, y0, x0, y0 - LENGTH_LONG_GUIDE_LINE);
        // 右 长标注线
        JdxfForArch.drawLine(graphics, x3, y3, x3, y3 - LENGTH_LONG_GUIDE_LINE);
        // 底 长标注
        JdxfForArch.paintArrowLine(graphics, x0, y0 - LENGTH_LONG_GUIDE_LINE * 0.75,
                x3, y3 - LENGTH_LONG_GUIDE_LINE * 0.75, "4600");
        /** 外宽标注 */
        // 左 短标注线
        JdxfForArch.drawLine(graphics, x7, y7, x7, y7 - LENGTH_SHORT_GUIDE_LINE);
        // 右 短标注线
        JdxfForArch.drawLine(graphics, x4, y4, x4, y4 - LENGTH_SHORT_GUIDE_LINE);
        // 底 短标注
        JdxfForArch.paintArrowLine(graphics, x7, y7 - LENGTH_SHORT_GUIDE_LINE * 0.75,
                x4, y4 - LENGTH_SHORT_GUIDE_LINE * 0.75, "4400");


        /** 内高标注 */
        JdxfForArch.paintArrowLine(graphics, x5 - innerWidth * 0.1, y5,
                x4 - innerWidth * 0.1, y4, "3200");

        /** 帮锚杆 */
        // 一侧帮锚杆的数量
        int sideStickAmount = (int)(innerHeight/stickSpace) + 1;
        // 最外侧的帮锚杆距离边缘的距离
        double stickEdgeSpace = (innerHeight - (sideStickAmount - 1) * stickSpace) / 2;
        // 左侧 帮锚杆
        for (int i = 0; i < sideStickAmount; i ++) {
            JdxfForArch.drawLine(graphics, x0 - stickLength, y0 + stickEdgeSpace + i * stickSpace, x0, y0 + stickEdgeSpace + i * stickSpace);
        }
        // 标注第一根左锚杆
        JdxfForArch.markLine(graphics, x0 - stickLength, y0 + stickEdgeSpace, x0, y0 + stickEdgeSpace, "帮锚杆");
        // 右侧帮锚杆
        for (int i = 0; i < sideStickAmount; i ++) {
            // 绘制帮锚杆
            JdxfForArch.drawLine(graphics, x3 + stickLength, y3 + stickEdgeSpace + i * stickSpace, x3, y3 + stickEdgeSpace + i * stickSpace);
            // 标注右侧帮锚杆的间距
            if (i < sideStickAmount - 1) {
                JdxfForArch.paintArrowLine(graphics, x3 + stickLength * 0.8, y3 + stickEdgeSpace + i * stickSpace, x3 + stickLength * 0.8, y3 + stickEdgeSpace + (i+1) * stickSpace, "" + stickSpace);
            }
        }

        /** 顶锚杆 */

        // 顶锚杆的数量
        int topStickAmount = (int)(innerWidth/stickSpace) + 1;
        // 最外侧的顶锚杆距离边缘的距离
        double topStickEdgeSpace = (innerWidth - (topStickAmount - 1) * stickSpace) / 2;

        for (int i = 0; i < topStickAmount; i ++) {
            JdxfForArch.drawLine(graphics, x1 + topStickEdgeSpace + i * stickSpace, y1, x1 + topStickEdgeSpace + i * stickSpace, y1 + stickLength);
        }
        // 标注第一根顶锚杆
        JdxfForArch.markLine(graphics, x1 + topStickEdgeSpace, y1, x1 + topStickEdgeSpace, y1 + stickLength, "顶锚杆");


        /** 顶锚索 */
        // 锚索的数量
        int topRopeAmount = (int)(innerWidth/ropeSpace) + 1;
        // 最外侧的锚索距离边缘的距离
        double topRopeEdgeSpace = (innerWidth - (topRopeAmount - 1) * ropeSpace) / 2;

        for (int i = 0; i < topRopeAmount; i ++) {
            JdxfForArch.drawLine(graphics, x1 + topRopeEdgeSpace + i * ropeSpace, y1, x1 + topRopeEdgeSpace + i * ropeSpace, y1 + ropeLength);
            // 标注锚索的间距
            if (i < topRopeAmount - 1) {
                JdxfForArch.paintArrowLine(graphics, x1 + topRopeEdgeSpace + i * ropeSpace, y1 + ropeLength * 0.8, x1 + topRopeEdgeSpace + (i + 1) * ropeSpace, y1 + ropeLength * 0.8, "" + ropeSpace);
            }
            // 标注第一根锚索
            if (i == 0) {
                JdxfForArch.markLine(graphics, x1 + topRopeEdgeSpace + i * ropeSpace, y1, x1 + topRopeEdgeSpace + i * ropeSpace, y1 + ropeLength, "顶锚索");
            }
        }


        /** */

    }

}
