package gaea.platform.web.controller;

import gaea.foundation.core.Constant;
import gaea.foundation.core.bo.EntityObject;
import gaea.foundation.core.repository.support.Pager;
import gaea.foundation.core.utils.ArrayUtils;
import gaea.foundation.core.utils.GenericsUtils;
import gaea.foundation.core.utils.ObjectUtils;
import gaea.platform.web.controller.support.Operation;
import gaea.platform.web.exception.ObjectNotFoundException;
import gaea.platform.web.utils.ParamUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * 请在这里输入说明
 *
 * @author bifeng.liu
 */
public abstract class OperationSupportController<T extends Object> extends BaseController implements ControllerOperation<T> {

    @Autowired(required = false)
    protected HttpServletRequest request;

    @Autowired(required = false)
    protected HttpServletResponse response;

    @RequestMapping("/{action}")
    public Object execute(@PathVariable("action") final String action, HttpServletRequest request, HttpServletResponse response, ModelMap modelMap) throws Exception {
        Method[] methods = findMethods(action);
        final Object self = this;
        if (ArrayUtils.isEmpty(methods)) {
            happenError("未找到页面:" + action, null);
        }
        for (int i = 0; i < 1; i++) {
            final Method method = methods[i];
            Operation operation = method.getAnnotation(Operation.class);
            boolean isJson = operation.isJson();
            Object ret = new ActionRedirector() {
                public Object doAction(HttpServletRequest request, HttpServletResponse response, ModelMap modelMap) throws Exception {
                    return method.invoke(self);
                }
            }.redirect(this.request, this.response, modelMap, isJson);
            if (isJson && ret != null) {
                return outputJson(response, ret);
            } else {
                return ret;
            }
        }
        return null;
    }


    @RequestMapping("/list")
    @ResponseBody
    public Object list() {
        Pager pager = getEntityList();
        return ObjectUtils.toJson(pager);
    }


    /**
     * 跳转到Add页面
     *
     * @return
     */
    @RequestMapping("/toAdd")
    public ModelAndView toAdd() {
        return new ActionRedirector() {
            @Override
            public Object doAction(HttpServletRequest request, HttpServletResponse response, ModelMap modelMap) {
                return parpareAdd();
            }
        }.redirect(this.request, this.response, null);
    }

    /**
     * 添加数据
     * <p/>
     * 执行<code>doAdd</code>方法，如果返回为null，则会直接返回空的<code>SuccessControllerResult</code>
     *
     * @param model 记录
     * @return
     */
    @RequestMapping("/add")
    @ResponseBody
    public Object add(@ModelAttribute("model") T model) {
        Object result = "";
        try {
            result = doAdd(model);
            if (result == null) {
                result = new SuccessControllerResult();
            }
        } catch (Exception e) {
            result = new ErrorControllerResult(e.getMessage());
        }
        return ObjectUtils.toJson(result);
    }

    protected abstract Object doAdd(T model);

    /**
     * 初始化添加页面，并准备相关数据
     * <p/>
     * 如果要加入数据，则必须返回<code>ModelAndView</code>，并手动在其中加入值
     *
     * @return
     */
    protected Object parpareAdd() {
        return concatPagepath(EDIT);
    }

    /**
     * 跳转到Update页面
     *
     * @return
     */
    @RequestMapping("/update-{id}")
    public ModelAndView toUpdate(@PathVariable("id") final Serializable id) {
        return new ActionRedirector() {
            @Override
            public Object doAction(HttpServletRequest request, HttpServletResponse response, ModelMap modelMap) {
                return parpareUpdate(id);
            }
        }.redirect(this.request, this.response, null);
    }

    /**
     * 更新数据
     * <p/>
     * 执行<code>doUpdate</code>方法，如果返回为null，则会直接返回空的<code>SuccessControllerResult</code>
     *
     * @param model 记录
     * @return
     */
    @RequestMapping("/update")
    @ResponseBody
    public Object update(@ModelAttribute("model") T model) {
        Object result = "";
        try {
            result = doUpdate(model);
            if (result == null) {
                result = new SuccessControllerResult();
            }
        } catch (Exception e) {
            result = new ErrorControllerResult(e.getMessage());
        }
        return ObjectUtils.toJson(result);
    }

    protected abstract Object doUpdate(T model);

