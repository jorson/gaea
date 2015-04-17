package com.nd.gaea.odata.uri.parser;

import com.nd.gaea.odata.antlr.UriLexer;
import com.nd.gaea.odata.antlr.UriParserParser;
import com.nd.gaea.odata.api.uri.UriInfo;
import com.nd.gaea.odata.api.uri.UriInfoKind;
import com.nd.gaea.odata.api.uri.queryoption.SystemQueryOptionKind;
import com.nd.gaea.odata.uri.UriInfoImpl;
import com.nd.gaea.odata.uri.queryoption.*;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.atn.PredictionMode;
import org.antlr.v4.runtime.misc.ParseCancellationException;

import java.util.List;

/**
 * 将URI中的OData字符串转换为可识别信息的主类
 * Created by wuhy on 14-11-19.
 */
public class Parser {


    int logLevel = 0;

    private enum ParserEntryRules {
        All, Batch, CrossJoin, Entity, ExpandItems, FilterExpression, Metadata, PathSegment, Orderby, Select
    };

    public Parser setLogLevel(final int logLevel) {
        this.logLevel = logLevel;
        return this;
    }

    public UriInfo parseUri(String input) throws UriParserException {

        boolean readQueryParameter = false;

        UriContext context = new UriContext();
        UriParseTreeVisitor uriParseTreeVisitor = new UriParseTreeVisitor(context);

        try {
            RawUri uri = UriDecoder.decodeUri(input, 0);

            String firstSegment = "";
            if (uri.pathSegmentListDecoded.size() > 0) {
                firstSegment = uri.pathSegmentListDecoded.get(0);
            }

            if (firstSegment.length() == 0) {
                readQueryParameter = true;
                context.contextUriInfo = new UriInfoImpl().setKind(UriInfoKind.service);
            } else {
                throw new IllegalArgumentException("illegal uri, please check sync");
            }

            if(readQueryParameter) {
                for(RawUri.QueryOption option : uri.queryOptionListDecoded) {
                    //处理Filter
                    if(option.name.equals(SystemQueryOptionKind.FILTER.toString())) {
                        UriParserParser.FilterExpressionEOFContext ctxFilterExpression =
                                (UriParserParser.FilterExpressionEOFContext) parseRule(option.value, ParserEntryRules.FilterExpression);

                        FilterOptionImpl filterOption =
                                (FilterOptionImpl) uriParseTreeVisitor.visitFilterExpressionEOF(ctxFilterExpression);

                        context.contextUriInfo.setSystemQueryOption(filterOption);
                    }
                    //处理OrderBy
                    else if (option.name.equals(SystemQueryOptionKind.ORDERBY.toString())) {
                        UriParserParser.OrderByEOFContext ctxOrderByExpression =
                                (UriParserParser.OrderByEOFContext) parseRule(option.value, ParserEntryRules.Orderby);

                        OrderByOptionImpl orderByOption =
                                (OrderByOptionImpl) uriParseTreeVisitor.visitOrderByEOF(ctxOrderByExpression);

                        context.contextUriInfo.setSystemQueryOption(orderByOption);
                    }
                    //处理Skip
                    else if (option.name.equals(SystemQueryOptionKind.SKIP.toString())) {
                        SkipOptionImpl skipOption = new SkipOptionImpl();
                        skipOption.setName(option.name);
                        skipOption.setText(option.value);
                        try {
                            skipOption.setValue(Integer.parseInt(option.value));
                        } catch (final NumberFormatException e) {
                            throw new UriParserSyntaxException("Illegal value of $skip option!", e,
                                    UriParserSyntaxException.MessageKeys.WRONG_VALUE_FOR_SYSTEM_QUERY_OPTION,
                                    option.name, option.value);
                        }
                        context.contextUriInfo.setSystemQueryOption(skipOption);
                    }
                    //处理Top
                    else if (option.name.equals(SystemQueryOptionKind.TOP.toString())) {
                        TopOptionImpl topOption = new TopOptionImpl();
                        topOption.setName(option.name);
                        topOption.setText(option.value);
                        try {
                            topOption.setValue(Integer.parseInt(option.value));
                        } catch (final NumberFormatException e) {
                            throw new UriParserSyntaxException("Illegal value of $top option!", e,
                                    UriParserSyntaxException.MessageKeys.WRONG_VALUE_FOR_SYSTEM_QUERY_OPTION,
                                    option.name, option.value);
                        }
                        context.contextUriInfo.setSystemQueryOption(topOption);
                    }
                    //处理Count
                    else if(option.name.equals(SystemQueryOptionKind.COUNT.toString())) {
                        CountOptionImpl countOption = new CountOptionImpl();
                        countOption.setName(option.name);
                        countOption.setText(option.value);
                        if (option.value.equalsIgnoreCase("true") || option.value.equalsIgnoreCase("false")) {
                            countOption.setValue(Boolean.parseBoolean(option.value));
                        } else {
                            throw new UriParserSyntaxException("Illegal value of $count option!",
                                    UriParserSyntaxException.MessageKeys.WRONG_VALUE_FOR_SYSTEM_QUERY_OPTION,
                                    option.name, option.value);
                        }
                        context.contextUriInfo.setSystemQueryOption(countOption);
                    }
                    else {
                        throw new UriParserSyntaxException("Unknown system query option!",
                                UriParserSyntaxException.MessageKeys.UNKNOWN_SYSTEM_QUERY_OPTION, option.name);
                    }
                }
            }
            return context.contextUriInfo;
        } catch (ParseCancellationException e) {
            throw e.getCause() instanceof UriParserException ?
                    (UriParserException) e.getCause() :
                    new UriParserSyntaxException("Syntax error", e, UriParserSyntaxException.MessageKeys.SYNTAX);
        }
    }


