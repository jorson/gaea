package com.nd.gaea.odata.uri.queryoption;


import com.nd.gaea.odata.api.uri.queryoption.OrderByItem;
import com.nd.gaea.odata.api.uri.queryoption.OrderByOption;
import com.nd.gaea.odata.api.uri.queryoption.SystemQueryOptionKind;

import java.util.ArrayList;
import java.util.List;

public class OrderByOptionImpl extends SystemQueryOptionImpl implements OrderByOption {

    private List<OrderByItemImpl> orders = new ArrayList<OrderByItemImpl>();

    public OrderByOptionImpl() {
        setKind(SystemQueryOptionKind.ORDERBY);
    }

    @Override
    public List<OrderByItem> getOrders() {
        List<OrderByItem> retList = new ArrayList<OrderByItem>();
        for (OrderByItemImpl item : orders) {
            retList.add(item);
        }
        return retList;
    }

    public OrderByOptionImpl addOrder(final OrderByItemImpl order) {
        orders.add(order);
        return this;
    }

}
