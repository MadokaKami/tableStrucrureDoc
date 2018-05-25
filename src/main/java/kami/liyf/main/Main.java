package kami.liyf.main;

import kami.liyf.bean.TableInfo;
import kami.liyf.config.SpringBeanConfig;
import kami.liyf.dao.SelTableInfo;
import kami.liyf.util.Jdbc;
import kami.liyf.util.WordUtil;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import javax.swing.*;

public class Main {

    public static void main(String[] args) {
        AnnotationConfigApplicationContext configApplicationContext = new AnnotationConfigApplicationContext(SpringBeanConfig.class);
        SelTableInfo selTableInfo = (SelTableInfo)configApplicationContext.getBean("selTableInfo");
        TableInfo tableInfo = null;
        try{
            tableInfo = selTableInfo.findTableInfo();
        }catch (Exception e){
            e.printStackTrace();
            JOptionPane.showConfirmDialog(null, e.getMessage(),"错误",JOptionPane.DEFAULT_OPTION,JOptionPane.ERROR_MESSAGE);
            return;
        }finally {
            Jdbc.closeConnection();
        }
        WordUtil wordUtil = (WordUtil)configApplicationContext.getBean("wordUtil");
        wordUtil.createDoc(tableInfo);
        JOptionPane.showConfirmDialog(null, "生成成功!","successful",JOptionPane.DEFAULT_OPTION,JOptionPane.INFORMATION_MESSAGE);
    }
}
