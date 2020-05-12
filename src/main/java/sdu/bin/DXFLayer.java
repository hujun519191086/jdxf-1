package sdu.bin;

/**
 * @Author: bin
 * @Date: 2020/4/27 14:09
 * @Description:
 */

public class DXFLayer extends DXFTableRecord {
    private String name;

    public DXFLayer(String name) {
        this.name = name;
    }

    public String toDXFString() {
        String returnString = new String();
        returnString = returnString + "0\nLAYER\n";
        returnString = returnString + super.toDXFString();
        returnString = returnString + "100\nAcDbLayerTableRecord\n";
        returnString = returnString + "2\n" + this.name + "\n";
        returnString = returnString + "70\n0\n";
        returnString = returnString + "390\n1\n";
        return returnString;
    }
}
