package gaea.foundation.core.repository;

import gaea.foundation.core.utils.PropertiesLoaderUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * Created by Administrator on 14-9-30.
 */
public abstract class AbstractConnectionManager<TConn> {

    private Map<String, TConn> nameDic = new HashMap<String, TConn>();
    private Object syncRoot = new Object();

    private String propertiesPath = null;
    private static Properties properties;

    public AbstractConnectionManager(String propertiesFilePath) {
        this.propertiesPath = propertiesFilePath;
        try {
            properties = PropertiesLoaderUtils.loadProperties(propertiesFilePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取连接配置
     * @param sectionName 连接配置的名称
     * @return 连接配置
     */
    public TConn getConnection(String sectionName) {
        TConn conn = nameDic.get(sectionName);
        if(null == conn) {
            synchronized (syncRoot) {
                conn = nameDic.get(sectionName);
                if(null == conn) {
                    String config = properties.getProperty(sectionName);
                    conn = createConnection(config);
                    nameDic.put(sectionName, conn);
                }
            }
        }
        return conn;
    }

    protected abstract TConn createConnection(String config);
}
