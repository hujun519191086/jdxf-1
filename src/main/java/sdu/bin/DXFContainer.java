package sdu.bin;

/**
 * @Author: bin
 * @Date: 2020/4/27 14:04
 * @Description:
 */

import java.util.Vector;

public abstract class DXFContainer extends Vector<DXFObject> implements DXFObject {
    private static final long serialVersionUID = 1L;

    public DXFContainer() {
    }

    public String toDXFString() {
        String returnString = new String();

        for(int i = 0; i < this.size(); ++i) {
            returnString = returnString + ((DXFObject)this.elementAt(i)).toDXFString();
        }

        return returnString;
    }
}