package sdu.bin;

/**
 * @Author: bin
 * @Date: 2020/4/27 14:10
 * @Description:
 */

public class DXFBlockRecord extends DXFTableRecord {
    private String name;

    public DXFBlockRecord(String name) {
        this.name = name;
    }

    public String toDXFString() {
        String returnString = new String();
        returnString = returnString + "0\nBLOCK_RECORD\n";
        returnString = returnString + super.toDXFString();
        returnString = returnString + "100\nAcDbBlockTableRecord\n";
        returnString = returnString + "2\n" + this.name + "\n";
        returnString = returnString + "70\n0\n";
        returnString = returnString + "280\n1\n";
        returnString = returnString + "281\n0\n";
        return returnString;
    }
}

