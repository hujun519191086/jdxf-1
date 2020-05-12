package sdu.bin;

/**
 * @Author: bin
 * @Date: 2020/4/27 14:10
 * @Description:
 */

public class DXFBlockEnd extends DXFEntity {
    private DXFBlock block;

    public DXFBlockEnd(DXFBlock block) {
        this.block = block;
    }

    public String toDXFString() {
        String returnString = new String();
        returnString = returnString + "0\nENDBLK\n";
        returnString = returnString + super.toDXFString();
        returnString = returnString + "100\nAcDbBlockEnd\n";
        return returnString;
    }
}
