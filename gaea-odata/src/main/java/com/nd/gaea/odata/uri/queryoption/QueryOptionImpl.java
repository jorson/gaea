package com.nd.gaea.odata.uri.queryoption;

import com.nd.gaea.odata.api.uri.queryoption.QueryOption;

public class QueryOptionImpl implements QueryOption {
  private String name;
  private String text;

  @Override
  public String getName() {
    return name;
  }

  public QueryOptionImpl setName(final String name) {
    this.name = name;
    return this;
  }

  @Override
  public String getText() {
    return text;
  }

  public QueryOptionImpl setText(final String value) {
    text = value;
    return this;
  }

}
