package xyz.leo.lego.mybatis.generator;

import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.dom.java.Field;
import org.mybatis.generator.api.dom.java.TopLevelClass;
import org.mybatis.generator.internal.util.StringUtility;

import java.util.Properties;

/**
 * @author xuyangze
 * @date 2019/11/12 4:06 PM
 */
public class LegoCommentGenerator extends EmptyCommentGenerator {

    private static final String CLASS_DEFAULT_COMMENT = "Created by Lego Generator";

    private Properties properties;

    public LegoCommentGenerator() {
        properties = new Properties();
    }

    @Override
    public void addConfigurationProperties(Properties properties) {
        // 获取自定义的 properties
        this.properties.putAll(properties);
    }

    @Override
    public void addModelClassComment(TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        // 获取表注释
        String remarks = introspectedTable.getRemarks();
        if (!StringUtility.stringHasValue(remarks)) {
            remarks = CLASS_DEFAULT_COMMENT;
        }

        topLevelClass.addJavaDocLine("/**");
        topLevelClass.addJavaDocLine(" * " + remarks);
        topLevelClass.addJavaDocLine(" */");
    }

    @Override
    public void addFieldComment(Field field, IntrospectedTable introspectedTable, IntrospectedColumn introspectedColumn) {
        // 获取列注释
        String remarks = introspectedColumn.getRemarks();
        if (!StringUtility.stringHasValue(remarks)) {
            remarks = CLASS_DEFAULT_COMMENT;
        }

        field.addJavaDocLine("/**");
        field.addJavaDocLine(" * " + remarks);
        field.addJavaDocLine(" */");
    }
}