package sdu.bin;

/**
 * @Author: bin
 * @Date: 2020/4/27 14:11
 * @Description:
 */

import java.util.Vector;

public class DXFDictionary extends Vector<DXFDictionary> implements DXFObject {
    private static final long serialVersionUID = 1L;
    public String name;
    protected DXFDatabaseObject myDXFDatabaseObject;
    private int ownerHandle;

    public DXFDictionary(String name, int ownerHandle) {
        this.name = name;
        this.ownerHandle = ownerHandle;
        this.myDXFDatabaseObject = new DXFDatabaseObject();
    }

    public String getName() {
        return this.name;
    }

    public int getHandle() {
        return this.myDXFDatabaseObject.getHandle();
    }

    public String toDXFString() {
        String returnString = new String();
        returnString = returnString + "0\nDICTIONARY\n";
        returnString = returnString + this.myDXFDatabaseObject.toDXFString();
        returnString = returnString + "330\n" + Integer.toHexString(this.ownerHandle) + "\n";
        returnString = returnString + "100\nAcDbDictionary\n";
        returnString = returnString + "281\n1\n";

        int i;
        for(i = 0; i < this.size(); ++i) {
            returnString = returnString + "3\n" + ((DXFDictionary)this.elementAt(i)).getName() + "\n";
            returnString = returnString + "350\n" + Integer.toHexString(((DXFDictionary)this.elementAt(i)).getHandle()) + "\n";
        }

        for(i = 0; i < this.size(); ++i) {
            returnString = returnString + ((DXFDictionary)this.elementAt(i)).toDXFString();
        }

        return returnString;
    }
}