    /**
     * 初始化修改页面，并准备相关数据
     * <p/>
     * 如果要加入数据，则必须返回<code>ModelAndView</code>，并手动在其中加入值
     *
     * @return
     */
    protected Object parpareUpdate(final Serializable id) {
        ModelAndView view = new ModelAndView(concatPagepath(EDIT));
        EntityObject entityObject = getModel(id);
        if (entityObject == null) {
            throw new ObjectNotFoundException(id, GenericsUtils.getSuperClassGenricType(this.getClass()).getName());
        }
        view.addObject("model", ObjectUtils.toJson(entityObject));
        return view;
    }

    /**
     * 删除数据
     * <p/>
     * 执行<code>doDelete</code>方法，如果返回为null，则会直接返回空的<code>SuccessControllerResult</code>
     *
     * @param id 记录
     * @return
     */
    @RequestMapping("/delete-{id}")
    @ResponseBody
    public String delete(@PathVariable("id") Serializable id) {
        Object result = "";
        try {
            result = doDelete(id);
            if (result == null) {
                result = new SuccessControllerResult();
            }
        } catch (Exception e) {
            result = new ErrorControllerResult(e.getMessage());
        }
        return ObjectUtils.toJson(result);
    }

    protected abstract Object doDelete(Serializable id);

    /**
     * 跳转到查看页面
     *
     * @return
     */
    @RequestMapping("/show-{id}")
    public Object show(@PathVariable("id") final Serializable id) {
        return new ActionRedirector() {
            @Override
            public Object doAction(HttpServletRequest request, HttpServletResponse response, ModelMap modelMap) {
                return parpareShow(id);
            }
        }.redirect(this.request, this.response, null);
    }

    /**
     * 初始化查看页面，并准备相关数据
     * <p/>
     * 如果要加入数据，则必须返回<code>ModelAndView</code>，并手动在其中加入值
     *
     * @return
     */
    protected Object parpareShow(final Serializable id) {
        ModelAndView view = new ModelAndView(concatPagepath(EDIT));
        EntityObject entityObject = getModel(id);
        if (entityObject == null) {
            throw new ObjectNotFoundException(id, GenericsUtils.getSuperClassGenricType(this.getClass()).getName());
        }
        view.addObject("model", ObjectUtils.toJson(entityObject));
        return view;
    }

    //-------------------------------------------------------------------------
    // 内部方法
    //-------------------------------------------------------------------------

    protected Method[] findMethods(final String action) {
        List<Method> results = new ArrayList<Method>();
        for (Class superClass = this.getClass(); superClass != OperationSupportController.class && superClass != null; superClass = superClass.getSuperclass()) {
            Method[] methods = superClass.getDeclaredMethods();
            for (Method method : methods) {
                Operation operation = method.getAnnotation(Operation.class);
                // 有注解且方法名相同
                if (operation != null && method.getName().equals(action)) {
                    results.add(method);
                }
            }
        }
        return results.toArray(new Method[]{});
    }

    /**
     * 输出JSON格式
     *
     * @param response
     * @param object
     * @return
     * @throws java.io.IOException
     */
    protected String outputJson(HttpServletResponse response, Object object) throws IOException {
        if (response != null) {
            response.getWriter().write(ObjectUtils.toJson(object));
            response.setContentType("application/json; charset=" + Constant.DEFAULT_CHARSET_NAME);
        }
        return "blank";
    }

    /**
     * 从页面中取得分页信息
     *
     * @param request
     * @return
     */
    protected Pager getPager(HttpServletRequest request) {
        int pageNo = ParamUtils.getInt(request, "pageIndex", 0);
        int pageSize = ParamUtils.getInt(request, "pageSize", 10);
        String ord = ParamUtils.getDecodeString(request, "ord", "", "utf8");
        long count = ParamUtils.getLong(request, "count", -1L);
        Pager pager = new Pager(pageNo, pageSize, count, null);
        pager.setOrderBy(ord);
        return pager;
    }

    //-------------------------------------------------------------------------
    // 抽象方法
    //-------------------------------------------------------------------------

    /**
     * 取得Entity列表
     *
     * @return
     */
    public abstract Pager getEntityList();

    /**
     * 取得操作对象
     * <p/>
     * 当为新增、修改、删除、查看时，Model为具体的查看对象
     *
     * @return
     */
    public abstract EntityObject getModel(Serializable id);

}
