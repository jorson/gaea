package com.nd.gaea.repository.mongodb;

import com.nd.gaea.core.repository.query.QuerySupport;
import com.nd.gaea.repository.mongodb.object.SimpleEntity;
import com.nd.gaea.repository.mongodb.object.SimpleEntityRepository;
import com.nd.gaea.utils.DateUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.List;

import static com.nd.gaea.core.repository.query.criterion.Restrictions.eq;
import static com.nd.gaea.core.repository.query.criterion.Restrictions.in;
import static com.nd.gaea.core.repository.query.criterion.Restrictions.like;

/**
 * 测试Mongodb的泛型
 *
 * @author bifeng.liu
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath*:/spring/applicationContext.xml")
public class MongodbRepositoryTest extends AbstractJUnit4SpringContextTests {

    SimpleEntityRepository dao = null;

    @Before
    public void init() {
        dao = (SimpleEntityRepository) applicationContext.getBean("simpleEntityDao");
    }

    @Test
    public void testAddGetRemove() {
        //直接删除集合，清空集合数据
        dao.dropCollection();
        List dataList = getDataList();
        SimpleEntity simpleObject = new SimpleEntity("simple_single", 66, 3, DateUtils.parse("2009-05-21", "yyyy-MM-dd"));
        simpleObject.setId("11111");
        dao.create(simpleObject);
        SimpleEntity ret = dao.findOne(QuerySupport.createQuery().addCriterion(eq("name", "simple_single")));
        Assert.assertTrue("插入单条数据，取得值与比较值不一致", simpleObject.equals(ret));
        ret = dao.get("11111");
        Assert.assertTrue("根据ID插入单条数据，取得值与比较值不一致", simpleObject.equals(ret));
        // 插入多条
        simpleObject.setAge(100);
        for (int i = 0; i < dataList.size(); i++) {
            SimpleEntity data = (SimpleEntity) dataList.get(i);
            dao.create(data);
        }
        List rets = dao.findAll();
        Assert.assertEquals("插入多条数据，取得条数与比较值不一致", rets.size(), 5);
        //查询不存在的数据
        ret = dao.get("bbbbb");
        Assert.assertNull("取得不存在的数据，取得不为NULL", ret);
        rets = dao.find(QuerySupport.createQuery().addCriterion(eq("name", "ccccc")));
        Assert.assertEquals("取得不存在的数据列表，取得条数不为0", rets.size(), 0);
        //删除数据
        ret = dao.findOne(QuerySupport.createQuery().addCriterion(eq("name", "simple_01")));
        dao.delete(ret.getId());
        rets = dao.find(QuerySupport.createQuery().addCriterion(eq("type", 1)));
        Assert.assertEquals("删除一条数据，取得条数与比较值不一致", rets.size(), 2);
        QuerySupport querySupport = QuerySupport.createQuery().addCriterion(eq("name", "simple_single"));
        ret = dao.findOne(querySupport);
        ret.setAge(50);
        dao.update(ret);
        ret = dao.findOne(querySupport);
        Assert.assertEquals("更新单条数据，取得更新值与比较值不一致", ret.getAge(), 50);
        rets = dao.find(QuerySupport.createQuery().addCriterion(eq("type", 1)));
        String[] ids = new String[3];
        for (int i = 0; i < rets.size(); i++) {
            SimpleEntity data = (SimpleEntity) rets.get(i);
            ids[i] = data.getId();
        }
        Assert.assertEquals("通过getList(ids)取得数据列表，取得条数与比较值不一致", dao.getList(ids).size(), 2);
        // 设置一个不存在的数据
        ids[2] = "bbbbbb";
        Assert.assertEquals("通过getList(ids)取得数据列表，取得条数与比较值不一致", dao.getList(ids).size(), 2);
    }

    @Test
    public void testFindQuerySupport() {
        //直接删除集合，清空集合数据
        dao.dropCollection();
        List dataList = dao.find(QuerySupport.createQuery());
        Assert.assertTrue("输入为空条件的QuerySupport时，返回列表为空", dataList.size() == 0);
        dataList = getDataList();
        for (int i = 0; i < dataList.size(); i++) {
            SimpleEntity data = (SimpleEntity) dataList.get(i);
            dao.create(data);
        }
        dataList = dao.find(QuerySupport.createQuery());
        Assert.assertTrue("输入为空条件的QuerySupport时，取得条数与比较值不一致", dataList.size() == 4);
        SimpleEntity simpleObject = new SimpleEntity("multi_object", 66, 3, DateUtils.parse("2009-05-21", "yyyy-MM-dd"));
        dao.create(simpleObject);
        dataList = dao.find(QuerySupport.createQuery().addCriterion(in("id", new Object[]{})));
        Assert.assertTrue("当IN条件值为空时，返回列表不为空", dataList.size() == 0);
        dataList = dao.find(QuerySupport.createQuery().addCriterion(like("name", "simple")));
        Assert.assertTrue("使用Like条件查询，取得条数与比较值不一致", dataList.size() == 4);
    }

    private List<SimpleEntity> getDataList() {
        List dataList = new ArrayList();
        dataList.add(new SimpleEntity("simple_01", 10, 1, DateUtils.parse("2011-01-09", "yyyy-MM-dd")));
        dataList.add(new SimpleEntity("simple_02", 30, 1, DateUtils.parse("2013-03-22", "yyyy-MM-dd")));
        dataList.add(new SimpleEntity("simple_03", 3, 1, DateUtils.parse("2002-05-11", "yyyy-MM-dd")));
        dataList.add(new SimpleEntity("simple_04", 55, 2, DateUtils.parse("2002-05-11", "yyyy-MM-dd")));
        return dataList;
    }
}
