package com.nd.gaea.odata.processor;

import com.nd.gaea.core.repository.query.criterion.*;
import com.nd.gaea.odata.api.ODataException;
import com.nd.gaea.odata.api.processor.ODataQuerySupport;
import com.nd.gaea.odata.api.processor.QueryOptions;
import com.nd.gaea.odata.api.processor.QueryProcessor;
import com.nd.gaea.odata.api.uri.queryoption.*;
import com.nd.gaea.odata.api.uri.queryoption.expression.*;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Stack;

/**
 * 默认查询处理器
 * Created by Administrator on 2014-11-21.
 */
public class DefaultQueryProcessor implements QueryProcessor<ODataQuerySupport> {

    /**
     * 默认空构造函数
     */
    public DefaultQueryProcessor() {
    }

    /**
     * 对查询项进行处理
     * @param options 待处理的查询项
     * @param filterClass 查询实体类型
     * @return 根据查询项目生成的QuerySupport对象
     * @see com.nd.gaea.core.repository.query.QuerySupport
     */
    @Override
    public ODataQuerySupport process(QueryOptions options, Class<?> filterClass) {
        if(options == null) {
            throw new NullPointerException("options");
        }
        if(filterClass == null) {
            throw new NullPointerException("filterClass");
        }

        ODataQuerySupport support = new ODataQuerySupport();
        //开始处理
        ODataOptionFlowHandler filterHandler = new FilterOptionHandler(filterClass);
        ODataOptionFlowHandler skipHandler = new SkipOptionHandler();
        ODataOptionFlowHandler topHandler = new TopOptionHandler();
        ODataOptionFlowHandler orderByHandler = new OrderByOptionHandler(filterClass);
        ODataOptionFlowHandler countHandler = new CountHandler();

        filterHandler.setNextFlowHandler(skipHandler);
        skipHandler.setNextFlowHandler(topHandler);
        topHandler.setNextFlowHandler(orderByHandler);
        orderByHandler.setNextFlowHandler(countHandler);

        filterHandler.handler(options, support);
        return support;
    }

    public abstract class ODataOptionFlowHandler {

        protected ODataOptionFlowHandler nextHandler;

        protected SimpleDateFormat[] dateFormats = new SimpleDateFormat[] {
                new SimpleDateFormat("yyyy-MM-dd"),
                new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'"),
                new SimpleDateFormat("yyyy-MM-dd'T'HH:mm'Z'")
        };


        public void setNextFlowHandler(ODataOptionFlowHandler nextHandler) {
            if(nextHandler == null) {
                throw new NullPointerException("nextHandler");
            }
            this.nextHandler = nextHandler;
        }

        /**
         * 检查OData表达式中数据及数据类型正确性
         */
        protected Field validateProperty(String name, Class<?> filterClass,
                                         List<Exception> exceptions) {
            Field field = null;

            try {
                field = filterClass.getDeclaredField(name);
            } catch (NoSuchFieldException e1) {
                exceptions.add(new NoSuchFieldException("can't found field " + e1.getMessage()
                        + ", in class " + filterClass.getSimpleName()));
            } catch (Exception e) {
                exceptions.add(new IllegalArgumentException("some error occurs when get property " + name));
            }
            return field;
        }

