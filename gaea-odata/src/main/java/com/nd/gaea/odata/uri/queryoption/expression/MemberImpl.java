package com.nd.gaea.odata.uri.queryoption.expression;

import com.nd.gaea.odata.api.ODataApplicationException;
import com.nd.gaea.odata.api.property.SingleProperty;
import com.nd.gaea.odata.api.uri.UriInfoResource;
import com.nd.gaea.odata.api.uri.UriResource;
import com.nd.gaea.odata.api.uri.queryoption.expression.ExpressionVisitException;
import com.nd.gaea.odata.api.uri.queryoption.expression.ExpressionVisitor;
import com.nd.gaea.odata.api.uri.queryoption.expression.Member;
import com.nd.gaea.odata.uri.UriInfoImpl;
import com.nd.gaea.odata.uri.UriResourceImpl;
import com.nd.gaea.odata.uri.UriResourcePropertyImpl;

import java.util.List;

/**
 * Created by Administrator on 14-11-19.
 */
public class MemberImpl extends ExpressionImpl implements Member {

    private UriInfoResource path;
    private SingleProperty property;

    @Override
    public SingleProperty getProperty() {
        UriInfoImpl uriInfo = (UriInfoImpl) path;
        UriResourceImpl lastResourcePart = (UriResourceImpl) uriInfo.getLastResourcePart();

        if(lastResourcePart != null && lastResourcePart instanceof UriResourcePropertyImpl) {
            this.property = ((UriResourcePropertyImpl) lastResourcePart).getProperty();
        }

        return this.property;
    }

    public Member setResourcePath(final UriInfoResource pathSegments) {
        this.path = pathSegments;
        setLastProperty();
        return this;
    }

    private void setLastProperty() {
        if(this.path != null) {
            List<UriResource> resources = this.path.getUriResourceParts();
            if(resources.size() > 0) {
                UriResource resource = resources.get(resources.size() - 1);
                if(resource instanceof UriResourcePropertyImpl) {
                    this.property = ((UriResourcePropertyImpl)resource).getProperty();
                }
            }
        }
    }

    @Override
    public <T> T accept(ExpressionVisitor<T> visitor) throws ExpressionVisitException, ODataApplicationException {
        return visitor.visitMember(path);
    }
}
