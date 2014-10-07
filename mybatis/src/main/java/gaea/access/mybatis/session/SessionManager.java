package gaea.access.mybatis.session;

import gaea.foundation.core.repository.AbstractConnectionManager;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.Reader;
import java.util.logging.LogManager;

/**
 * 通用的MyBatis的Session管理器
 */
public class SessionManager extends AbstractConnectionManager<SqlSessionFactory> {

    private static final Logger logger = LoggerFactory.getLogger(SessionManager.class);

    /**
     * 创建连接,返回会话工厂
     * @return 返回当前连接的会话工厂
     */
    @Override
    protected SqlSessionFactory createConnection(String config) {
        Reader reader = null;
        try {
            reader = Resources.getResourceAsReader(config);
            SqlSessionFactory factory = new SqlSessionFactoryBuilder()
                    .build(reader);
            return factory;
        } catch (IOException e) {
            if(logger.isErrorEnabled()) {
                logger.error("load mybatis config file error");
            }
        }
        return null;
    }
}
