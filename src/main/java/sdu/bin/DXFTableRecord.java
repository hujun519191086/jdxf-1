package sdu.bin;

/**
 * @Author: bin
 * @Date: 2020/4/27 14:08
 * @Description:
 */


public class DXFTableRecord extends DXFDatabaseObject {
    public DXFTableRecord() {
    }

    public String toDXFString() {
        String result = new String();
        result = result + super.toDXFString();
        result = result + "100\nAcDbSymbolTableRecord\n";
        return result;
    }
}
