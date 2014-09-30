package gaea.web.demo.simple;

import gaea.foundation.core.repository.query.criterion.Criterion;
import gaea.foundation.core.repository.support.Pager;
import gaea.foundation.core.service.EntityService;
import gaea.foundation.core.utils.ObjectUtils;
import gaea.foundation.core.utils.StringUtils;
import gaea.platform.web.controller.BaseEntityController;
import gaea.platform.web.controller.support.Operation;
import gaea.platform.web.exception.ControllerException;
import gaea.platform.web.utils.ParamUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static gaea.foundation.core.repository.query.criterion.Restrictions.like;

/**
 * Created by Administrator on 14-9-30.
 */
@RequestMapping("/simple")
@Controller
public class SimpleEntityController extends BaseEntityController<SimpleEntity> {

    private SimpleEntity model;

    @Autowired
    private SimpleEntityService simpleEntityService;

    /**
     * 取得实体的Service
     *
     * @return
     */
    protected EntityService getEntityService() {
        return simpleEntityService;
    }

    @Override
    protected Object parpareAdd() {
        ModelAndView view = new ModelAndView(concatPagepath(EDIT));
        //view.addAllObjects();
        List<Map> status = new ArrayList<Map>();
        Map data_01 = new HashMap();
        data_01.put("code", "1");
        data_01.put("name", "正确");
        status.add(data_01);
        Map data_02 = new HashMap();
        data_02.put("code", "0");
        data_02.put("name", "错误");
        status.add(data_02);
        view.addObject("status", ObjectUtils.toJson(status));
        return view;
    }

    @Override
    protected Object doAdd(SimpleEntity simpleEntity) {
        if ("error".equalsIgnoreCase(simpleEntity.getName())) {
            throw new ControllerException("这是一个错误");
        }
        return super.doAdd(simpleEntity);
    }

    @RequestMapping("/index")
    public String index() {
        return concatPagepath("index");
    }

    @Operation(isJson = true)
    public Object testJson(){
        Pager pager = getEntityList();
        return pager;
    }

    @Operation
    public Object testPage(){
        return concatPagepath("index");
    }

    @Override
    protected List<Criterion> getSearchList() {
        List<Criterion> criterions = new ArrayList<Criterion>();
        String name = ParamUtils.getDecodeString(this.request, "name", "", "utf-8");
        if (!StringUtils.isEmpty(name)) {
            criterions.add(like("name", "%" + name + "%"));
        }
        return criterions;
    }
}
