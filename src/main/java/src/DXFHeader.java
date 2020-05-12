package src;

/**
 * @Author: bin
 * @Date: 2020/4/27 10:09
 * @Description:
 */
public class DXFHeader {

    private static final String ACADVER = "AC1021"; //AutoCAD图形数据库版本号
    private static final String HANDSEED = "c4"; //下一个可用的句柄

    @Override
    public String toString() {
        return "HEADER\n9\n$ACADVER\n1\n" + ACADVER + "9\n$HANDSEED\n5\n" + HANDSEED + "\n0\n";
    }
}
