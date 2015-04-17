package com.nd.gaea.repository.hibernate;


import com.nd.gaea.core.repository.query.QuerySupport;
import com.nd.gaea.core.repository.query.criterion.Criterion;
import com.nd.gaea.repository.hibernate.object.EntityRepository;
import com.nd.gaea.repository.hibernate.object.JsonEntity;
import com.nd.gaea.repository.hibernate.object.SimpleEntity;
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

import static com.nd.gaea.core.repository.query.criterion.Restrictions.*;

/**
 * 请在这里输入说明
 *
 * @author bifeng.liu
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath*:/c3p0/applicationContext.xml")
public class HibernateRepositoryTest extends AbstractJUnit4SpringContextTests {

    private EntityRepository dao;

    @Before
    public void init() {
        dao = (EntityRepository) applicationContext.getBean("entityDao");
    }

    public SimpleEntity getSimpleObject() {
        SimpleEntity simpleEntity = new SimpleEntity("simple_object", DateUtils.parse("2009-05-21", "yyyy-MM-dd"), 20, 1, "remark");
        return simpleEntity;
    }

    @Test
    public void testAddGetDelete() {
        dao.deleteAll();
        SimpleEntity simpleEntity = new SimpleEntity("simple_object", DateUtils.parse("2009-05-21", "yyyy-MM-dd"), 20, 1, "remark");
        dao.create(simpleEntity);
        SimpleEntity ret = dao.findOne(QuerySupport.createQuery().addCriterion(eq("name", "simple_object")));
        Assert.assertTrue("插入单条数据，取得值与比较值不一致", ret.getId().equals(simpleEntity.getId()));
        ret.setAge(100);
        ret.setStatus(0);
        dao.update(ret);
        ret = dao.findOne(QuerySupport.createQuery().addCriterion(eq("age", 100)));
        Assert.assertTrue("更新单条数据，取得值与比较值不一致", simpleEntity.getId().equals(ret.getId()));
        Assert.assertTrue("名称不唯一", dao.isUnique(simpleEntity, "name"));
        dao.create(ret);
        List dataList = dao.findAll();
        Assert.assertTrue("添加已存在的数据后，取得条数与比较值不一致", dataList.size() == 2);
        Assert.assertFalse("名称唯一", dao.isUnique(simpleEntity, "name"));
        dao.delete(simpleEntity.getId());
        dataList = dao.findAll();
        Assert.assertTrue("删除已存在的数据后，取得条数与比较值不一致", dataList.size() == 1);
    }

    @Test
    public void testFindQuerySupport() {
        dao.deleteAll();
        List dataList = dao.find(QuerySupport.createQuery());
        Assert.assertTrue("输入为空条件的QuerySupport时，返回列表为空", dataList.size() == 0);
        SimpleEntity simpleEntity = new SimpleEntity("simple_object", DateUtils.parse("2009-05-21", "yyyy-MM-dd"), 20, 1, "remark");
        dao.create(simpleEntity);
        dataList = dao.find(QuerySupport.createQuery().addCriterion(in("id", new Object[]{})));
        Assert.assertTrue("当IN条件值为空时，返回列表为空", dataList.size() == 0);
        //测试AND和OR
        Criterion criterion = or(new Criterion[]{eq("age", 20), eq("name", "simple_object")});
        dataList = dao.find(QuerySupport.createQuery().addCriterion(criterion));
        Assert.assertTrue("使用OR条件，取得条数与比较值不一致", dataList.size() == 1);
    }

    @Test
    public void testThrowException() {
        dao.deleteAll();
        try {
            dao.testThrowException();
        } catch (Exception ex) {

        }
        List dataList = dao.findAll();
        Assert.assertTrue("抛出异常后，数据库有插入数据", dataList.size() == 0);
    }

    @Test
    public void testCount() {
        dao.deleteAll();
        SimpleEntity simpleEntity = new SimpleEntity("simple_01", DateUtils.parse("2009-05-21", "yyyy-MM-dd"), 26, 1, "remark000");
        dao.create(simpleEntity);
        SimpleEntity simpleEntity_02 = new SimpleEntity("simple_02", DateUtils.parse("2009-05-21", "yyyy-MM-dd"), 26, 1, "remark000");
        dao.create(simpleEntity_02);
        SimpleEntity simpleEntity_03 = new SimpleEntity("nostart_simple_03", DateUtils.parse("2009-05-21", "yyyy-MM-dd"), 26, 1, "remark000");
        dao.create(simpleEntity_03);
        long count = dao.count(QuerySupport.createQuery().addCriterion(like("name", "simple%")));
        Assert.assertTrue("根据条件取得记录数，取得值与比较值不一致", count == 2);
        count = dao.count(QuerySupport.createQuery());
        Assert.assertTrue("根据条件取得记录数，取得值与比较值不一致", count == 3);
    }

    @Test
    public void testAddGetJsonObject() {
        dao.deleteAll();
        JsonEntity jsonEntity = new JsonEntity("jsonName", 123);
        List<String> datas = new ArrayList<>();
        datas.add("测试");
        datas.add("!@$%@#$&%^*__)+_~]|{\":>?<");
        jsonEntity.setValues(datas);
        SimpleEntity simpleEntity = new SimpleEntity("simple_object", DateUtils.parse("2009-05-21", "yyyy-MM-dd"), 20, 1, "remark");
        simpleEntity.setJsonEntity(jsonEntity);
        dao.create(simpleEntity);
        SimpleEntity ret = dao.findOne(QuerySupport.createQuery().addCriterion(eq("name", "simple_object")));
        Assert.assertTrue("插入单条数据，取得值与比较值不一致", ret.getJsonEntity().equals(simpleEntity.getJsonEntity()));
    }
}
