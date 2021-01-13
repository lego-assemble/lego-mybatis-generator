package xyz.leo.lego.mybatis.generator;

import org.mybatis.generator.api.GeneratedJavaFile;
import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.dom.java.Interface;
import org.mybatis.generator.api.dom.java.Method;
import org.mybatis.generator.api.dom.java.TopLevelClass;
import org.mybatis.generator.api.PluginAdapter;
import org.mybatis.generator.api.dom.xml.Element;
import org.mybatis.generator.api.dom.xml.TextElement;
import org.mybatis.generator.api.dom.xml.XmlElement;
import java.util.List;

/**
 * @author xuyangze
 * @date 2019/11/12 4:06 PM
 */
public class LombokPlugin extends PluginAdapter {

    private static final String DEFAULT_DELETED = "is_deleted";

    private static final String ENABLE = "true";

    /**
     * 软删除列名
     */
    private String deletedColumn;

    /**
     * 是否启用软删除
     */
    private boolean enableSoftDeleted;

    public boolean validate(List<String> list) {
        String enableSoftDeletedValue = properties.getProperty("enableSoftDeleted", "false");
        enableSoftDeleted = ENABLE.equals(enableSoftDeletedValue);
        if (enableSoftDeleted) {
            deletedColumn = properties.getProperty("deletedColumn", DEFAULT_DELETED);
        }

        return true;
    }

    @Override
    public List<GeneratedJavaFile> contextGenerateAdditionalJavaFiles() {
        return super.contextGenerateAdditionalJavaFiles();
    }

    @Override
    public boolean modelBaseRecordClassGenerated(TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        //添加domain的import
        topLevelClass.addImportedType("lombok.Data");

        //添加domain的注解
        topLevelClass.addAnnotation("@Data");
        return true;
    }

    @Override
    public boolean clientGenerated(Interface interfaze, TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        //Mapper文件的注释
        interfaze.addJavaDocLine("/**");
        interfaze.addJavaDocLine(" * Created by Lego Generator");
        interfaze.addJavaDocLine(" */");
        return true;
    }

    @Override
    public boolean modelSetterMethodGenerated(Method method, TopLevelClass topLevelClass, IntrospectedColumn introspectedColumn, IntrospectedTable introspectedTable, ModelClassType modelClassType) {
        //不生成getter
        return false;
    }

    @Override
    public boolean modelGetterMethodGenerated(Method method, TopLevelClass topLevelClass, IntrospectedColumn introspectedColumn, IntrospectedTable introspectedTable, ModelClassType modelClassType) {
        //不生成setter
        return false;
    }

    @Override
    public boolean sqlMapExampleWhereClauseElementGenerated(XmlElement element, IntrospectedTable introspectedTable) {
        if (checkEnableSoftDeleted(introspectedTable)) {
            for (Element child : element.getElements()) {
                if (child instanceof XmlElement && ((XmlElement) child).getName().equals("where")) {
                    TextElement element1 = new TextElement("and " + deletedColumn + " = 0");
                    ((XmlElement) child).getElements().add(element1);
                    break;
                }
            }
        }

        return true;
    }

    @Override
    public boolean sqlMapSelectByPrimaryKeyElementGenerated(XmlElement element, IntrospectedTable introspectedTable) {
        if (checkEnableSoftDeleted(introspectedTable)) {
            TextElement element1 = new TextElement("and " + deletedColumn + " = 0");
            element.getElements().add(element1);
        }

        return true;
    }

    @Override
    public boolean providerApplyWhereMethodGenerated(Method method, TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        return super.providerApplyWhereMethodGenerated(method, topLevelClass, introspectedTable);
    }

    @Override
    public boolean providerCountByExampleMethodGenerated(Method method, TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        return super.providerCountByExampleMethodGenerated(method, topLevelClass, introspectedTable);
    }

    @Override
    public boolean providerSelectByExampleWithBLOBsMethodGenerated(Method method, TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        return super.providerSelectByExampleWithBLOBsMethodGenerated(method, topLevelClass, introspectedTable);
    }

    @Override
    public boolean providerSelectByExampleWithoutBLOBsMethodGenerated(Method method, TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        return super.providerSelectByExampleWithoutBLOBsMethodGenerated(method, topLevelClass, introspectedTable);
    }

    @Override
    public boolean providerUpdateByExampleSelectiveMethodGenerated(Method method, TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        return super.providerUpdateByExampleSelectiveMethodGenerated(method, topLevelClass, introspectedTable);
    }

    @Override
    public boolean providerUpdateByExampleWithBLOBsMethodGenerated(Method method, TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        return super.providerUpdateByExampleWithBLOBsMethodGenerated(method, topLevelClass, introspectedTable);
    }

    @Override
    public boolean providerUpdateByExampleWithoutBLOBsMethodGenerated(Method method, TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        return super.providerUpdateByExampleWithoutBLOBsMethodGenerated(method, topLevelClass, introspectedTable);
    }

    @Override
    public boolean providerUpdateByPrimaryKeySelectiveMethodGenerated(Method method, TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        return super.providerUpdateByPrimaryKeySelectiveMethodGenerated(method, topLevelClass, introspectedTable);
    }

    private boolean checkEnableSoftDeleted(IntrospectedTable introspectedTable) {
        if (enableSoftDeleted) {
            IntrospectedColumn introspectedColumn = introspectedTable.getColumn(deletedColumn);
            if (null == introspectedColumn) {
                throw new RuntimeException("\ncreate map xml error for table: " + introspectedTable.getFullyQualifiedTableNameAtRuntime() + " which table not has soft deleted column: " + deletedColumn);
            }

            return true;
        }

        return false;
    }
}
