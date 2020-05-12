package sdu.bin;

/**
 * @Author: bin
 * @Date: 2020/4/27 14:10
 * @Description:
 */

public class DXFBlock extends DXFEntity {
    private String name;

    public DXFBlock(String name) {
        this.name = name;
    }

    public String toDXFString() {
        String returnString = new String();
        returnString = returnString + "0\nBLOCK\n";
        returnString = returnString + super.toDXFString();
        returnString = returnString + "100\nAcDbBlockBegin\n";
        returnString = returnString + "2\n" + this.name + "\n";
        returnString = returnString + "70\n0\n";
        returnString = returnString + "10\n0\n";
        returnString = returnString + "20\n0\n";
        returnString = returnString + "30\n0\n";
        returnString = returnString + "3\n" + this.name + "\n";
        returnString = returnString + "1\n\n";
        return returnString;
    }
}

