package kami.liyf.config;

import kami.liyf.util.Jdbc;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.*;

@ComponentScan("kami.liyf")
//@PropertySource(value = "file:#{systemProperties['user.dir']}#{systemProperties['file.separator']}my-ini.properties",encoding = "utf-8")
@ImportResource("classpath:spring.xml")
@Configuration
public class SpringBeanConfig {

    @Value("${ftlpath}")
    private String ftlpath;

    @Bean
    public freemarker.template.Configuration freemarkerConfig(){
        freemarker.template.Configuration configuration = new freemarker.template.Configuration();
        // 设置模本装置方法和路径,FreeMarker支持多种模板装载方法。可以重servlet，classpath，数据库装载，
        // 这里我们的模板是放在ftl文件夹下面
        configuration.setDefaultEncoding("UTF-8");
        configuration.setClassForTemplateLoading(this.getClass(),ftlpath);
        return configuration;
    }
    @Bean
    public String test(@Value("file:#{systemProperties['user.dir']}#{systemProperties['file.separator']}my-ini.properties") String t){
        System.out.println(t);
        return t;
    }
    @Bean(initMethod = "getConnection",destroyMethod = "closeConnection")
    public Jdbc jdbc(@Value("${jdbc.username}")String user, @Value("${jdbc.password}")String password, @Value("${jdbc.url}")String url){
        Jdbc jdbc = new Jdbc();
        jdbc.setPassword(password);
        jdbc.setUrl(url);
        jdbc.setUser(user);
        return jdbc;
    }
}
