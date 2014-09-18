package gaea.foundation.core.repository.shard;

/**
 * 分表的信息
 *
 * @author wuhy
 */
public class PartitionInfo {

    /**
     * 逻辑表名，不能用于查询
     */
    private String tableName;
    /**
     * 真正使用的表名，在查询语句中使用的表名
     */
    private String readTableName;

    public PartitionInfo(String tableName, String readTableName) {
        this.tableName = tableName;
        this.readTableName = readTableName;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getReadTableName() {
        return readTableName;
    }

    public void setReadTableName(String readTableName) {
        this.readTableName = readTableName;
    }
}
