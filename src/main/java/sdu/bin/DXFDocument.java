package sdu.bin;

/**
 * @Author: bin
 * @Date: 2020/4/27 14:03
 * @Description:
 */

public class DXFDocument {
    private DXFSection header;
    private DXFSection classes;
    private DXFSection tables;
    private DXFSection blocks;
    private DXFSection entities;
    private DXFSection objects;
    private String documentComment;
    private DXFHeaderSegmentAutoCAD acadHeaderSegment;
    private DXFGraphics graphics;

    public DXFDocument() {
        this("");
    }

    public DXFDocument(String documentComment) {
        this.header = new DXFSection("HEADER");
        this.classes = new DXFSection("CLASSES");
        this.tables = new DXFSection("TABLES");
        this.blocks = new DXFSection("BLOCKS");
        this.entities = new DXFSection("ENTITIES");
        this.objects = new DXFSection("OBJECTS");
        this.documentComment = "";
        this.documentComment = documentComment;
        this.graphics = new DXFGraphics(this);
        this.generateAcadExtras();
    }

    private void generateAcadExtras() {
        this.acadHeaderSegment = new DXFHeaderSegmentAutoCAD("AC1021");
        this.header.add(this.acadHeaderSegment);
        DXFTable viewportTable = new DXFTable("VPORT");
        this.tables.add(viewportTable);
        DXFTable linetypeTable = new DXFTable("LTYPE");
        this.tables.add(linetypeTable);
        DXFTable layerTable = new DXFTable("LAYER");
        this.tables.add(layerTable);
        DXFTable styleTable = new DXFTable("STYLE");
        this.tables.add(styleTable);
        DXFTable viewTable = new DXFTable("VIEW");
        this.tables.add(viewTable);
        DXFTable ucsTable = new DXFTable("UCS");
        this.tables.add(ucsTable);
        DXFTable appIDTable = new DXFTable("APPID");
        this.tables.add(appIDTable);
        DXFTable dimStyleTable = new DXFDimStyleTable("DIMSTYLE");
        this.tables.add(dimStyleTable);
        DXFTable blockRecordTable = new DXFTable("BLOCK_RECORD");
        this.tables.add(blockRecordTable);
        DXFViewport viewport = new DXFViewport("*ACTIVE", 1000);
        viewportTable.add(viewport);
        DXFLinetype linetype = new DXFLinetype("ByBlock");
        linetypeTable.add(linetype);
        linetype = new DXFLinetype("ByLayer");
        linetypeTable.add(linetype);
        DXFLayer layer = new DXFLayer("0");
        layerTable.add(layer);
        DXFAppID appID = new DXFAppID("ACAD");
        appIDTable.add(appID);
        DXFBlockRecord blockRecord = new DXFBlockRecord("*Model_Space");
        blockRecordTable.add(blockRecord);
        blockRecord = new DXFBlockRecord("*Paper_Space");
        blockRecordTable.add(blockRecord);
        DXFBlock block = new DXFBlock("*Model_Space");
        this.blocks.add(block);
        DXFBlockEnd endblock = new DXFBlockEnd(block);
        this.blocks.add(endblock);
        block = new DXFBlock("*Paper_Space");
        this.blocks.add(block);
        endblock = new DXFBlockEnd(block);
        this.blocks.add(endblock);
        DXFDictionary dictionary = new DXFDictionary("", 0);
        this.objects.add(dictionary);
        DXFDictionary childDictionary = new DXFDictionary("ACAD_GROUP", dictionary.getHandle());
        dictionary.add(childDictionary);
    }

    public void setUnits(int unitsCode) {
        this.addHeaderVariable("$INSUNITS", 70, Integer.toString(unitsCode));
    }

    public void addHeaderVariable(String name, int code, String value) {
        DXFHeaderSegment headerSegment = new DXFHeaderSegment();
        headerSegment.addHeaderLine(name, code, value);
        this.header.add(headerSegment);
    }

    public DXFGraphics getGraphics() {
        return this.graphics;
    }

    public String toDXFString() {
        String returnString = new String();
        this.acadHeaderSegment.setHandleLimit(DXFDatabaseObject.getHandleCount());
        returnString = returnString + "999\n" + this.documentComment + "\n";
        returnString = returnString + this.header.toDXFString();
        returnString = returnString + this.classes.toDXFString();
        returnString = returnString + this.tables.toDXFString();
        returnString = returnString + this.blocks.toDXFString();
        returnString = returnString + this.entities.toDXFString();
        returnString = returnString + this.objects.toDXFString();
        returnString = returnString + "0\nEOF\n";
        return returnString;
    }

    public void addTable(DXFTable table) {
        this.tables.add(table);
    }

    public void addEntity(DXFEntity entity) {
        this.entities.add(entity);
    }

    public DXFStyle addStyle(DXFStyle style) {
        for(int i = 0; i < this.tables.size(); ++i) {
            DXFTable table = (DXFTable)this.tables.elementAt(i);
            if (table.name.equals("STYLE")) {
                int index = table.indexOf(style);
                if (index > 0) {
                    return (DXFStyle)table.elementAt(index);
                }
            }
        }

        DXFTable styleTable = null;

        for(int i = 0; i < this.tables.size(); ++i) {
            DXFTable table = (DXFTable)this.tables.elementAt(i);
            if (table.name.equals("STYLE")) {
                styleTable = table;
                break;
            }
        }

        if (styleTable == null) {
            styleTable = new DXFTable("STYLE");
            this.tables.add(styleTable);
        }

        styleTable.add(style);
        return style;
    }
}
