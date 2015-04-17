package com.nd.gaea.odata.uri;

import com.nd.gaea.odata.api.ODataRuntimeException;
import com.nd.gaea.odata.api.uri.*;
import com.nd.gaea.odata.api.uri.queryoption.*;
import com.nd.gaea.odata.uri.queryoption.CustomQueryOptionImpl;
import com.nd.gaea.odata.uri.queryoption.QueryOptionImpl;
import com.nd.gaea.odata.uri.queryoption.SystemQueryOptionImpl;

import java.util.*;

public class UriInfoImpl implements UriInfo {

    private UriInfoKind kind;

    private List<CustomQueryOptionImpl> customQueryOptions = new ArrayList<CustomQueryOptionImpl>();
    private HashMap<SystemQueryOptionKind, SystemQueryOption> systemQueryOptions =
            new HashMap<SystemQueryOptionKind, SystemQueryOption>();
    private HashMap<String, String> aliasToValue = new HashMap<String, String>();

    private String fragment;

    private UriResource lastResourcePart;
    private List<UriResource> pathParts = new ArrayList<UriResource>();

    @Override
    public UriInfoAll asUriInfoAll() {
        return this;
    }

    @Override
    public UriInfoBatch asUriInfoBatch() {
        return this;
    }

    @Override
    public UriInfoCrossjoin asUriInfoCrossjoin() {
        return this;
    }

    @Override
    public UriInfoEntityId asUriInfoEntityId() {
        return this;
    }

    @Override
    public UriInfoMetadata asUriInfoMetadata() {
        return this;
    }

    @Override
    public UriInfoResource asUriInfoResource() {
        return this;
    }

    @Override
    public List<UriResource> getUriResourceParts() {
        List<UriResource> returnList = new ArrayList<UriResource>();
        for (UriResource item : pathParts) {
            returnList.add(item);
        }
        return Collections.unmodifiableList(returnList);
    }

    public UriInfoImpl addResourcePart(final UriResourceImpl uriPathInfo) {
        pathParts.add(uriPathInfo);
        lastResourcePart = uriPathInfo;
        return this;
    }

    @Override
    public List<CustomQueryOption> getCustomQueryOptions() {
        List<CustomQueryOption> retList = new ArrayList<CustomQueryOption>();
        for (CustomQueryOptionImpl item : customQueryOptions) {
            retList.add(item);
        }
        return retList;
    }

    @Override
    public String getValueForAlias(String alias) {
        return aliasToValue.get(alias);
    }

    @Override
    public ExpandOption getExpandOption() {
        return (ExpandOption) systemQueryOptions.get(SystemQueryOptionKind.EXPAND);
    }

    @Override
    public FilterOption getFilterOption() {
        return (FilterOption) systemQueryOptions.get(SystemQueryOptionKind.FILTER);
    }

    @Override
    public FormatOption getFormatOption() {
        return (FormatOption) systemQueryOptions.get(SystemQueryOptionKind.FORMAT);
    }

    @Override
    public IdOption getIdOption() {
        return (IdOption) systemQueryOptions.get(SystemQueryOptionKind.ID);
    }

    @Override
    public CountOption getCountOption() {
        return (CountOption) systemQueryOptions.get(SystemQueryOptionKind.COUNT);
    }

    @Override
    public UriInfoKind getKind() {
        return kind;
    }

    public UriInfoImpl setKind(final UriInfoKind kind) {
        this.kind = kind;
        return this;
    }

    public UriResource getLastResourcePart() {
        return lastResourcePart;
    }

    @Override
    public OrderByOption getOrderByOption() {
        return (OrderByOption) systemQueryOptions.get(SystemQueryOptionKind.ORDERBY);
    }

    @Override
    public SearchOption getSearchOption() {

        return (SearchOption) systemQueryOptions.get(SystemQueryOptionKind.SEARCH);
    }

    @Override
    public SelectOption getSelectOption() {
        return (SelectOption) systemQueryOptions.get(SystemQueryOptionKind.SELECT);
    }