        /**
         * 将指定类型的Value转换
         */
        protected Object transferProperty(String value, Class<?> filedClass,
                                          List<Exception> exceptions) {
            Object realObj = null;

            try {
                //如果是字符串类型
                if(filedClass.equals(String.class)) {
                    value = value.replace("'", "");
                }
                //如果是日期类型
                if(filedClass.equals(Date.class)) {
                    for(SimpleDateFormat format : dateFormats) {
                        try {
                            realObj = format.parse(value);
                        } catch(Exception e) {
                            //try next...
                        }
                    }
                } else if(filedClass.isPrimitive()) {
                    //如果是基础类型
                    String clsName = filedClass.getSimpleName();
                    switch (clsName) {
                        case "long":
                            realObj = Long.parseLong(value);
                            break;
                        case "int":
                            realObj = Integer.parseInt(value);
                            break;
                        case "double":
                            realObj = Double.parseDouble(value);
                            break;
                        case "float":
                            realObj = Float.parseFloat(value);
                            break;
                    }
                }
                else {
                    Constructor<?> ctr = filedClass.getConstructor(String.class);
                    realObj = ctr.newInstance(value);
                }
            } catch (Exception e) {
                //如果出现任何异常
                exceptions.add(new IllegalArgumentException("can't convert " + value
                        + " to class " + filedClass.getSimpleName()));
            }
            return realObj;
        }

        public abstract void handler(QueryOptions options, ODataQuerySupport support);

        protected abstract void doHandle(ODataQuerySupport support);
    }

    private class TopOptionHandler extends ODataOptionFlowHandler {

        TopOption option;

        @Override
        public void handler(QueryOptions options, ODataQuerySupport support) {
            if((this.option = options.getQueryOption(SystemQueryOptionKind.TOP)) != null) {
                //如果存在TOP标签, 但是不存在SKIP标签
                if(options.getQueryOption(SystemQueryOptionKind.SKIP) == null) {
                    //设置偏移量为0
                    support.getQuerySupport().offset(0);
                }
                //执行Handle函数
                doHandle(support);
            }
            //如果后续还有其他处理器,继续处理吸取
            if(super.nextHandler != null) {
                super.nextHandler.handler(options, support);
            }
        }

        @Override
        protected void doHandle(ODataQuerySupport support) {
            support.getQuerySupport().limit(this.option.getValue());
        }
    }

    private class SkipOptionHandler extends ODataOptionFlowHandler {

        SkipOption option;

        @Override
        public void handler(QueryOptions options, ODataQuerySupport support) {
            if((this.option = options.getQueryOption(SystemQueryOptionKind.SKIP)) != null) {
                doHandle(support);
            }
            //如果后续还有其他处理器,继续处理吸取
            if(super.nextHandler != null) {
                super.nextHandler.handler(options, support);
            }
        }

        @Override
        protected void doHandle(ODataQuerySupport support) {
            support.getQuerySupport().offset(this.option.getValue());
        }
    }

    private class FilterOptionHandler extends ODataOptionFlowHandler {

        private FilterOption option;
        private Class<?> filterClass;

        public FilterOptionHandler(Class<?> filterClass) {
            this.filterClass = filterClass;
        }

        @Override
        public void handler(QueryOptions options, ODataQuerySupport support) {
            if((this.option = options.getQueryOption(SystemQueryOptionKind.FILTER)) != null) {
                doHandle(support);
            }
            //如果后续还有其他处理器,继续处理吸取
            if(super.nextHandler != null) {
                super.nextHandler.handler(options, support);
            }
        }

        @Override
        protected void doHandle(ODataQuerySupport support) {
            //如果不是二叉表达式, 就不需要进行处理
            if(option.getExpression() instanceof Binary) {
                Binary binary = (Binary)option.getExpression();
                //parseExpression(option.getExpression(), binary.getOperator(), support);
                try {
                    parseExpression(binary, support);
                } catch (ODataException e) {
                    e.printStackTrace();
                }
            }
        }

