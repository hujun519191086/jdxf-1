package sdu.bin;

/**
 * @Author: bin
 * @Date: 2020/4/27 13:27
 * @Description:
 */

public class DXFEntity extends DXFDatabaseObject {
    public DXFEntity() {
    }

    public String toDXFString() {
        String result = super.toDXFString();
        result = result + "100\nAcDbEntity\n";
        return result;
    }

    public String getDXFHatchInfo() {
        return "";
    }

    public int getDXFLineWeight(float javaLineWidth) {
        double scale = 35.27777777777778D;
        double lineWidthMMHundredths = (double)javaLineWidth * scale;
        int[] lineWidthOptions = new int[]{0, 5, 9, 13, 15, 18, 20, 25, 30, 35, 40, 50, 53, 60, 70, 80, 90, 100, 106, 120, 140, 158, 200, 211};

        int i;
        for(i = 1; i < lineWidthOptions.length && (double)lineWidthOptions[i] - lineWidthMMHundredths <= 0.0D; ++i) {
            ;
        }

        lineWidthMMHundredths = (double)lineWidthOptions[i - 1];
        return (int)lineWidthMMHundredths;
    }
}

