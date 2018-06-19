package com.xhtc.dbunit_tool.controller;

import com.xhtc.dbunit_tool.vo.TableNameVO;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 导出DataSource数据(DBUnit)
 *
 * @author xhtc
 * @create 2018-06-19 18:27
 **/

public class XmlController {

    private static SqlSession session;

    static {
        String resource = "application.properties";
        InputStream is = XmlController.class.getClassLoader().getResourceAsStream(resource);
        SqlSessionFactory sessionFactory = new SqlSessionFactoryBuilder().build(is);
        session = sessionFactory.openSession();
    }

    public static void main(String[] args) {
        List<String> tables = new ArrayList<>();
        tables.add("");
        batchCreateXml(tables);

    }

    private static void createXmlData(String tableName) {
        TableNameVO vo = new TableNameVO();
        System.out.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        System.out.println("</dataset>");
        vo.setTableName(tableName);
        List<Object> objects = session.selectList("mapper.mapper.getMap", vo);
        map2Xml(objects, tableName);
    }

    private static void batchCreateXml(List<String> tableNames) {
        TableNameVO vo = new TableNameVO();

        System.out.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        System.out.println("</dataset>");

        for (String tableName : tableNames) {
            vo.setTableName(tableName);
            List<Object> objects = session.selectList("mapper.mapper.getMap", vo);
            map2Xml(objects, tableName);
        }
    }

    /**
     * 转换成DBUnit的DataSource数据格式 <tablename key=value />
     *
     * @param objects
     * @param tableName
     */
    private static void map2Xml(List<Object> objects, String tableName) {
        if (objects == null || objects.size() == 0) {
            return;
        }
        for (Object o : objects) {
            Map<String, Object> map = (HashMap) o;
            StringBuilder result = new StringBuilder("<" + tableName + " ");
            for (Map.Entry<String, Object> entry : map.entrySet()) {
                result.append(entry.getKey()).append("=\"").append(entry.getValue().toString()).append("\" ");
            }
            result.append("/>");
            System.out.println(result);
        }

    }

}
