package kami.liyf.dao;

import kami.liyf.bean.ColumnInfo;
import kami.liyf.bean.TableInfo;
import kami.liyf.util.Jdbc;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Repository
public class SelTableInfo {

    @Value("${tableName}")
    private String tableName;

    public List<ColumnInfo> findColumnInfo(){
        return findColumnInfo(tableName);
    }

    public List<ColumnInfo> findColumnInfo(String tableName){
        String sql = "select distinct b.COLUMN_NAME, b.comments, a.column_id," +
                "case when a.data_type = 'VARCHAR2'or (a.data_precision is not null and a.data_scale <> 0) then a.data_type " +
                "|| '(' || nvl2(a.data_precision,(a.data_scale || ',' || a.data_precision),data_length) || ')' " +
                "when a.data_precision is not null and a.data_scale = 0 then a.data_type || '(' || a.data_precision || ')'" +
                "else a.data_type end as data_type" +
                "  from user_tab_columns a, user_col_comments b" +
                " where a.column_name = b.column_name" +
                "   and a.table_name = upper(?)" +
                "   and b.table_name = upper(?)";
        ResultSet rs = Jdbc.executeQuery(sql,new Object[]{tableName, tableName});
        List<ColumnInfo> list = new ArrayList<ColumnInfo>();
        try{
            while (rs.next()){
                ColumnInfo columnInfo = new ColumnInfo();
                columnInfo.setEnName(rs.getString(1));
                columnInfo.setChineseName(rs.getString(2));
                columnInfo.setNo(rs.getInt(3));
                columnInfo.setType(rs.getString(4));
                if("PK_ID".equalsIgnoreCase(columnInfo.getEnName())){
                    columnInfo.setChineseName("主键id");
                    columnInfo.setRemarks("主键");
                }
                if("MAIN_PK_ID".equalsIgnoreCase(columnInfo.getEnName())){
                    columnInfo.setRemarks("流程主表主键");
                }
                if("REMARKS".equalsIgnoreCase(columnInfo.getEnName())){
                    columnInfo.setChineseName("备注");
                }
                if("CREATED_BY".equalsIgnoreCase(columnInfo.getEnName())){
                    columnInfo.setChineseName("创建人");
                }
                if("CREATED_DATE".equalsIgnoreCase(columnInfo.getEnName())){
                    columnInfo.setChineseName("创建时间");
                }
                if("LAST_UPDATED_BY".equalsIgnoreCase(columnInfo.getEnName())){
                    columnInfo.setChineseName("最后修改人");
                }
                if("LAST_UPDATED_DATE".equalsIgnoreCase(columnInfo.getEnName())){
                    columnInfo.setChineseName("最后修改时间");
                }
                if("RECORD_VERSION".equalsIgnoreCase(columnInfo.getEnName())){
                    columnInfo.setChineseName("版本号");
                }
                if("DELETED_FLAG".equalsIgnoreCase(columnInfo.getEnName())){
                    columnInfo.setChineseName("删除标记");
                }
                if("DELETED_BY".equalsIgnoreCase(columnInfo.getEnName())){
                    columnInfo.setChineseName("删除人");
                }
                if("DELETED_DATE".equalsIgnoreCase(columnInfo.getEnName())){
                    columnInfo.setChineseName("删除时间");
                }
                if("ATTRIBUTE1".equalsIgnoreCase(columnInfo.getEnName()) && "".equalsIgnoreCase(columnInfo.getChineseName())){
                    columnInfo.setChineseName("扩展字段1");
                }
                if("ATTRIBUTE2".equalsIgnoreCase(columnInfo.getEnName()) && "".equalsIgnoreCase(columnInfo.getChineseName())){
                    columnInfo.setChineseName("扩展字段2");
                }
                if("ATTRIBUTE3".equalsIgnoreCase(columnInfo.getEnName()) && "".equalsIgnoreCase(columnInfo.getChineseName())){
                    columnInfo.setChineseName("扩展字段3");
                }
                if("ATTRIBUTE4".equalsIgnoreCase(columnInfo.getEnName()) && "".equalsIgnoreCase(columnInfo.getChineseName())){
                    columnInfo.setChineseName("扩展字段4");
                }
                if("ATTRIBUTE5".equalsIgnoreCase(columnInfo.getEnName()) && "".equalsIgnoreCase(columnInfo.getChineseName())){
                    columnInfo.setChineseName("扩展字段5");
                }
                list.add(columnInfo);
            }
        }catch (SQLException e){
            e.printStackTrace();
            throw new RuntimeException("查询失败");
        }
        Collections.sort(list,new ColumnInfo().new SortByNo());
        return list;
    }

    public TableInfo findTableInfo(String tableName) throws Exception{
        String sql = "select comments from all_tab_comments where table_name= upper(?) ";
        ResultSet rs = Jdbc.executeQuery(sql,new Object[]{tableName});
        TableInfo tableInfo = new TableInfo();
        if(rs.next()){
            tableInfo.setTableEnName(tableName);
            tableInfo.setTableComments(rs.getString(1));
        }else{
            throw new Exception("表不存在");
        }
        List<ColumnInfo> list = findColumnInfo(tableName);
        tableInfo.setColList(list);
        return tableInfo;
    }

    public TableInfo findTableInfo() throws Exception{
        return findTableInfo(this.tableName);
    }
}
