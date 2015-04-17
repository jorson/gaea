package com.nd.gaea.odata.uri.parser;

import com.nd.gaea.odata.antlr.UriLexer;
import com.nd.gaea.odata.antlr.UriParserBaseVisitor;
import com.nd.gaea.odata.antlr.UriParserParser;
import com.nd.gaea.odata.api.uri.UriInfoKind;
import com.nd.gaea.odata.api.uri.queryoption.expression.BinaryOperatorKind;
import com.nd.gaea.odata.property.SinglePropertyImpl;
import com.nd.gaea.odata.uri.UriInfoImpl;
import com.nd.gaea.odata.uri.UriResourceImpl;
import com.nd.gaea.odata.uri.UriResourcePropertyImpl;
import com.nd.gaea.odata.uri.queryoption.*;
import com.nd.gaea.odata.uri.queryoption.expression.*;
import org.antlr.v4.runtime.tree.ParseTree;

import java.util.ArrayList;
import java.util.List;

/**
 * URI转换的表达式树访问器
 * Created by jorson on 14-11-19.
 */
public class UriParseTreeVisitor extends UriParserBaseVisitor<Object> {

    private UriContext context;

    public UriParseTreeVisitor(final UriContext context) {
        this.context = context;
    }

    @Override
    protected Object aggregateResult(final Object aggregate, final Object nextResult) {
        if (aggregate != null) {
            return aggregate;
        } else {
            return nextResult;
        }
    }

    @Override
    public ExpressionImpl visitAltEquality(final UriParserParser.AltEqualityContext ctx) {
        BinaryImpl binary = new BinaryImpl();

        int tokenIndex = ctx.vO.getType();

        if (tokenIndex == UriLexer.EQ_ALPHA) {
            binary.setOperator(BinaryOperatorKind.EQ);
        } else {
            binary.setOperator(BinaryOperatorKind.NE);
        }
        binary.setLeftOperand((ExpressionImpl) ctx.vE1.accept(this));
        binary.setRightOperand((ExpressionImpl) ctx.vE2.accept(this));

        return binary;
    }

    @Override
    public ExpressionImpl visitAltComparism(final UriParserParser.AltComparismContext ctx) {
        BinaryImpl binary = new BinaryImpl();

        int tokenIndex = ctx.vO.getType();

        if (tokenIndex == UriLexer.GT) {
            binary.setOperator(BinaryOperatorKind.GT);
        } else if (tokenIndex == UriLexer.GE) {
            binary.setOperator(BinaryOperatorKind.GE);
        } else if (tokenIndex == UriLexer.LT) {
            binary.setOperator(BinaryOperatorKind.LT);
        } else if (tokenIndex == UriLexer.LE) {
            binary.setOperator(BinaryOperatorKind.LE);
        }

        binary.setLeftOperand((ExpressionImpl) ctx.vE1.accept(this));
        binary.setRightOperand((ExpressionImpl) ctx.vE2.accept(this));
        return binary;
    }

    @Override
    public Object visitAltContain(final UriParserParser.AltContainContext ctx) {
        BinaryImpl binary = new BinaryImpl();

        binary.setOperator(BinaryOperatorKind.IN);
        binary.setLeftOperand((ExpressionImpl)ctx.vE1.accept(this));
        if(ctx.vlSS.size() == 0) {
            throw new IllegalArgumentException("argument miss when visit IN");
        } else {
            MultiLiteralImpl multiLiteral = new MultiLiteralImpl();
            List<String> values = new ArrayList<>();
            for(UriParserParser.PrimitiveLiteralContext pCtx : ctx.vlSS) {
                LiteralImpl literal = (LiteralImpl) pCtx.accept(this);
                if(multiLiteral.getType() == null && literal.getType() != null) {
                    multiLiteral.setType(literal.getType());
                }
                values.add(literal.getText());
            }
            multiLiteral.setItems(values);
            binary.setRightOperand((ExpressionImpl)multiLiteral);
        }
        return binary;
    }

    @Override
    public ExpressionImpl visitAltAnd(final UriParserParser.AltAndContext ctx) {
        BinaryImpl binary = new BinaryImpl();

        binary.setOperator(BinaryOperatorKind.AND);
        binary.setLeftOperand((ExpressionImpl) ctx.vE1.accept(this));
        binary.setRightOperand((ExpressionImpl) ctx.vE2.accept(this));

        return binary;
    }

    @Override
    public ExpressionImpl visitAltOr(final UriParserParser.AltOrContext ctx) {
        BinaryImpl binary = new BinaryImpl();

        binary.setOperator(BinaryOperatorKind.OR);
        binary.setLeftOperand((ExpressionImpl) ctx.vE1.accept(this));
        binary.setRightOperand((ExpressionImpl) ctx.vE2.accept(this));

        return binary;
    }

    @Override
    public Object visitFilter(final UriParserParser.FilterContext ctx) {

        FilterOptionImpl filter = new FilterOptionImpl().setExpression((ExpressionImpl) ctx.children.get(2).accept(this));
        return filter;
    }

