package kami.liyf.bean;

import java.util.Comparator;

public class ColumnInfo {

    /**
     * 序号
     */
    private Integer no = new Integer("0");

    /**
     * 中文名称(备注)
     */
    private String chineseName = "";

    /**
     * 英文名称(列名)
     */
    private String enName = "";

    /**
     * 类型
     */
    private String type = "";

    /**
     * 说明
     */
    private String remarks = "";

    public Integer getNo() {
        return no;
    }

    public void setNo(Integer no) {
        if(no == null){
            return;
        }
        this.no = no;
    }

    public String getChineseName() {
        return chineseName;
    }

    public void setChineseName(String chineseName) {
        if(chineseName == null){
            return;
        }
        this.chineseName = chineseName;
    }

    public String getEnName() {
        return enName;
    }

    public void setEnName(String enName) {
        if(enName == null){
            return;
        }
        this.enName = enName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        if(type == null){
            return;
        }
        this.type = type;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        if(remarks == null){
            return;
        }
        this.remarks = remarks;
    }


    public class SortByNo implements Comparator<ColumnInfo>{
        public int compare(ColumnInfo o1, ColumnInfo o2) {
            ColumnInfo parseO1 = (ColumnInfo)o1;
            ColumnInfo parseO2 = (ColumnInfo)o2;
            if(parseO1.getNo() == null && parseO2.getNo() == null){
                return 0;
            }else if(parseO2.getNo() == null){
                return 1;
            }else if(parseO1.getNo() == null){
                return -1;
            }else {
                return parseO1.getNo() - parseO2.getNo();
            }
        }

    }
}
