package sdu.bin;

/**
 * @Author: bin
 * @Date: 2020/4/27 14:05
 * @Description:
 */

public class DXFHeaderSegmentAutoCAD extends DXFHeaderSegment {
    private String acadVersion;
    private int handleLimit;

    public DXFHeaderSegmentAutoCAD(String acadVersion) {
        this.acadVersion = acadVersion;
        this.handleLimit = 20000;
    }

    public void setHandleLimit(int handleLimit) {
        this.handleLimit = handleLimit;
    }

    public String toDXFString() {
        String returnString = new String();
        returnString = returnString + super.toDXFString();
        returnString = returnString + "9\n$ACADVER\n";
        returnString = returnString + "1\n" + this.acadVersion + "\n";
        returnString = returnString + "9\n$HANDSEED\n";
        returnString = returnString + "5\n" + Integer.toHexString(this.handleLimit) + "\n";
        return returnString;
    }
}
