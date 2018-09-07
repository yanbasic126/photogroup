package com.photogroup.report;

import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

public class ReportGenerator {

    public void generate() {
        // 通过Freemaker的Configuration读取相应的ftl
        Configuration cfg = new Configuration(Configuration.VERSION_2_3_28);
        // 设定去哪里读取相应的ftl模板文件
        cfg.setClassForTemplateLoading(this.getClass(), "/reports");
        // 在模板文件目录中找到名称为name的文件
        Template template;
        try {
            template = cfg.getTemplate("basic.ftl");
            Map<String, Object> templateData = new HashMap<>();
            templateData.put("username", "Today is a beautiful day");

            try (StringWriter out = new StringWriter()) {

                template.process(templateData, out);
                System.out.println(out.getBuffer().toString());

                out.flush();
            } catch (TemplateException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (IOException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
    }

    public static void main(String[] args) {
        // TODO Auto-generated method stub
        new ReportGenerator().generate();
    }

}