    @Override
    public Object visitFilterExpressionEOF(final UriParserParser.FilterExpressionEOFContext ctx) {

        FilterOptionImpl filter = new FilterOptionImpl().setExpression((ExpressionImpl) ctx.children.get(0).accept(this));
        return filter;
    }

    @Override
    public Object visitMemberExpr(final UriParserParser.MemberExprContext ctx){
        UriInfoImpl uriInfoImplpath = new UriInfoImpl().setKind(UriInfoKind.resource);

        if (ctx.vPs != null) {
            // save the context
            UriInfoImpl backupUriInfoPath = context.contextUriInfo;
            context.contextUriInfo = uriInfoImplpath;

            ctx.vPs.accept(this);
            context.contextUriInfo = backupUriInfoPath;
        }

        MemberImpl ret = new MemberImpl();
        ret.setResourcePath(uriInfoImplpath);
        return ret;
    }

    @Override
    public Object visitNullrule(final UriParserParser.NullruleContext ctx) {
        return new LiteralImpl().setText("null");
    }

    @Override
    public Object visitOrderBy(final UriParserParser.OrderByContext ctx) {

        OrderByOptionImpl orderBy = new OrderByOptionImpl();

        for (UriParserParser.OrderByItemContext item : ((UriParserParser.OrderListContext) ctx.getChild(2)).vlOI) {
            OrderByItemImpl oItem = (OrderByItemImpl) item.accept(this);
            orderBy.addOrder(oItem);
        }

        return orderBy;
    }

    @Override
    public Object visitOrderByEOF(final UriParserParser.OrderByEOFContext ctx) {

        OrderByOptionImpl orderBy = new OrderByOptionImpl();

        for (UriParserParser.OrderByItemContext item : ((UriParserParser.OrderListContext) ctx.getChild(0)).vlOI) {
            OrderByItemImpl oItem = (OrderByItemImpl) item.accept(this);
            orderBy.addOrder(oItem);
        }

        return orderBy;
    }

    @Override
    public Object visitOrderByItem(final UriParserParser.OrderByItemContext ctx) {
        OrderByItemImpl oItem = new OrderByItemImpl();
        if (ctx.vD != null) {
            oItem.setDescending(true);
        }

        oItem.setExpression((ExpressionImpl) ctx.vC.accept(this));
        return oItem;
    }

    @Override
    public Object visitSkip(final UriParserParser.SkipContext ctx) {
        SkipOptionImpl skiptoken = new SkipOptionImpl();

        String text = ctx.children.get(2).getText();

        return skiptoken.setValue(Integer.parseInt(text)).setText(text);
    }

    @Override
    public Object visitSkiptoken(final UriParserParser.SkiptokenContext ctx) {
        SkipTokenOptionImpl skiptoken = new SkipTokenOptionImpl();

        String text = ctx.children.get(2).getText();

        return skiptoken.setValue(text).setText(text);
    }

    @Override
    public Object visitTop(final UriParserParser.TopContext ctx) {
        TopOptionImpl top = new TopOptionImpl();

        String text = ctx.children.get(2).getText();

        return top.setValue(Integer.parseInt(text)).setText(text);
    }

    @Override
    public Object visitBooleanNonCase(final UriParserParser.BooleanNonCaseContext ctx) {
        String text = ctx.getText().toLowerCase();

        if (text.equals("false")) {
            return new LiteralImpl().setText("false").setType(Boolean.class);
        }
        return new LiteralImpl().setText("true").setType(Boolean.class);
    }

    @Override
    public Object visitNaninfinity(final UriParserParser.NaninfinityContext ctx) {
        return new LiteralImpl()
                .setType(Double.class)
                .setText(ctx.getText());
    }

    @Override
    public Object visitInteger(final UriParserParser.IntegerContext ctx) {
        return new LiteralImpl()
                .setType(Integer.class)
                .setText(ctx.getText());
    }

    @Override
    public Object visitPrimitiveLiteral(final UriParserParser.PrimitiveLiteralContext ctx) {
        ParseTree child1 = ctx.children.get(0);

        if (child1 instanceof UriParserParser.EnumLitContext
                || child1 instanceof UriParserParser.BooleanNonCaseContext
                || child1 instanceof UriParserParser.NullruleContext
                || child1 instanceof UriParserParser.NaninfinityContext
                || child1 instanceof UriParserParser.IntegerContext) {
            return child1.accept(this);
        }
        return new LiteralImpl().setText(ctx.getText());
    }

    @Override
    public Object visitPathSegment(final UriParserParser.PathSegmentContext ctx) {

        String odi = ctx.vODI.getText();
        UriResourcePropertyImpl simpleResource = new UriResourcePropertyImpl()
                .setProperty(new SinglePropertyImpl(odi));
        context.contextUriInfo.addResourcePart(simpleResource);

        UriResourceImpl pathInfoSegment = (UriResourceImpl) context.contextUriInfo.getLastResourcePart();
        return pathInfoSegment;
    }

    @Override
    public Object visitPathSegments(final UriParserParser.PathSegmentsContext ctx) {
        // path segment
        for (UriParserParser.PathSegmentContext it : ctx.vlPS) {
            it.accept(this);
        }

        // const segment
        if (ctx.vCS != null) {
            ctx.vCS.accept(this);
        }
        return null;
    }
}
