package gaea.foundation.core.utils;

import gaea.foundation.core.utils.collection.CollectionFilter;
import gaea.foundation.core.utils.collection.CollectionSelector;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.*;

/**
 * 集合工具类的测试类
 */
public class CollectionUtilsTest {
    List<InnerObject> datas = new ArrayList<InnerObject>();

    @Before
    public void init() {
        datas.add(new InnerObject(1, "User01"));
        datas.add(new InnerObject(2, "User02"));
        datas.add(new InnerObject(100, "User100"));
        datas.add(new InnerObject(101, "User101"));
        datas.add(new InnerObject(102, "User102"));
    }


    @Test
    public void testWhere() {
        List<InnerObject> ret = (List<InnerObject>) CollectionUtils.where(datas, new CollectionFilter() {
            public boolean filter(Object data) {
                InnerObject d = (InnerObject) data;
                return d.id >= 100;
            }
        });
        Assert.assertTrue("输入有效的列表数据，返回值不正确", ret.size() == 3 && ret instanceof List);
    }

    @Test
    public void testSelect() {
        List<String> ret = (List<String>) CollectionUtils.select(datas, new CollectionSelector() {
            public Object select(Object data) {
                InnerObject d = (InnerObject) data;
                return d.name;
            }
        });
        Assert.assertTrue("输入有效的列表数据，返回值不正确", ret.size() == 5 && ret instanceof List && ret.get(0).equals("User01"));
    }


    @Test
    public void testNewInstance() {
        Assert.assertTrue("输入ArrayList列表对象，返回类型不正确", ArrayList.class.isAssignableFrom(CollectionUtils.newInstance(new ArrayList<String>()).getClass()));
        Assert.assertTrue("输入Vector列表对象，返回类型不正确", Vector.class.isAssignableFrom(CollectionUtils.newInstance(new Vector()).getClass()));
        Assert.assertTrue("输入TreeSet列表对象，返回类型不正确", TreeSet.class.isAssignableFrom(CollectionUtils.newInstance(new TreeSet<String>()).getClass()));
    }

    class InnerObject {

        private int id;
        private String name;
        private Date birthday;

        public InnerObject(int id, String name) {
            this.id = id;
            this.name = name;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Date getBirthday() {
            return birthday;
        }

        public void setBirthday(Date birthday) {
            this.birthday = birthday;
        }
    }
}