    @Override
    public SkipOption getSkipOption() {
        return (SkipOption) systemQueryOptions.get(SystemQueryOptionKind.SKIP);
    }

    @Override
    public SkipTokenOption getSkipTokenOption() {
        return (SkipTokenOption) systemQueryOptions.get(SystemQueryOptionKind.SKIPTOKEN);
    }

    @Override
    public TopOption getTopOption() {
        return (TopOption) systemQueryOptions.get(SystemQueryOptionKind.TOP);
    }

    public UriInfoImpl setQueryOptions(final List<QueryOptionImpl> list) {

        for (QueryOptionImpl item : list) {
            if (item instanceof SystemQueryOptionImpl) {
                setSystemQueryOption((SystemQueryOptionImpl) item);
            } else if (item instanceof CustomQueryOptionImpl) {
                addCustomQueryOption(item);
            }
        }
        return this;
    }

    public void addCustomQueryOption(final QueryOptionImpl item) {
        customQueryOptions.add((CustomQueryOptionImpl) item);
        if (item.getName().startsWith("@")) {
            aliasToValue.put(item.getName(), item.getText());
        }
    }

    public UriInfoImpl setSystemQueryOption(final SystemQueryOptionImpl systemOption) {

        if (systemOption.getKind() == SystemQueryOptionKind.EXPAND) {
            systemQueryOptions.put(SystemQueryOptionKind.EXPAND, systemOption);
        } else if (systemOption.getKind() == SystemQueryOptionKind.FILTER) {
            systemQueryOptions.put(SystemQueryOptionKind.FILTER, systemOption);
        } else if (systemOption.getKind() == SystemQueryOptionKind.FORMAT) {
            systemQueryOptions.put(SystemQueryOptionKind.FORMAT, systemOption);
        } else if (systemOption.getKind() == SystemQueryOptionKind.ID) {
            systemQueryOptions.put(SystemQueryOptionKind.ID, systemOption);
        } else if (systemOption.getKind() == SystemQueryOptionKind.COUNT) {
            systemQueryOptions.put(SystemQueryOptionKind.COUNT, systemOption);
        } else if (systemOption.getKind() == SystemQueryOptionKind.ORDERBY) {
            systemQueryOptions.put(SystemQueryOptionKind.ORDERBY, systemOption);
        } else if (systemOption.getKind() == SystemQueryOptionKind.SEARCH) {
            systemQueryOptions.put(SystemQueryOptionKind.SEARCH, systemOption);
        } else if (systemOption.getKind() == SystemQueryOptionKind.SELECT) {
            systemQueryOptions.put(SystemQueryOptionKind.SELECT, systemOption);
        } else if (systemOption.getKind() == SystemQueryOptionKind.SKIP) {
            systemQueryOptions.put(SystemQueryOptionKind.SKIP, systemOption);
        } else if (systemOption.getKind() == SystemQueryOptionKind.SKIPTOKEN) {
            systemQueryOptions.put(SystemQueryOptionKind.SKIPTOKEN, systemOption);
        } else if (systemOption.getKind() == SystemQueryOptionKind.TOP) {
            systemQueryOptions.put(SystemQueryOptionKind.TOP, systemOption);
        } else if (systemOption.getKind() == SystemQueryOptionKind.LEVELS) {
            systemQueryOptions.put(SystemQueryOptionKind.LEVELS, systemOption);
        } else {
            throw new ODataRuntimeException("Unsupported System Query Option: " + systemOption.getName());
        }

        return this;
    }

    @Override
    public UriInfoService asUriInfoService() {
        return this;
    }

    @Override
    public String getFragment() {
        return fragment;
    }

    public UriInfoImpl setFragment(final String fragment) {
        this.fragment = fragment;
        return this;
    }

    public void removeResourcePart(final int index) {
        pathParts.remove(index);
    }

    @Override
    public Collection<SystemQueryOption> getSystemQueryOptions() {
        return Collections.unmodifiableCollection(systemQueryOptions.values());
    }

}