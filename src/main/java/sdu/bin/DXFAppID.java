package sdu.bin;

/**
 * @Author: bin
 * @Date: 2020/4/27 14:09
 * @Description:
 */

public class DXFAppID extends DXFTableRecord {
    private String name;

    public DXFAppID(String name) {
        this.name = name;
    }

    public String toDXFString() {
        String returnString = new String();
        returnString = returnString + "0\nAPPID\n";
        returnString = returnString + super.toDXFString();
        returnString = returnString + "100\nAcDbRegAppTableRecord\n";
        returnString = returnString + "2\n" + this.name + "\n";
        returnString = returnString + "70\n0\n";
        return returnString;
    }
}

