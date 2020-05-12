package sdu.bin;

/**
 * @Author: bin
 * @Date: 2020/4/27 14:07
 * @Description:
 */

public class DXFViewport extends DXFTableRecord {
    private String name;
    private int viewportHeight;

    public DXFViewport(String name, int viewportHeight) {
        this.name = name;
        this.viewportHeight = viewportHeight;
    }

    public String toDXFString() {
        String returnString = new String();
        returnString = returnString + "0\nVPORT\n";
        returnString = returnString + super.toDXFString();
        returnString = returnString + "100\nAcDbViewportTableRecord\n";
        returnString = returnString + "2\n" + this.name + "\n";
        returnString = returnString + "40\n" + this.viewportHeight + "\n";
        returnString = returnString + "70\n0\n";
        return returnString;
    }
}
