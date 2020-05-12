package sdu.bin;

/**
 * @Author: bin
 * @Date: 2020/4/27 14:03
 * @Description:
 */

public class DXFSection extends DXFContainer {
    private static final long serialVersionUID = 1L;
    public String name;

    public DXFSection(String name) {
        this.name = name;
    }

    public String toDXFString() {
        String returnString = new String();
        returnString = returnString + "0\nSECTION\n";
        returnString = returnString + "2\n" + this.name + "\n";
        returnString = returnString + super.toDXFString();
        returnString = returnString + "0\nENDSEC\n";
        return returnString;
    }
}
