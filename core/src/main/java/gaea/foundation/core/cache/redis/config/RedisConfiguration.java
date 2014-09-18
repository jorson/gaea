package gaea.foundation.core.cache.redis.config;

/**
 * 请在这里输入说明
 *
 * @author wuhy
 */
public class RedisConfiguration {

    public static final int DEFAULT_TIMEOUT = 2000;
    public static final int DEFAULT_DATABASE = 0;
    public static final int DEFAULT_MAX_TOTAL = 8;
    public static final int DEFAULT_MAX_IDLE = 8;
    public static final int DEFAULT_MIN_IDLE = 0;

    private String name;
    private int maxTotal;
    private int maxIdle;
    private int minIdle;
    private String host;
    private int port;
    private int timeout;
    private String password;
    private int database;
    private String clientName;
    private boolean isDefault;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getMaxTotal() {
        if (maxTotal <= 0) {
            maxTotal = DEFAULT_MAX_TOTAL;
        }
        return maxTotal;
    }

    public void setMaxTotal(int maxTotal) {
        this.maxTotal = maxTotal;
    }

    public int getMaxIdle() {
        if (maxIdle <= 0) {
            maxIdle = DEFAULT_MAX_IDLE;
        }
        return maxIdle;
    }

    public void setMaxIdle(int maxIdle) {
        this.maxIdle = maxIdle;
    }

    public int getMinIdle() {
        if (minIdle <= 0) {
            minIdle = DEFAULT_MIN_IDLE;
        }
        return minIdle;
    }

    public void setMinIdle(int minIdle) {
        this.minIdle = minIdle;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public int getTimeout() {
        if (timeout <= 0) {
            timeout = DEFAULT_TIMEOUT;
        }
        return timeout;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getDatabase() {
        if (database <= 0) {
            database = DEFAULT_DATABASE;
        }
        return database;
    }

    public void setDatabase(int database) {
        this.database = database;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public boolean isDefault() {
        return isDefault;
    }

    public void setDefault(boolean isDefault) {
        this.isDefault = isDefault;
    }
}