        /**
         * 转换表达式
         * @param expression
         * @param support
         * @throws ODataException
         */
        private void parseExpression(Expression expression, ODataQuerySupport support) throws ODataException {
            if(!(expression instanceof Binary))
                throw new ODataException("can't parse expression");

            Expression currentExpr;

            Binary currentBinary = null;
            Binary prevBinary = null;

            Stack<Expression> expressionStack = new Stack<>();
            Stack<Criterion> criterions = new Stack<>();
            Stack<Criterion> tmpCriterions = new Stack<>();

            //将根节点压入栈
            expressionStack.push(expression);
            while (!expressionStack.empty()) {
                //开始判断根节点的的类型
                currentExpr = expressionStack.peek();
                if(currentExpr instanceof Binary) {
                    currentBinary = (Binary)currentExpr;
                }
                //如果上级节点为空 或 左右子节点有一个是当前节点时
                if(prevBinary == null ||
                        prevBinary.getLeftOperand() == currentBinary ||
                        prevBinary.getRightOperand() == currentBinary) {

                    //如果左树也是二叉树, 压入处理栈
                    if(currentBinary.getLeftOperand() instanceof Binary) {
                        expressionStack.push(currentBinary.getLeftOperand());
                    }
                    //如果右树也是二叉树, 压入处理栈
                    else if(currentBinary.getRightOperand() instanceof Binary) {
                        expressionStack.push(currentBinary.getRightOperand());
                    }
                }
                else if(currentBinary.getLeftOperand() == prevBinary) {
                    if(currentBinary.getRightOperand() instanceof Binary) {
                        expressionStack.push(currentBinary.getRightOperand());
                    }
                }
                else {
                    //因为OR, AND都是二元运算, 所以做一个计数器
                    int count = 2;
                    BinaryOperatorKind opKind = currentBinary.getOperator();
                    if(opKind == BinaryOperatorKind.OR) {
                        List<Criterion> criterionList = new ArrayList<>();
                        while (count > 0 && !tmpCriterions.empty()) {
                            criterionList.add(tmpCriterions.pop());
                            count--;
                        }
                        while (count > 0 &&criterions.size() != 0) {
                            criterionList.add(criterions.pop());
                            count--;
                        }
                        Disjunction disjunction = new Disjunction(criterionList.toArray(new Criterion[0]));
                        criterions.add(disjunction);
                    } else if(opKind == BinaryOperatorKind.AND) {
                        Junction junction = new Junction(Junction.Nature.AND);
                        while (count > 0 && !tmpCriterions.empty()) {
                            junction.add(tmpCriterions.pop());
                            count --;
                        }
                        criterions.add(junction);
                    } else if(opKind == BinaryOperatorKind.EQ || opKind == BinaryOperatorKind.NE ||
                            opKind == BinaryOperatorKind.LT || opKind == BinaryOperatorKind.LE ||
                            opKind == BinaryOperatorKind.GT || opKind == BinaryOperatorKind.GE ||
                            opKind == BinaryOperatorKind.IN) {

                        String propertyName = null;
                        Object propertyValue = null;
                        Criterion criterion = null;
                        Expression leftExpr = currentBinary.getLeftOperand(),
                                rightExpr = currentBinary.getRightOperand();

                        if(leftExpr instanceof Member) {
                            Member left = (Member)leftExpr;
                            propertyName = left.getProperty().getName();

                            if(rightExpr instanceof Literal) {
                                Literal right = (Literal)rightExpr;
                                Field field = validateProperty(propertyName, filterClass, support.getParseExceptions());
                                //字段不为空
                                if(field != null) {
                                    propertyValue = transferProperty(right.getText(), field.getType(), support.getParseExceptions());
                                    //类型可以被转换
                                    if(propertyValue != null) {
                                        criterion = handleComparism(currentBinary, propertyName, propertyValue);
                                    }
                                }
                            } else if(rightExpr instanceof MultiLiteral) {
                                MultiLiteral right = (MultiLiteral)rightExpr;
                                criterion = handleContain(currentBinary, propertyName, right.getItems(), support.getParseExceptions());
                            }
                            if(criterion != null) {
                                tmpCriterions.add(criterion);
                            }
                        }
                    }
                    expressionStack.pop();
                }
                prevBinary = currentBinary;
            }

            if(criterions.empty()) {
                //如果criterions栈中为空,则表示为单一条件
                while (!tmpCriterions.empty()) {
                    support.getQuerySupport().addCriterion(tmpCriterions.pop());
                }
            } else {
                //将Criterion栈中的项目推入QuerySupport
                while (!criterions.empty()) {
                    support.getQuerySupport().addCriterion(criterions.pop());
                }
            }
        }

