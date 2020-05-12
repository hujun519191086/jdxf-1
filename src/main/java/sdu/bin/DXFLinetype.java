package sdu.bin;

/**
 * @Author: bin
 * @Date: 2020/4/27 14:08
 * @Description:
 */

public class DXFLinetype extends DXFTableRecord {
    private String name;

    public DXFLinetype(String name) {
        this.name = name;
    }

    public String toDXFString() {
        String returnString = new String();
        returnString = returnString + "0\nLTYPE\n";
        returnString = returnString + super.toDXFString();
        returnString = returnString + "100\nAcDbLinetypeTableRecord\n";
        returnString = returnString + "2\n" + this.name + "\n";
        returnString = returnString + "70\n0\n";
        returnString = returnString + "72\n65\n";
        returnString = returnString + "73\n0\n";
        returnString = returnString + "40\n0\n";
        returnString = returnString + "3\n\n";
        return returnString;
    }
}

