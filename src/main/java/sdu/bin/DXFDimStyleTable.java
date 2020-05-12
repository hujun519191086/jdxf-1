package sdu.bin;

/**
 * @Author: bin
 * @Date: 2020/4/27 14:12
 * @Description:
 */

public class DXFDimStyleTable extends DXFTable {
    private static final long serialVersionUID = 1L;

    public DXFDimStyleTable(String name) {
        super(name);
    }

    public String toDXFString() {
        String returnString = new String();
        returnString = returnString + "0\nTABLE\n";
        returnString = returnString + "2\n" + this.name + "\n";
        returnString = returnString + this.myDXFDatabaseObject.toDXFString();
        returnString = returnString + "100\nAcDbSymbolTable\n";
        returnString = returnString + "70\n" + this.size() + "\n";
        returnString = returnString + "100\nAcDbDimStyleTable\n";
        returnString = returnString + "71\n1\n";

        for(int i = 0; i < this.size(); ++i) {
            returnString = returnString + ((DXFDatabaseObject)this.elementAt(i)).toDXFString();
        }

        returnString = returnString + "0\nENDTAB\n";
        return returnString;
    }
}