        private Criterion handleComparism(Binary binary, String propertyName, Object propertyValue) {
            SimpleCriterion criterion = null;
            BinaryOperatorKind kind = binary.getOperator();
            switch (kind) {
                case EQ:
                    criterion = Restrictions.eq(propertyName, propertyValue);
                    break;
                case NE:
                    criterion = Restrictions.ne(propertyName, propertyValue);
                    break;
                case LT:
                    criterion = Restrictions.lt(propertyName, propertyValue);
                    break;
                case LE:
                    criterion = Restrictions.le(propertyName, propertyValue);
                    break;
                case GT:
                    criterion = Restrictions.gt(propertyName, propertyValue);
                    break;
                case GE:
                    criterion = Restrictions.ge(propertyName, propertyValue);
                    break;
                default:
                    break;
            }
            return criterion;
        }

        private Criterion handleContain(Binary binary, String propertyName, List<String> items, List<Exception> exceptions) {
            BinaryOperatorKind kind = binary.getOperator();
            InCriterion criterion = null;
            if(kind == BinaryOperatorKind.IN) {
                //确定属性名称存在
                Field field = validateProperty(propertyName, filterClass, exceptions);
                if(field != null) {
                    List<Object> realItems = new ArrayList<>();
                    for(String item : items) {
                        Object realItem = transferProperty(item, field.getType(), exceptions);
                        if(realItem == null) {
                            continue;
                        }
                        realItems.add(realItem);
                    }
                    if(realItems.size() > 0) {
                        criterion = Restrictions.in(propertyName, realItems);
                    }
                }
            }
            return criterion;
        }
    }

    private class OrderByOptionHandler extends ODataOptionFlowHandler {

        private OrderByOption option;
        private Class<?> filterClass;

        public OrderByOptionHandler(Class<?> filterClass) {
            this.filterClass = filterClass;
        }

        @Override
        public void handler(QueryOptions options, ODataQuerySupport support) {
            if((this.option = options.getQueryOption(SystemQueryOptionKind.ORDERBY)) != null) {
                doHandle(support);
            }
            //如果后续还有其他处理器,继续处理吸取
            if(super.nextHandler != null) {
                super.nextHandler.handler(options, support);
            }
        }

        @Override
        protected void doHandle(ODataQuerySupport support) {
            List<String> propertyList = new ArrayList<>();
            //获取Order中所有的排序项目
            for (OrderByItem item : option.getOrders()) {
                Expression expr = item.getExpression();
                //如果表达式为Member表达式
                if(expr instanceof Member) {
                    String propertyName = ((Member)expr).getProperty().getName();
                    //如果OrderBy的属性存在, 且没有被重复添加
                    if(validateProperty(propertyName, filterClass, support.getParseExceptions()) != null &&
                            !propertyList.contains(propertyName)) {
                        if(item.isDescending()) {
                            support.getQuerySupport().addOrder(Order.desc(propertyName));
                        } else {
                            support.getQuerySupport().addOrder(Order.asc(propertyName));
                        }
                        propertyList.add(propertyName);
                    }
                }
            }
        }
    }

    private class CountHandler extends ODataOptionFlowHandler {

        CountOption option;

        @Override
        public void handler(QueryOptions options, ODataQuerySupport support) {
            if((this.option = options.getQueryOption(SystemQueryOptionKind.COUNT)) != null) {
                doHandle(support);
            }
            //如果后续还有其他处理器,继续处理
            if(super.nextHandler != null) {
                super.nextHandler.handler(options, support);
            }
        }

        @Override
        protected void doHandle(ODataQuerySupport support) {
            support.getQuerySupport().setHasCount(option.getValue());
        }
    }
}
