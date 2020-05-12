package sdu.bin;

/**
 * @Author: bin
 * @Date: 2020/4/27 14:05
 * @Description:
 */

import java.util.Vector;

public class DXFHeaderSegment implements DXFObject {
    private Vector<DXFHeaderSegment.HeaderLine> lines = new Vector();

    public DXFHeaderSegment() {
    }

    public void addHeaderLine(String name, int code, String value) {
        DXFHeaderSegment.HeaderLine headerLine = new DXFHeaderSegment.HeaderLine(name, code, value);
        this.lines.add(headerLine);
    }

    public String toDXFString() {
        String returnString = new String();

        for(int i = 0; i < this.lines.size(); ++i) {
            DXFHeaderSegment.HeaderLine headerLine = (DXFHeaderSegment.HeaderLine)this.lines.elementAt(i);
            returnString = returnString + "9\n";
            returnString = returnString + headerLine.name + "\n";
            returnString = returnString + headerLine.code + "\n";
            returnString = returnString + headerLine.value + "\n";
        }

        return returnString;
    }

    private class HeaderLine {
        public String name;
        public int code;
        public String value;

        public HeaderLine(String name, int code, String value) {
            this.name = name;
            this.code = code;
            this.value = value;
        }
    }
}

