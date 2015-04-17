package com.nd.gaea.odata.api.uri.queryoption.expression;


import java.util.List;

public interface Enumeration extends Expression {

    public List<String> getValues();
/*
  public EdmEnumType getType();*/

}
