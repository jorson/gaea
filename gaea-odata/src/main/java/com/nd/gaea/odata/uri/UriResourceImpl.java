package com.nd.gaea.odata.uri;

import com.nd.gaea.odata.api.uri.UriResource;
import com.nd.gaea.odata.api.uri.UriResourceKind;

/**
 * Covers Functionimports and BoundFunction in URI
 */
public abstract class UriResourceImpl implements UriResource {
  protected UriResourceKind kind;

  public UriResourceImpl(final UriResourceKind kind) {
    this.kind = kind;
  }

  @Override
  public UriResourceKind getKind() {
    return kind;
  }

}
