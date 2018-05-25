package kami.liyf.util;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import kami.liyf.bean.ColumnInfo;
import kami.liyf.bean.TableInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.swing.*;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class WordUtil {

    @Autowired
    private Configuration freemarkerConfig;

    @Value("${outPath}")
    private String outPath;

    @Value("${wordName}")
    private String wordName;

    @Value("${templateName}")
    private String templateName;

    public void createDoc(TableInfo tableInfo) {
        Template t = null;
        try {
            // test.ftl为要装载的模板
            t = freemarkerConfig.getTemplate(templateName);
            t.setEncoding("utf-8");

        } catch(FileNotFoundException e){
            e.printStackTrace();
            JOptionPane.showConfirmDialog(null, e.getMessage(),"错误",JOptionPane.DEFAULT_OPTION,JOptionPane.ERROR_MESSAGE);
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 输出文档路径及名称
        File outFile = new File(outPath + File.separator + wordName);
        if (!outFile.getParentFile().exists()) {  //判断父目录路径是否存在，即test.txt前的I:\a\b\
            outFile.getParentFile().mkdirs(); //不存在则创建父目录
        }
        Writer out = null;

        try {
            out = new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream(outFile), "utf-8"));
        } catch(FileNotFoundException e){
            e.printStackTrace();
            JOptionPane.showConfirmDialog(null, e.getMessage(),"错误",JOptionPane.DEFAULT_OPTION,JOptionPane.ERROR_MESSAGE);
        } catch (Exception e1) {
            e1.printStackTrace();
        }

        try {
            t.process(tableInfo, out);
            out.close();
        } catch (TemplateException e) {
            e.printStackTrace();
        } catch(FileNotFoundException e){
            e.printStackTrace();
            JOptionPane.showConfirmDialog(null, e.getMessage(),"错误",JOptionPane.DEFAULT_OPTION,JOptionPane.ERROR_MESSAGE);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
