package sdu.bin;

/**
 * @Author: bin
 * @Date: 2020/4/27 13:28
 * @Description:
 */

public abstract class DXFDatabaseObject implements DXFObject {
    private static int handleCount = 1;
    protected int handle;

    public DXFDatabaseObject() {
        this.handle = handleCount++;
    }

    public String toDXFString() {
        String result = "5\n" + Integer.toHexString(this.handle) + "\n";
        return result;
    }

    public int getHandle() {
        return this.handle;
    }

    public static int getHandleCount() {
        return handleCount;
    }
}