    public void showTokens(final String input, final List<? extends Token> list) {
        boolean first = true;
        System.out.println("input: " + input);
        String nL = "\n";
        String out = "[" + nL;
        for (Token token : list) {
            if (!first) {
                out += ",";
                first = false;
            }
            int index = token.getType();
            if (index != -1) {
                out += "\"" + token.getText() + "\"" + "     " + UriLexer.tokenNames[index] + nL;
            } else {
                out += "\"" + token.getText() + "\"" + "     " + index + nL;
            }
        }
        out += ']';
        System.out.println("tokens: " + out);
        return;
    }

    protected void addStage1ErrorStategy(final UriParserParser parser) {
        // Throw exception at first syntax error
        parser.setErrorHandler(new BailErrorStrategy());

    }

    protected void addStage1ErrorListener(final UriParserParser parser) {
        // No error logging to System.out or System.err, only exceptions used (depending on ErrorStrategy)
        parser.removeErrorListeners();
        parser.addErrorListener(new CheckFullContextListener());

    }

    protected void addStage2ErrorStategy(final UriParserParser parser) {
        // Throw exception at first syntax error
        parser.setErrorHandler(new BailErrorStrategy());
    }

    protected void addStage2ErrorListener(final UriParserParser parser) {
        // No error logging to System.out or System.err, only exceptions used (depending on ErrorStrategy)
        parser.removeErrorListeners();
    }


    private ParserRuleContext parseRule(final String input, final ParserEntryRules entryPoint)
            throws UriParserSyntaxException {
        UriParserParser parser = null;
        UriLexer lexer = null;
        ParserRuleContext ret = null;

        // Use 2 stage approach to improve performance
        // see https://github.com/antlr/antlr4/issues/192

        // stage = 1
        try {

            // create parser
            if (logLevel > 0) {
                lexer = new UriLexer(new ANTLRInputStream(input));
                showTokens(input, lexer.getAllTokens());
            }

            lexer = new UriLexer(new ANTLRInputStream(input));
            parser = new UriParserParser(new CommonTokenStream(lexer));

            // Set error strategy
            addStage1ErrorStategy(parser);

            // Set error collector
            addStage1ErrorListener(parser);

            // user the faster LL parsing
            parser.getInterpreter().setPredictionMode(PredictionMode.SLL);

            // parse
            switch (entryPoint) {
                case All:
                    ret = parser.allEOF();
                    break;
                case Batch:
                    ret = parser.batchEOF();
                    break;
                case CrossJoin:
                    ret = parser.crossjoinEOF();
                    break;
                case Metadata:
                    ret = parser.metadataEOF();
                    break;
                case PathSegment:
                    ret = parser.pathSegmentEOF();
                    break;
                case FilterExpression:
                    lexer.mode(Lexer.DEFAULT_MODE);
                    ret = parser.filterExpressionEOF();
                    break;
                case Orderby:
                    lexer.mode(Lexer.DEFAULT_MODE);
                    ret = parser.orderByEOF();
                    break;
                case ExpandItems:
                    lexer.mode(Lexer.DEFAULT_MODE);
                    ret = parser.expandItemsEOF();
                    break;
                case Entity:
                    ret = parser.entityEOF();
                    break;
                case Select:
                    ret = parser.selectEOF();
                    break;
                default:
                    break;

            }

        } catch (ParseCancellationException hardException) {
            // stage = 2
            try {

                // create parser
                lexer = new UriLexer(new ANTLRInputStream(input));
                parser = new UriParserParser(new CommonTokenStream(lexer));

                // Set error strategy
                addStage2ErrorStategy(parser);

                // Set error collector
                addStage2ErrorListener(parser);

                // Use the slower SLL parsing
                parser.getInterpreter().setPredictionMode(PredictionMode.LL);

                // parse
                switch (entryPoint) {
                    case All:
                        ret = parser.allEOF();
                        break;
                    case Batch:
                        ret = parser.batchEOF();
                        break;
                    case CrossJoin:
                        ret = parser.crossjoinEOF();
                        break;
                    case Metadata:
                        ret = parser.metadataEOF();
                        break;
                    case PathSegment:
                        ret = parser.pathSegmentEOF();
                        break;
                    case FilterExpression:
                        lexer.mode(Lexer.DEFAULT_MODE);
                        ret = parser.filterExpressionEOF();
                        break;
                    case Orderby:
                        lexer.mode(Lexer.DEFAULT_MODE);
                        ret = parser.orderByEOF();
                        break;
                    case ExpandItems:
                        lexer.mode(Lexer.DEFAULT_MODE);
                        ret = parser.expandItemsEOF();
                        break;
                    case Entity:
                        ret = parser.entityEOF();
                        break;
                    case Select:
                        ret = parser.selectEOF();
                        break;
                    default:
                        break;
                }

            } catch (final RecognitionException weakException) {
                throw new UriParserSyntaxException("Error in syntax", weakException,
                        UriParserSyntaxException.MessageKeys.SYNTAX);

                // exceptionOnStage = 2;
            }
        } catch (final RecognitionException hardException) {
            throw new UriParserSyntaxException("Error in syntax", hardException,
                    UriParserSyntaxException.MessageKeys.SYNTAX);
        }

        return ret;
    }
}
