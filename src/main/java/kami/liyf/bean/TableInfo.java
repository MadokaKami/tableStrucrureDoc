package kami.liyf.bean;

import java.util.List;

public class TableInfo {

    private String tableEnName = "";

    private String tableComments = "";

    List<ColumnInfo> colList;

    public String getTableEnName() {
        return tableEnName;
    }

    public void setTableEnName(String tableEnName) {
        this.tableEnName = tableEnName;
    }

    public String getTableComments() {
        return tableComments;
    }

    public void setTableComments(String tableComments) {
        if(tableComments == null){
            return;
        }
        this.tableComments = tableComments;
    }

    public List<ColumnInfo> getColList() {
        return colList;
    }

    public void setColList(List<ColumnInfo> colList) {
        this.colList = colList;
    }
}
