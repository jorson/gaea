package com.nd.gaea.web.model;

import com.nd.gaea.web.model.entry.*;
import com.nd.gaea.web.model.mapper.AutoModelMapper;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.modelmapper.*;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.lang.reflect.Type;
import java.util.*;

/**
 * Created by Administrator on 2014-11-25.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:/config/applicationContext.xml")
public class ViewModelScannerTest {

    @Autowired
    private ViewModelScanner scanner;

    @Before
    public void setup() {
        try{
            scanner.scanPackage();
            AutoModelMapper.getInstance().getMapperCount();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Test
    public void simpleModelMapper() {

        SimpleEntry source = new SimpleEntry();
        source.setId(1L);
        source.setAge(20);
        source.setName("Demo User");
        source.setPassword("password");
        source.setBirthday(new Date());

        NestEntry nestSource = new NestEntry();
        nestSource.setId(12);
        nestSource.setAddress("Test Address");
        nestSource.setZipCode("1234567");
        source.setNestEntry(nestSource);

        ModelMapper modelMapper = new ModelMapper();
/*        modelMapper.addMappings(new SimpleMapping());
        modelMapper.addMappings(new NestMapping());*/
        modelMapper.getConfiguration()
                .setMatchingStrategy(MatchingStrategies.LOOSE);
        SimpleViewModel target = modelMapper.map(source, SimpleViewModel.class);
        Assert.assertNotNull(target);
        Assert.assertEquals(target.getId().longValue(), 1L);
        Assert.assertEquals(target.getAge().intValue(), 20);
        Assert.assertEquals(target.getName(), "Demo User");
    }

    private class SimpleMapping extends PropertyMap<SimpleEntry, SimpleViewModel> {

        @Override
        protected void configure() {
            map().setId(source.getId());
            map().setName(source.getName());
            map().setAge(source.getAge());
        }
    }

    private class NestMapping extends PropertyMap<NestEntry, NestViewModel> {

        @Override
        protected void configure() {
            map().setId(source.getId());
            map().setAddress(source.getAddress());
        }
    }

    @Test
    public void listModelMapper() {
        List<SimpleEntry> sources = new ArrayList<>();
        SimpleEntry source = new SimpleEntry();
        source.setId(1L);
        source.setAge(20);
        source.setName("Demo User");
        source.setPassword("password");
        source.setBirthday(new Date());

        sources.add(source);

        ModelMapper modelMapper = new ModelMapper();
        Type listType = new TypeToken<List<SimpleViewModel>>(){}.getType();
        List<SimpleViewModel> result = modelMapper.map(sources, listType);
        Assert.assertNotNull(result);
    }

    @Test
    public void mapModelMapper() {
        Map<Integer, SimpleEntry> sources = new HashMap<>();
        SimpleEntry source = new SimpleEntry();
        source.setId(1L);
        source.setAge(20);
        source.setName("Demo User");
        source.setPassword("password");
        source.setBirthday(new Date());

        sources.put(1, source);
        ModelMapper modelMapper = new ModelMapper();
        //modelMapper.addMappings(new TestMapper());
        modelMapper.addConverter(new TestMapper());

        Type listType = new TypeToken<HashMap<Integer, SimpleViewModel>>(){}.getType();
        HashMap<Integer, SimpleViewModel> result = modelMapper.map(sources, listType);
        Assert.assertNotNull(result);
    }

    public class TestMapper extends AbstractConverter<HashMap<Integer, SimpleEntry>, HashMap<Integer, SimpleViewModel>> {
        @Override
        protected HashMap<Integer, SimpleViewModel> convert(HashMap<Integer, SimpleEntry> source) {
            HashMap<Integer, SimpleViewModel> target = new HashMap<>();
            for(Map.Entry<Integer, SimpleEntry> item : source.entrySet()) {
                SimpleViewModel model = new SimpleViewModel();
                model.setName(item.getValue().getName());
                target.put(item.getKey(), model);
            }
            return target;
        }
    }

    @Test
    public void convertTest() {
        SimpleEntry source = new SimpleEntry();
        source.setId(1L);
        source.setAge(20);
        source.setName("Demo User");
        source.setPassword("password");
        source.setBirthday(new Date());

        NestEntry nestSource = new NestEntry();
        nestSource.setId(12);
        nestSource.setAddress("Test Address");
        nestSource.setZipCode("1234567");
        source.setNestEntry(nestSource);

        Converter<SimpleEntry, SimpleViewModel> converter = new AbstractConverter<SimpleEntry, SimpleViewModel>() {
            public SimpleViewModel convert(SimpleEntry source) {
                SimpleViewModel result = new SimpleViewModel();
                NestViewModel nestResult = new NestViewModel();

                result.setId(source.getId());
                result.setName(source.getName() + source.getPassword());
                result.setAge(source.getAge());
                nestResult.setAddress(source.getNestEntry().getAddress());
                result.setNestViewModel(nestResult);
                return result;
            }
        };

        ModelMapper modelMapper = new ModelMapper();
        //modelMapper.addConverter(converter);
        modelMapper.addMappings(new SimpleMap());
        modelMapper.addMappings(new NestMap());;
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.LOOSE);

        SimpleViewModel target = modelMapper.map(source, SimpleViewModel.class);
        Assert.assertNotNull(target);
        Assert.assertEquals(target.getId().longValue(), 1L);
        Assert.assertEquals(target.getName(), "DemoUserpassword");
        Assert.assertEquals(target.getAge().intValue(), 20);
    }

    @Test
    public void mapperTestA() throws Exception {
        SimpleEntry source = createSimpleEntry();
        Object result = AutoModelMapper.getInstance().process(source, SimpleViewModel.class);
        if(result instanceof SimpleViewModel) {
            SimpleViewModel realResult = (SimpleViewModel)result;
            Assert.assertNotNull(realResult);
            Assert.assertEquals(realResult.getId().longValue(), 1L);
            Assert.assertEquals(realResult.getAge().intValue(), 20);
            Assert.assertEquals(realResult.getName(), "Demo User");
        } else {
            System.out.println("Type Error");
        }
    }

    @Test
    public void mapperTestB() throws Exception {
        List<SimpleEntry> sources = createSimpleEntryList();
        Object result = AutoModelMapper.getInstance().process(sources, SimpleViewModel.class);
        Assert.assertNotNull(result);
    }

    @Test
    public void mapperTestC() throws Exception {
        SimpleEntry source = createSimpleEntry();
        Object result = AutoModelMapper.getInstance().process(source, ConvertEntryViewModel.class);
        if(result instanceof ConvertEntryViewModel) {
            ConvertEntryViewModel realResult = (ConvertEntryViewModel)result;
            Assert.assertNotNull(realResult);
            Assert.assertEquals(realResult.getId().longValue(), 1L);
            Assert.assertEquals(realResult.getName(), "Demo Userpassword");
            Assert.assertEquals(realResult.getAge().intValue(), 20);
        } else {
            System.out.println("Type Error");
        }
    }

    private SimpleEntry createSimpleEntry() {
        SimpleEntry source = new SimpleEntry();
        source.setId(1L);
        source.setAge(20);
        source.setName("Demo User");
        source.setPassword("password");
        source.setBirthday(new Date());
        return source;
    }

    private List<SimpleEntry> createSimpleEntryList() {
        List<SimpleEntry> sources = new ArrayList<>();
        SimpleEntry source = new SimpleEntry();
        source.setId(1L);
        source.setAge(20);
        source.setName("Demo User");
        source.setPassword("password");
        source.setBirthday(new Date());

        sources.add(source);
        return sources;
    }

    private class SimpleMap extends PropertyMap<SimpleEntry, SimpleViewModel> {

        @Override
        protected void configure() {
            map().setId(source.getId());
            map().setAge(source.getAge());
        }
    }

    private class NestMap extends PropertyMap<NestEntry, NestViewModel> {

        @Override
        protected void configure() {
            map().setAddress(source.getAddress());
        }
    }
}
