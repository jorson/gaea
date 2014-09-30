package gaea.foundation.core.repository;

/**
 * Created by Administrator on 14-9-30.
 */
public abstract class AbstractConnectionManager<TConn> {

    protected abstract TConn createConnection();
}
