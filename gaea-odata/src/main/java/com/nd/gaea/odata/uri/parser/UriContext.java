package com.nd.gaea.odata.uri.parser;

import com.nd.gaea.odata.uri.UriInfoImpl;

import java.util.Stack;

public class UriContext {

  public static class LambdaVariables {
    public boolean isCollection;
    public String name;
  }

  public Stack<LambdaVariables> allowedLambdaVariables;
  public UriInfoImpl contextUriInfo;
  public boolean contextReadingFunctionParameters;

  public UriContext() {

    contextReadingFunctionParameters = false;
    allowedLambdaVariables = new Stack<LambdaVariables>();

  }
}