package gaea.foundation.core.utils.odata;

/**
 * Created by Administrator on 14-9-15.
 */
public class ODataPagerQueryParam {
    private Integer pageIndex;
    private Integer pageSize;
    private Boolean needCount;
    private String orderBy;
    public ODataPagerQueryParam(){
        this.needCount = true;
    }
    public ODataPagerQueryParam(Integer pageIndex){
        this.pageIndex = pageIndex;
        this.needCount = true;
    }
    public ODataPagerQueryParam(Integer pageIndex,Integer pageSize){
        this.pageIndex = pageIndex;
        this.pageSize = pageSize;
        this.needCount = true;
    }
    public ODataPagerQueryParam(Integer pageIndex,Integer pageSize,String orderBy){
        this.pageIndex = pageIndex;
        this.pageSize = pageSize;
        this.needCount = true;
        this.orderBy = orderBy;
    }
    public ODataPagerQueryParam(Integer pageIndex,Integer pageSize,Boolean needCount,String orderBy){
        this.pageIndex = pageIndex;
        this.pageSize = pageSize;
        this.needCount = needCount;
        this.orderBy = orderBy;
    }

    public Integer getPageIndex() {
        return pageIndex;
    }

    public void setPageIndex(Integer pageIndex) {
        this.pageIndex = pageIndex;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Boolean getNeedCount() {
        return needCount;
    }

    public void setNeedCount(Boolean needCount) {
        this.needCount = needCount;
    }

    public String getOrderBy() {
        return orderBy;
    }

    public void setOrderBy(String orderBy) {
        this.orderBy = orderBy;
    }
}
