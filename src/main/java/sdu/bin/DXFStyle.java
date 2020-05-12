package sdu.bin;

/**
 * @Author: bin
 * @Date: 2020/4/27 14:11
 * @Description:
 */

import java.awt.Font;

public class DXFStyle extends DXFTableRecord {
    private String name;
    private String dxfFontName;
    private Font javaFont;

    public DXFStyle(Font font) {
        this.javaFont = font;
        this.dxfFontName = getDXFFontName(font);
        this.name = this.dxfFontName;
    }

    public String toDXFString() {
        String returnString = new String();
        returnString = returnString + "0\nSTYLE\n";
        returnString = returnString + super.toDXFString();
        returnString = returnString + "100\nAcDbTextStyleTableRecord\n";
        returnString = returnString + "2\n" + this.name + "\n";
        returnString = returnString + "3\n" + this.dxfFontName + "\n";
        returnString = returnString + "70\n0\n";
        return returnString;
    }

    public boolean equals(DXFStyle other) {
        return this.javaFont.equals(other.javaFont);
    }

    public String getStyleName() {
        return this.name;
    }

    private static String getDXFFontName(Font javaFont) {
        return isStandardJavaFont(javaFont) ? javaToDXFFontMap(javaFont) : javaFont.getFontName();
    }

    private static boolean isStandardJavaFont(Font javaFont) {
        return javaFont.getFamily().equals("Serif") || javaFont.getFamily().equals("Monospaced") || javaFont.getFamily().equals("SansSerif");
    }

    private static String javaToDXFFontMap(Font javaFont) {
        String returnString;
        if (javaFont.getFamily().equals("Serif")) {
            returnString = "romanc";
        } else if (javaFont.getFamily().equals("Monospaced")) {
            returnString = "isocpeur";
        } else {
            returnString = "arial";
        }

        int fontStyle = javaFont.getStyle();
        switch(fontStyle) {
            case 1:
                returnString = returnString + "_bold";
                break;
            case 2:
                returnString = returnString + "_italic";
                break;
            case 3:
                returnString = returnString + "_bold_italic";
        }

        return returnString;
    }
}

